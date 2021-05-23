package side;

import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.text.DecimalFormat;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/*
Subscriber görevi yapacak thread'in Runnable classı
 */

public class PrintQueue implements Runnable {

    private Queue<WelcomeMessages> allClients;
    private final static Counter counter = new Counter();
    private static float start = System.nanoTime();
    private static float end;
    private int printSleepTime;
    private boolean isTerminated = false;
    private ServerSocket serverSocket;
    ServerAnalysis serverAnalysis = new ServerAnalysis();
    ServerOperation serverOperation = new ServerOperation();


    private LinkedBlockingQueue<Message> incomingMessage;

    public PrintQueue(LinkedBlockingQueue<Message> incomingMessage, int printSleepTime, Queue<WelcomeMessages> allClients, ServerSocket welcomeSocket) {
        this.allClients = allClients;
        this.incomingMessage = incomingMessage;
        this.printSleepTime = printSleepTime;
        this.serverSocket = welcomeSocket;
    }


    @Override
    public void run() {

        final float start1 = System.nanoTime();
        while (true) {

            if (!incomingMessage.isEmpty()) {
                counter.increment();
                float son = System.nanoTime();
                DecimalFormat df = new DecimalFormat("#.###");
                double time = son - start1;
                double time1 = time % 1000000;
                time = (time - time1) / 1000000;

                System.out.print("Counter: " + counter.getCounter() + " Timer: " + df.format(time));
                System.out.println(" Message:" + incomingMessage.peek().getMessage() + " From:" + incomingMessage.peek().getTopic() + "  Queue size is " + incomingMessage.size());

                serverAnalysis.publishersTimer(incomingMessage.size(), counter, start, allClients);

                try {
                    serverOperation.operation(incomingMessage.peek().getTopic(), incomingMessage.peek().getOperation());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                incomingMessage.poll();
            }

            if (counter.getCounter() >= serverAnalysis.limitOfCounter) {
                if (!isTerminated) {
                    int totalDropped = 0;
                    try {
                        serverAnalysis.printArr();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    while (!allClients.isEmpty()) {
                        System.out.println("Dropped Messages" + allClients.peek().dropped);
                        totalDropped += allClients.poll().dropped;
                    }

                    System.out.println("Total Number of Dropped Messages: " + totalDropped);
                }
//                try {
//                    serverSocket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                isTerminated = true;
                System.exit(0);
//                if (incomingMessage.size() == 0)

            }
        }

    }


}
