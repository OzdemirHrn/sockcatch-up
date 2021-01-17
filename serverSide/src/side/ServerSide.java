package side;

import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author harunOzdemir
 */
class ServerSide {

    static final int droppedTotal = 0;
    private static ExecutorService service = Executors.newFixedThreadPool(20);
    private static Queue<Task> allClients = new LinkedList();
    /*
    Bu queue gelen objectleri tutuyor. Daha sonra bu objectleri bir thread subscriber gibi ekrana bastÄ±racak
     */
    private static LinkedBlockingQueue<Message> comingMessages = new LinkedBlockingQueue<Message>(100);
    //  private static LinkedBlockingQueue<Message> dropQueue = new LinkedBlockingQueue<Message>();

    public static void main(String argv[]) throws Exception {

        int printSleepTime = 1;
        /*
        Welcoming socket with 6789 port
        TCP connectionlarÄ± iÃ§in Network dersi SlaytlarÄ±na bakÄ±n!
         */
        ServerSocket welcomeSocket = new ServerSocket(6789);
        Runnable printObjects = new PrintQueue(comingMessages, printSleepTime, allClients);
        /*Bu thread queuedaki alÄ±nan objeleri sanki subscirber gibi
        ekrana bastÄ±rÄ±yor
         */
        Thread threadPrintingObjects = new Thread(printObjects);
        threadPrintingObjects.start();

        /*
        Bu loop farklÄ± clientlerden gelen mesajlarÄ± karÅŸÄ±lÄ±yor
        Gelen her clienti farklÄ± bir thread karÅŸÄ±lÄ±yor
        BÃ¶yle olmasaydÄ± aynÄ± anda tek clientten veri alabilirdik.
         */
        while (true) {
            //ServerSocket.accept() is blocking method and blocks until a socket connection made.
            System.out.println("Server is waiting for client connection...");
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("Client connected!  " + connectionSocket.getPort());
            Task task = new Task(connectionSocket, comingMessages);
            allClients.add(task);

            service.submit(task);

        }
    }
}

//Counter Counter Counter Counter Counter Counter Counter Counter Counter
class Counter {

    private AtomicInteger c = new AtomicInteger(0);

    public int increment() {
        c.getAndIncrement();
        return c.get();
    }

    public int getCounter() {
        return c.get();
    }
}

class Once {

    private final AtomicBoolean done = new AtomicBoolean();

    public void run(Runnable task) {
        if (done.get()) {
            return;
        }
        if (done.compareAndSet(false, true)) {
            task.run();
        }
    }
}

class PrintQueue implements Runnable {

    static Queue<Task> allClients;
    final static Counter counter = new Counter();
    final Once once = new Once();
    static float start = System.nanoTime();
    static float end;
    int printSleepTime;
    /*
    Subscriber gÃ¶revi yapacak thread'in Runnable classÄ±
     */

    // test variables
    static int incrementOfCounter = 50;                    // tutulma aralığını buraya yaz, ne sıklıkla tutulacak
    static int totalCounter = incrementOfCounter;
    static int limitOfCounter = 10000;                                  // nereye kadar gidecek buraya onu yaz 30.000 e gidecekse bunu buraya yaz
    static int SizeOfArray = (int) (limitOfCounter / incrementOfCounter) + 1;
    static int arrayIndexNumber = 0;
    static float arr[] = new float[SizeOfArray];
    static int QueueSize[] = new int[SizeOfArray];
    static int droppedSize[] = new int[SizeOfArray];

    private LinkedBlockingQueue<Message> incomingMessage;

    public PrintQueue(LinkedBlockingQueue<Message> incomingMessage, int printSleepTime, Queue<Task> allClients) {
        this.allClients = allClients;
        this.incomingMessage = incomingMessage;
        this.printSleepTime = printSleepTime;
    }

    static void publishersTimer(int incomingMessageSize) {

        if ((counter.getCounter() == totalCounter) && (counter.getCounter() <= limitOfCounter)) {
            end = System.nanoTime() - start;
            arr[arrayIndexNumber] = (float) (end / 1e6);
            QueueSize[arrayIndexNumber] = incomingMessageSize;
            arrayIndexNumber++;
            totalCounter = totalCounter + incrementOfCounter;
            int totalDropped = 0;
            for (Task eachClient : allClients) {

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
                System.out.println(" Message:"+ incomingMessage.peek().getMessage() + " From:" + incomingMessage.peek().getTopic() + "  Queue size is " + incomingMessage.size());

                publishersTimer(incomingMessage.size());
                operation(incomingMessage.peek().getTopic(), incomingMessage.peek().getMessage());

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
            System.out.println("Counter number: " + counterNumber + " time: " + arr[i] + "  Queue size is " + QueueSize[i]+" Dropped count: "+ droppedSize[i]);
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

final class Task implements Runnable {

    /*
    Bu class gelen objectleri karÅŸÄ±lan executor thread havuzunun classÄ±
    Objectler burada karÅŸÄ±landÄ±ktan sonra queueya alÄ±nÄ±yor.
     */
    private Socket client = null;
    int dropped = 0;
    private int clientMessage;
    private LinkedBlockingQueue<Message> incomingMessage;

    public Task(Socket clientSocket, LinkedBlockingQueue<Message> incomingMessage) {
        this.incomingMessage = incomingMessage;
        this.client = clientSocket;

    }

    @Override
    public void run() {

        while (true) {

            try {
                InputStream inputStream = client.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                /*
                Queueya objectlerin eklendiÄŸi kÄ±sÄ±m
                 */

                try {
                    incomingMessage.add(((Message) objectInputStream.readObject()));
                } catch (IllegalStateException | ClassNotFoundException e) {
                    dropped++;
                    //System.out.println("dropped count: "+ dropped );
                }

            } catch (IOException ex) {

                try {
                    /*
                    BaÄŸlantÄ± kopmasÄ± durumunda connection kapatÄ±lÄ±yor.
                     */
                    client.close();
                    System.out.println("Connection Reset " + client.getPort());
                    break;

                } catch (IOException ex1) {
                    Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex1);
                }

            }
        }

    }

}
