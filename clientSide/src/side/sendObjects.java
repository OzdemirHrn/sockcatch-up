package side;

import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

import static side.DelayObject.takeWaitingTime;

/**
 * Objectleri Server'a gönderen Thread ve onun Runnable classı
 */

public class sendObjects implements Runnable {

    final static Counter counter = new Counter();
    private final String topic;
    private String toFile = "";
    private int sendObjectSleep;
    private LinkedBlockingDeque<Message> outgoingMessage;
    private Socket clientSocket;
    RandomVariable randomVariable = new RandomVariable();
    ClientAnalysis clientAnalysis = new ClientAnalysis();
    BlockingQueue<DelayObject> DQ;

    private final double award = 3;
    Rtt rtt = new Rtt(0.05);
    NashEq nashEq = new NashEq();
    boolean rttFirstCome = false;

    public sendObjects(BlockingQueue<DelayObject> DQ,LinkedBlockingDeque<Message> outgoingMessage, Socket clientSocket, int sendObjectSleep, String topic) {
        this.DQ = DQ;
        this.topic = topic;
        this.outgoingMessage = outgoingMessage;
        this.clientSocket = clientSocket;
        this.sendObjectSleep = sendObjectSleep;
    }

    @Override
    public void run() {

        final float start1 = System.nanoTime();
        IPriority priority = new PriorityKorcak();
        double rttOfMessage = 0;
        double passengerPriority;

        FileWriter fileWriter = new FileOperations().createInputfile(topic);




        while (true) {

            if (!outgoingMessage.isEmpty()) {

                ObjectOutputStream outToServer;
                try {
                    int randomSending = randomVariable.getRandomVariable();
                    Thread.sleep(400);
                    //outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
                    //System.out.println(outgoingMessage.peek().getMessage()+"  "+outgoingMessage.peek().getTopic()+"  Queue size is "+outgoingMessage.size());

                    Message passenger = outgoingMessage.peek();
                    passengerPriority = priority.priorityAssigner(passenger.getMessage());

                    //timer
                    long rttTimeStart = System.nanoTime();

                    if (nashEq.action(passenger.getSize(),
                            rttOfMessage,
                            passengerPriority,
                            award,
                            QueueOccupancyReceiver.queueOccupancy,
                            QueueOccupancyReceiver.queueOccupancy,
                            fileWriter)) {

                        outToServer = new ObjectOutputStream(clientSocket.getOutputStream());

                        System.out.println();

                        outToServer.writeObject(outgoingMessage.poll());

                        long sampleRtt = (System.nanoTime() - rttTimeStart);

                        if (rttFirstCome)
                            rttOfMessage = rtt.calculateRTT(sampleRtt, rtt.calculateEstimatedRtt(sampleRtt));
                        rttFirstCome = true;

                        //timer
                        counter.increment();


                        clientAnalysis.publishersTimer(counter);

                        float son = System.nanoTime();
                        DecimalFormat df = new DecimalFormat("#.###");
                        double time = son - start1;
                        double time1 = time % 1000000;
                        time = (time - time1) / 1000000;
                        String counterTime = "Counter: " + counter.getCounter() + " Timer: " + df.format(time) + "  ";
                        fileWriter.write(counterTime);
                        System.out.println(counterTime);
                    }

                    else {
                        System.out.println("Don't Send to Server! Wait Until a While!");
                        fileWriter.write("Don't Send to Server! Wait Until a While!\n");
                        passenger.setPriority(passengerPriority);
                        passenger.setDelayedTrue();
                        double delayTime = takeWaitingTime(passenger.getSize(),
                                rttOfMessage,
                                passengerPriority,
                                award,
                                QueueOccupancyReceiver.queueOccupancy,
                                QueueOccupancyReceiver.queueOccupancy);
                        passenger.setCounter(passenger.getCounter() + 1);
                        DelayObject delayObject = new DelayObject(outgoingMessage.poll(), delayTime);
                        DQ.add(delayObject);

                    }

                } catch (IOException ex) {
                    System.out.println("Server connection closed!");
                    try {
                        clientAnalysis.printArr(fileWriter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Thread.currentThread().interrupt();
                    return;
                } catch (InterruptedException ex) {
                    Logger.getLogger(createObjects.class.getName()).log(Level.SEVERE, null, ex);
                    try {
                        clientAnalysis.printArr(fileWriter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
       /* try {

            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(sendObjects.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

}
