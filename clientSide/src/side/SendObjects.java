package side;

import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * Objectleri Server'a gönderen Thread ve onun Runnable classı
 */

public class SendObjects implements Runnable {


    final static int limitOfMessage = 10000;
    private final String topic;
    private final int sendObjectSleep;
    private LinkedBlockingDeque<Message> outgoingMessage;
    private final Socket clientSocket;
    private SensorType sensorType;
    ClientAnalysis clientAnalysis = new ClientAnalysis();



    public SendObjects(LinkedBlockingDeque<Message> outgoingMessage, Socket clientSocket, int sendObjectSleep, String topic, int sensorType) {

        this.topic = topic;
        this.outgoingMessage = outgoingMessage;
        this.clientSocket = clientSocket;
        this.sendObjectSleep = sendObjectSleep;
        this.sensorType = new SensorType(sensorType);
    }

    @Override
    public void run() {

        final float start1 = System.nanoTime();
        IPriority priority = new PriorityKorcak();
        double passengerPriority;

        FileWriter fileWriter = new FileOperations().createInputfile(topic);


        while (!MultipleClients.counter.isReachedToLimit(limitOfMessage)) {

            if (!outgoingMessage.isEmpty()) {

                ObjectOutputStream outToServer;
                try {
                    int sending = sensorType.operatingFrequency();
                    Thread.sleep(sending);
                    //outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
                    //System.out.println(outgoingMessage.peek().getMessage()+"  "+outgoingMessage.peek().getTopic()+"  Queue size is "+outgoingMessage.size());

                    //poll-> return and remove yapiyor.

                    Message passenger = outgoingMessage.peek();
                    passengerPriority = priority.priorityAssigner(passenger.getMessage());
                    passenger.setPriority(passengerPriority);
                    passenger.setInitialPriorityIfDelayed(passengerPriority);

                    //priority.setCounter(1);
                    outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
                    MultipleClients.counter.incrementSendMessageInFirstAttempt();
                    MultipleClients.delayedMessagesPriority.get(0).add(passengerPriority);
                    System.out.println();
                    passenger.setCounter(passenger.getCounter() + 1);

                    //Son gönderdiğim mesaj <-- First Consistent Data
                    priority.setFirstConsistentData(passenger.getMessage());
                    //priority.setTempAnnealing(0.1);
                    outToServer.writeObject(outgoingMessage.poll());


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
                    System.out.println(counterTime);


                } catch (IOException ex) {
                    System.out.println("Server connection closed!");
                    clientAnalysisPrint(fileWriter);
                    break;
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
            Logger.getLogger(SendObjects.class.getName()).log(Level.SEVERE, null, ex);
        }

        Thread.currentThread().interrupt();
    }

    private void clientAnalysisPrint(FileWriter fileWriter) {

        try {

            clientAnalysis.printArr(fileWriter);
            FileWriter fileWriterCounter = new FileOperations().createInputfile("Total & Dropped Message Counter");
            fileWriterCounter.write("Total Dropped Messages: " + MultipleClients.counter.getDroppedMessagesAfterSeveralTrialAttempt() + "\n" +
                    "Has Send in First Attempt: " + MultipleClients.counter.getSendMessageInFirstAttempt() + "\n"
            );
            fileWriterCounter.close();
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
