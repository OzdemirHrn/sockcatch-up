package side;

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


    // test variables
    static int incrementOfCounter = 50;                 // tutulma aralığını buraya yaz, ne sıklıkla tutulacak
    static int totalCounter = incrementOfCounter;
    static int limitOfCounter = 100;               // nereye kadar gidecek buraya onu yaz 30.000 e gidecekse bunu buraya yaz
    static int SizeOfArray = (int) (limitOfCounter / incrementOfCounter) + 1;
    static int arrayIndexNumber = 0;
    static float[] arr = new float[SizeOfArray];
    static int[] QueueSize = new int[SizeOfArray];
    static int[] droppedSize = new int[SizeOfArray];

    private LinkedBlockingQueue<Message> incomingMessage;

    public PrintQueue(LinkedBlockingQueue<Message> incomingMessage, int printSleepTime, Queue<WelcomeMessages> allClients) {
        this.allClients = allClients;
        this.incomingMessage = incomingMessage;
        this.printSleepTime = printSleepTime;
    }

    void publishersTimer(int incomingMessageSize) {

        if ((counter.getCounter() == totalCounter) && (counter.getCounter() <= limitOfCounter)) {
            end = System.nanoTime() - start;
            arr[arrayIndexNumber] = (float) (end / 1e6);
            QueueSize[arrayIndexNumber] = incomingMessageSize;
            arrayIndexNumber++;
            totalCounter = totalCounter + incrementOfCounter;
            int totalDropped = 0;
            for (WelcomeMessages eachClient : allClients) {

                // System.out.println("Dropped Messages" + eachClient.dropped);
                totalDropped += eachClient.dropped;

            }
            droppedSize[arrayIndexNumber] = totalDropped;

            //System.out.println("Total Number of Dropped Messages: " + totalDropped);
        }

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

                publishersTimer(incomingMessage.size());
                /*
                artık message'yi değil operationu yolluyorum.
                 */
                operation(incomingMessage.peek().getTopic(), incomingMessage.peek().getOperation());

                incomingMessage.poll();
            }

            if (counter.getCounter() == limitOfCounter) {
                int totalDropped = 0;
                printArr();
                while (!allClients.isEmpty()) {
                    System.out.println("Dropped Messages" + allClients.peek().dropped);
                    totalDropped += allClients.poll().dropped;
                }

                System.out.println("Total Number of Dropped Messages: " + totalDropped);
                System.exit(1);
            }
        }

    }

    public void printArr() {
        int i;
        int counterNumber = 0;
        for (i = 0; i < arr.length; i++) {
            counterNumber = counterNumber + incrementOfCounter;
            System.out.println("Counter number: " + counterNumber + " time: " + arr[i] + "  Queue size is " + QueueSize[i] + " Dropped count: " + droppedSize[i]);
        }

    }

    public void operation(String messageDeviceInfo, int messageOperation) {

        if (messageOperation > 4000) {

            int periot = 0;
            for (int i = 0; i < 7000000; i++) {
                periot++;
                if (periot == 700000) {
                    System.out.print("");
                }
            }
        } else if (messageOperation > 3000) {

            int periot = 0;
            for (int i = 0; i < 700000; i++) {
                periot++;
                if (periot == 70000) {
                    System.out.print("");
                }
            }
        } else if (messageOperation > 2000) {

        } else if (messageOperation > 1000) {

            int periot = 0;
            for (int i = 0; i < 70000000; i++) {
                periot++;
                if (periot == 7000000) {
                    System.out.print("");
                }
            }
        } else {
            System.out.println("case:else buraya girmememiz lazım");
        }

    }

}
