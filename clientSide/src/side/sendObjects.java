package side;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Objectleri Server'a gönderen Thread ve onun Runnable classı
 */

public class sendObjects implements Runnable {

    final static Counter counter = new Counter();
    static float start = System.nanoTime();
    static float end;
    static float[] arr = new float[20];
    private int sendObjectSleep;
    private static final Random random = new Random();
    private LinkedBlockingQueue<Message> outgoingMessage;
    private Socket clientSocket;


    public sendObjects(LinkedBlockingQueue<Message> outgoingMessage, Socket clientSocket, int sendObjectSleep) {

        this.outgoingMessage = outgoingMessage;
        this.clientSocket = clientSocket;
        this.sendObjectSleep = sendObjectSleep;

    }

    //TIMER
    static void publishersTimer() {

        int interval = 50;
        for (int i = 0; i < 20; i++) {
            if (counter.getCounter() == interval) {
                end = System.nanoTime() - start;
                arr[i] = (float) (end / 1e6);
                break;
            }
            interval += 50;
        }


    }

    @Override
    public void run() {

        final float start1 = System.nanoTime();

        while (true) {

            if (!outgoingMessage.isEmpty()) {

                ObjectOutputStream outToServer;
                try {

                    int randomSending = getRandomVariable();
                    Thread.sleep(sendObjectSleep + randomSending);
                    //outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
                    //System.out.println(outgoingMessage.peek().getMessage()+"  "+outgoingMessage.peek().getTopic()+"  Queue size is "+outgoingMessage.size());

                    outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
                    outToServer.writeObject(outgoingMessage.poll());

                    //timer
                    counter.increment();
                    publishersTimer();

                    float son = System.nanoTime();
                    DecimalFormat df = new DecimalFormat("#.###");
                    double time = son - start1;
                    double time1 = time % 1000000;
                    time = (time - time1) / 1000000;
                    System.out.println("Counter: " + counter.getCounter() + " Timer: " + df.format(time));


                } catch (IOException ex) {            // buraya yada+++++++
                    System.out.println("Server connection closed!");
                    printArr();
                    System.exit(1);
                    break;
                } catch (InterruptedException ex) {        // buraya eklenecek
                    Logger.getLogger(createObjects.class.getName()).log(Level.SEVERE, null, ex);
                    printArr();
                }
            }
        }
        try {

            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(sendObjects.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getRandomVariable() {
        int randomSending = random.nextInt(100);

        if (randomSending < 5) {
            randomSending = random.nextInt(7);
        } else if (randomSending < 10) {
            randomSending = randomSending * 5;
        } else if (randomSending < 20) {
            randomSending = randomSending * 7;
        } else if (randomSending < 30) {
            randomSending = random.nextInt(3);
        } else if (randomSending < 40) {
            randomSending = randomSending * 4;
        } else if (randomSending < 50) {
            randomSending = random.nextInt(3);
        } else if (randomSending < 60) {
            randomSending = randomSending * 5;
        } else if (randomSending < 70) {
            randomSending = random.nextInt(7);
        } else if (randomSending < 80) {
            randomSending = randomSending * 9;
        } else if (randomSending < 93) {
            randomSending = 0;
        }
        return randomSending;
    }

    public void printArr() {
        int i;
        int counterNumber = 0;
        for (i = 0; i < arr.length; i++) {
            counterNumber = counterNumber + 50;
            System.out.println("Counter number: " + counterNumber + " time: " + arr[i]);
        }

    }

}
