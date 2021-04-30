package side;


import java.io.ObjectOutputStream;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;


/**
 *
 * @author harunOzdemir
 */

public class ServerSide {

    static final int droppedTotal = 0;
    private static ExecutorService service = Executors.newFixedThreadPool(20);
    private static Queue<WelcomeMessages> allClients = new LinkedList();
    static float Qmin=20,Qmax=80;
    // q değeri
    /*
    Bu queue gelen objectleri tutuyor. Daha sonra bu objectleri bir thread subscriber gibi ekrana bastıracak
    */
    private static LinkedBlockingQueue<Message> comingMessages = new LinkedBlockingQueue<Message>(100);

    public static void main(String[] argv) throws Exception {

        int printSleepTime = 1;
        /*
        Welcoming socket with 6789 port
         */
        ServerSocket welcomeSocket = new ServerSocket(6789);
        Runnable printObjects = new PrintQueue(comingMessages, printSleepTime, allClients);
        /*Bu thread queuedaki alınan objeleri sanki subscriber gibi
        ekrana bastırıyor
         */
        Thread threadPrintingObjects = new Thread(printObjects);
        threadPrintingObjects.start();


        while (true) {
            //ServerSocket.accept() is blocking method and blocks until a socket connection made.
            System.out.println("Server is waiting for client connection...");
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("Client connected!  " + connectionSocket.getPort());
            //Burada göndersem qmin ve qmax'ı ???????
            ObjectOutputStream toServerQminQmax;
            toServerQminQmax = new ObjectOutputStream(connectionSocket.getOutputStream());
            toServerQminQmax.writeObject(Qmin+" "+Qmax);


            WelcomeMessages welcomeMessages = new WelcomeMessages(connectionSocket, comingMessages);
            QueueInfo queueInfo = new QueueInfo(connectionSocket,comingMessages);


            allClients.add(welcomeMessages);
            service.submit(queueInfo);
            service.submit(welcomeMessages);

        }
    }
}

