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


    final static int limitOfMessage = 1500;
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


        FileWriter fileWriter = new FileOperations().createInputfile(topic);


        while (!MultipleClients.counter.isReachedToLimit(limitOfMessage)) {

            if (!outgoingMessage.isEmpty()) {

                ObjectOutputStream outToServer;
                try {

                    int sending = sensorType.operatingFrequency();
                    Thread.sleep(sending);

                    //priority.setCounter(1);
                    outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
                    MultipleClients.counter.incrementSendMessageInFirstAttempt();

                    System.out.println();

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

}
