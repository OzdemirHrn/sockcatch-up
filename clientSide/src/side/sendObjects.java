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
    final static int limitOfMessage = 10000;
    private final String topic;
    private final int sendObjectSleep;
    private LinkedBlockingDeque<Message> outgoingMessage;
    private final Socket clientSocket;

    RandomVariable randomVariable = new RandomVariable();
    ClientAnalysis clientAnalysis = new ClientAnalysis();
    BlockingQueue<DelayObject> DQ = new DelayQueue<>();

    private final double award = 3;
    Rtt rtt = new Rtt(0.05);
    NashEq nashEq = new NashEq();
    boolean rttFirstCome = false;

    public sendObjects(LinkedBlockingDeque<Message> outgoingMessage, Socket clientSocket, int sendObjectSleep, String topic) {
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


        while (!counter.isReachedToLimit(limitOfMessage)) {

            //alttaki kod bekletilen nesnelerden süresi bitmişleri tekrar queue ya sokuyor


            try {
                Message delayedMessage = DQ.poll().message  ;
                if (delayedMessage.getPriority() <= 0.9) {
                    delayedMessage.setPriority(delayedMessage.getPriority() + 0.1);
                } else {
                    delayedMessage.setPriority(1);
                }
                delayedMessage.setDelayedTrue();
                if (delayedMessage.getCounter() == 4) {
                    DQ.poll();
                } else {
                    outgoingMessage.addFirst(delayedMessage);
                }

            } catch (Exception e) {
            }



           /*
            Buraya wait Thread methodu ekleyecektim. Loop sürekli dönmesin
            Queue boş olduğunda beklesin diye.
            ---EKLEMEDIM SADECE IF STATEMENT---
            */

            if (!outgoingMessage.isEmpty()) {

                //System.out.println("topic= "+topic);

                ObjectOutputStream outToServer;
                try {
                    int randomSending = randomVariable.getRandomVariable();


                    //outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
                    //System.out.println(outgoingMessage.peek().getMessage()+"  "+outgoingMessage.peek().getTopic()+"  Queue size is "+outgoingMessage.size());

                    //poll-> return and remove yapiyor.

                    Message passenger = outgoingMessage.peek();
                    if (passenger.isDelayed()) {
                        System.out.println("Delayed Message is trying to send "+passenger.getMessage());
                        fileWriter.write("Delayed Message is trying to send\n");
                        passengerPriority = passenger.getPriority();
                    } else {
                        passengerPriority = priority.priorityAssigner(passenger.getMessage());
                        passenger.setPriority(passengerPriority);
                    }

                    //timer
                    long rttTimeStart = System.nanoTime();

                    if (nashEq.action(passenger.getSize(),
                            rttOfMessage,
                            passengerPriority,
                            award,
                            QueueOccupancyReceiver.queueOccupancy,
                            QueueOccupancyReceiver.queueOccupancy,
                            fileWriter)) {

                        passenger.setRtt(rttOfMessage);

                        outToServer = new ObjectOutputStream(clientSocket.getOutputStream());

                        System.out.println();
                        passenger.setCounter(passenger.getCounter()+1);

                        //Son gönderdiğim mesaj <-- First Consistent Data
                        priority.setFirstConsistentData(passenger.getMessage());
                        //priority.setTempAnnealing(0.1);
                        outToServer.writeObject(outgoingMessage.poll());


                        long sampleRtt = (System.nanoTime() - rttTimeStart);
                        Thread.sleep(160);

                        if (rttFirstCome){
                            rttOfMessage = rtt.calculateRTT(sampleRtt, rtt.calculateEstimatedRtt(sampleRtt));


                        }


                        rttFirstCome = true;

                        //timer
                        counter.increment();



                        clientAnalysis.publishersTimer(counter);

                        float son = System.nanoTime();
                        DecimalFormat df = new DecimalFormat("#.###");
                        double time = son - start1;
                        double time1 = time % 1000000;
                        time = (time - time1) / 1000000;
                        String counterTime = "Counter: " + counter.getCounter() + " Timer: " + df.format(time) +"  ";
                        fileWriter.write(counterTime);
                        //System.out.println(counterTime);
                    }

                    /*
                     * Nash Eq. Gönderme kararı alınca buraya düşecek.
                     */

                    else {
                        System.out.println("Don't Send to Server! Wait Until a While!");
                        fileWriter.write("Don't Send to Server! Wait Until a While!\n");
                        passenger.setPriority(passengerPriority);
                        passenger.setDelayedTrue();
                        passenger.setRtt(rttOfMessage);
                        double delayTime = takeWaitingTime(passenger.getSize(),
                                rttOfMessage,
                                passengerPriority,
                                award,
                                QueueOccupancyReceiver.queueOccupancy,
                                QueueOccupancyReceiver.queueOccupancy);
                        DelayObject delayObject = new DelayObject(outgoingMessage.poll(), delayTime);
                        DQ.add(delayObject);
                    }

                } catch (IOException ex) {
                    System.out.println("Server connection closed!");
                    clientAnalysisPrint(fileWriter);
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

        try {

            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(sendObjects.class.getName()).log(Level.SEVERE, null, ex);
        }

        clientAnalysisPrint(fileWriter);
    }

    private void clientAnalysisPrint(FileWriter fileWriter) {
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
    }

}
