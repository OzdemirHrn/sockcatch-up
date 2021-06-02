package side;

import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.concurrent.BlockingQueue;

import static side.DelayObject.takeWaitingTime;
import static side.SendObjects.award;
import static side.SendObjects.rttAlpha;


public class SendDelayedObject implements Runnable {

    private final int sendObjectSleep;
    private final String topic;
    private final Socket clientSocket;
    private final BlockingQueue<DelayObject> DQ;
    private String toFile = "";

    public SendDelayedObject(BlockingQueue<DelayObject> DQ, Socket clientSocket, int sendObjectSleep, String topic) {

        this.DQ = DQ;
        this.clientSocket = clientSocket;
        this.sendObjectSleep = sendObjectSleep;
        this.topic = topic;
    }

    ClientAnalysis clientAnalysis = new ClientAnalysis();

    Rtt rtt = new Rtt(rttAlpha);
    NashEq nashEq = new NashEq();
    boolean rttFirstCome = false;

    @Override
    public void run() {

        final float start1 = System.nanoTime();
        double rttOfMessage = 0;
        double passengerPriority;

        FileWriter fileWriter = new FileOperations().createInputfile("Delayed " + topic);

        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ObjectOutputStream outToServer;
            try {
                Message delayedMessage = DQ.poll().message;

                if (delayedMessage.getPriority() <= 0.95) {
                    delayedMessage.setPriority(delayedMessage.getPriority() + 0.05);
                } else {
                    delayedMessage.setPriority(1);
                }
                if (delayedMessage.getCounter() == 4) {
                    fileWriter.write("dropped\n");
                    MultipleClients.counter.decrementtsizeOfDelayedQueue();
                   // System.out.println("dropped");
                    MultipleClients.counter.incrementDroppedMessagesAfterSeveralAttempts();
                    MultipleClients.delayedMessagesPriority.get(4).add(delayedMessage.getInitialPriorityIfDelayed());
                } else {

                    passengerPriority = delayedMessage.getPriority();
                    long rttTimeStart = System.nanoTime();
                    if (nashEq.action(delayedMessage.getSize(),
                            rttOfMessage,
                            passengerPriority,
                            award,
                            QueueOccupancyReceiver.queueOccupancy,
                            QueueOccupancyReceiver.queueOccupancy,
                            fileWriter)) {

                        switch (delayedMessage.getCounter()) {
                            case 1 -> {
                                MultipleClients.counter.incrementSendMessageInSecondAttempt();
                                MultipleClients.delayedMessagesPriority.get(1).add(delayedMessage.getInitialPriorityIfDelayed());
                            }
                            case 2 -> {
                                MultipleClients.counter.incrementSendMessageInThirdAttempt();
                                MultipleClients.delayedMessagesPriority.get(2).add(delayedMessage.getInitialPriorityIfDelayed());
                            }
                            case 3 -> {
                                MultipleClients.counter.incrementSendMessageInFourthAttempt();
                                MultipleClients.delayedMessagesPriority.get(3).add(delayedMessage.getInitialPriorityIfDelayed());
                            }
                            default -> System.err.println("Illegal Message Delayed Counter Value");
                        }

                        MultipleClients.counter.decrementtsizeOfDelayedQueue();
                        outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
                        outToServer.writeObject(delayedMessage);

                        long sampleRtt = (System.nanoTime() - rttTimeStart);
                        if (rttFirstCome)
                            rttOfMessage = rtt.calculateRTT(sampleRtt, rtt.calculateEstimatedRtt(sampleRtt));
                        rttFirstCome = true;

                        //timer
                        MultipleClients.counter.incrementTotalMessageCounter();

                        clientAnalysis.publishersTimer(MultipleClients.counter);

                        float son = System.nanoTime();
                        DecimalFormat df = new DecimalFormat("#.###");
                        double time = son - start1;
                        double time1 = time % 1000000;
                        time = (time - time1) / 1000000;
                        String counterTime = "Counter: " + MultipleClients.counter.getCounter() + " Timer: " + df.format(time) + "  ";
                        fileWriter.write(counterTime);
                     //   System.out.println("delayedobject" + counterTime);
                        fileWriter.write("gönderdim xd\n");
                      //  System.out.println("gönderdim xd");
                    } else {
                       // System.out.println("Again Don't Send to Server! Wait Until a While! ");
                        fileWriter.write("Again Don't Send to Server! Wait Until a While!\n");
                        double delayTime = takeWaitingTime(delayedMessage.getSize(),
                                rttOfMessage,
                                passengerPriority,
                                award,
                                QueueOccupancyReceiver.queueOccupancy,
                                QueueOccupancyReceiver.queueOccupancy);
                        delayedMessage.setCounter(delayedMessage.getCounter() + 1);
                        DelayObject delayObject = new DelayObject(delayedMessage, delayTime);
                        DQ.add(delayObject);

                    }
                }


            } catch (Exception e) {

            }



        }
    }

}
