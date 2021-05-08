package side;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import static side.NashEq.takeWaitingTime;

/**
 * Objectleri Server'a gönderen Thread ve onun Runnable classı
 */
// merhaba 2
public class sendObjects implements Runnable {

    final static Counter counter = new Counter();
    private int sendObjectSleep;
    private LinkedBlockingQueue<Message> outgoingMessage;
    private Socket clientSocket;
    RandomVariable randomVariable = new RandomVariable();
    ClientAnalysis clientAnalysis = new ClientAnalysis();
    BlockingQueue<DelayObject> DQ = new DelayQueue<DelayObject>();

    private final double award = 3;
    Rtt rtt = new Rtt(0.05);

    public sendObjects(LinkedBlockingQueue<Message> outgoingMessage, Socket clientSocket, int sendObjectSleep) {

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


        while (true) {
            //alttaki kod bekletilen nesnelerden süresi bitmişleri tekrar queue ya sokuyor
            try {
                Message delayedMessage = DQ.peek().message;
                if (delayedMessage.getPriority()<=0.9){
                    delayedMessage.setPriority(delayedMessage.getPriority()+0.1);
                }else{
                    delayedMessage.setPriority(1);
                }
                outgoingMessage.add(DQ.poll().message);
                System.out.println("Tekrar gönderiliyor");

            } catch (Exception e) {
            }

           /*
            Buraya wait Thread methodu ekleyecektim. Loop sürekli dönmesin
            Queue boş olduğunda beklesin diye.
            ---EKLEMEDIM SADECE IF STATEMENT---
            */

            if (!outgoingMessage.isEmpty()) {

                ObjectOutputStream outToServer;
                try {
                    int randomSending = randomVariable.getRandomVariable();

                    Thread.sleep(sendObjectSleep + randomSending);
                    //outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
                    //System.out.println(outgoingMessage.peek().getMessage()+"  "+outgoingMessage.peek().getTopic()+"  Queue size is "+outgoingMessage.size());

                    //poll-> return and remove yapiyor.

                    Message passenger = outgoingMessage.peek();
                    if(passenger.isDelayed()){

                    }else{
                        passengerPriority = priority.priorityAssigner(passenger.getMessage());
                    }

                    //timer
                    float rttTimeStart = System.nanoTime();
                    outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
                    outToServer.writeObject(outgoingMessage.poll());


                    if (NashEq.action(passenger.getSize(),
                            rttOfMessage,
                            passengerPriority,
                            award,
                            QueueOccupancyReceiver.queueOccupancy,
                            QueueOccupancyReceiver.queueOccupancy
                            )) {
                        outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
                        outToServer.writeObject(outgoingMessage.poll());

                        //System.out.println("ife giriyorum sıyırdım");
                        // System.out.println("rttStart "+rttTimeStart+" | rttend"+rttTimeEnd+" | aradaki fark"+ rttTime + " Timer: "+dfrtt.format(rttTime));

                        double sampleRtt = (System.nanoTime() - rttTimeStart) / 1000000;

                        rttOfMessage = rtt.calculateRTT(sampleRtt, rtt.calculateEstimatedRtt(sampleRtt));

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
                        passenger.setPriority(passengerPriority);
                        long delayTime= takeWaitingTime(passenger.getSize(),
                                rttOfMessage,
                                passengerPriority,
                                award,
                                QueueOccupancyReceiver.queueOccupancy,
                                QueueOccupancyReceiver.queueOccupancy);
                        DelayObject delayObject = new DelayObject(outgoingMessage.poll(),delayTime);
                        DQ.add(delayObject);
                    }

                } catch (IOException ex) {            // buraya yada+++++++
                    System.out.println("Server connection closed!");
                    clientAnalysis.printArr();
                    System.exit(1);
                    break;
                } catch (InterruptedException ex) {        // buraya eklenecek
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
