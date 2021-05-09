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
    BlockingQueue<DelayObject> DQ = new DelayQueue<DelayObject>();

    private final double award = 3;
    Rtt rtt = new Rtt(0.05);
    boolean rttFirstCome = false;

    public sendObjects(LinkedBlockingDeque<Message> outgoingMessage, Socket clientSocket, int sendObjectSleep,String topic) {
        this.topic = topic;
        this.outgoingMessage = outgoingMessage;
        this.clientSocket = clientSocket;
        this.sendObjectSleep = sendObjectSleep;
    }

    @Override
    public void run() {

        final float start1 = System.nanoTime();
        Priority priority = new Priority();
        double rttOfMessage = 0;
        double passengerPriority = 0;

        FileWriter fileWriter = FileOperations.createInputfile(topic);


        while (true) {

            //alttaki kod bekletilen nesnelerden süresi bitmişleri tekrar queue ya sokuyor

            try {
                Message delayedMessage = DQ.peek().message;
                if (delayedMessage.getPriority()<=0.9){
                    delayedMessage.setPriority(delayedMessage.getPriority()+0.1);
                }else{
                    delayedMessage.setPriority(1);
                }
                delayedMessage.setDelayedTrue();
                delayedMessage.setCounter(delayedMessage.getCounter()+1);
                if(delayedMessage.getCounter()==4){
                    DQ.poll();
                }
                outgoingMessage.addFirst(DQ.poll().message);
                //System.out.println("Tekrar gönderiliyor");
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

                    Thread.sleep(sendObjectSleep + randomSending);
                    //outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
                    //System.out.println(outgoingMessage.peek().getMessage()+"  "+outgoingMessage.peek().getTopic()+"  Queue size is "+outgoingMessage.size());

                    //poll-> return and remove yapiyor.

                    Message passenger = outgoingMessage.peek();
                    if(passenger.isDelayed()){
                        System.out.println("Delayed Message is trying to send");
                    }else{
                        passengerPriority = priority.priorityAssigner(passenger.getMessage());
                    }

                    //timer
                    long rttTimeStart = System.nanoTime();

                    if (NashEq.action(passenger.getSize(),
                                        rttOfMessage,
                                        passengerPriority,
                                        award,
                                        QueueOccupancyReceiver.queueOccupancy,
                                        QueueOccupancyReceiver.queueOccupancy  ))
                    {

                        outToServer = new ObjectOutputStream(clientSocket.getOutputStream());

                        System.out.println();
                        outToServer.writeObject(outgoingMessage.poll());


                        long sampleRtt = (System.nanoTime() - rttTimeStart) ;


                        if(rttFirstCome)
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
                        System.out.println("Counter: " + counter.getCounter() + " Timer: " + df.format(time));
                    }

                    /*
                     * Nash Eq. Gönderme kararı alınca buraya düşecek.
                     */

                    else {
                        System.out.println("Don't Send to Server! Wait Until a While!");
                        passenger.setPriority(passengerPriority);
                        passenger.setDelayedTrue();
                        double delayTime= takeWaitingTime(passenger.getSize(),
                                rttOfMessage,
                                passengerPriority,
                                award,
                                QueueOccupancyReceiver.queueOccupancy,
                                QueueOccupancyReceiver.queueOccupancy);
                        DelayObject delayObject = new DelayObject(outgoingMessage.poll(),delayTime);
                        DQ.add(delayObject);
                    }

                } catch (IOException ex) {
                    System.out.println("Server connection closed!");
                    clientAnalysis.printArr();

                    try {
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.exit(1);
                    break;
                } catch (InterruptedException ex) {
                    Logger.getLogger(createObjects.class.getName()).log(Level.SEVERE, null, ex);
                    clientAnalysis.printArr();
                }
            }
        }
        try {

            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(sendObjects.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
