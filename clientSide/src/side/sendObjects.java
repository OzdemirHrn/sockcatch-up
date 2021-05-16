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


    private final double award = 3;

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

        FileWriter fileWriter = new FileOperations().createInputfile(topic);

        while (true) {


            if (!outgoingMessage.isEmpty()) {


                ObjectOutputStream outToServer;
                try {
                    int randomSending = randomVariable.getRandomVariable();

                    Thread.sleep(sendObjectSleep + randomSending);

                    Message passenger = outgoingMessage.peek();


                    outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
                    System.out.println();

                    outToServer.writeObject(outgoingMessage.poll());
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

    }

}
