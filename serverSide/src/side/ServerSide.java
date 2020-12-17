package side;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author harunOzdemir
 */

class ServerSide {

    private static ExecutorService service = Executors.newFixedThreadPool(3);
    /*
    Bu queue gelen objectleri tutuyor. Daha sonra bu objectleri bir thread subscriber gibi ekrana bastıracak
    */
    private static LinkedBlockingQueue<Message> comingMessages = new LinkedBlockingQueue<Message>();

    public static void main(String argv[]) throws Exception {
        /*
        Welcoming socket with 6789 port
        TCP connectionları için Network dersi Slaytlarına bakın!
        */
        ServerSocket welcomeSocket = new ServerSocket(6789);
        Runnable printObjects = new PrintQueue(comingMessages);
        /*Bu thread queuedaki alınan objeleri sanki subscirber gibi
        ekrana bastırıyor
        */
        Thread threadPrintingObjects = new Thread(printObjects);
        threadPrintingObjects.start();

        /*
        Bu loop farklı clientlerden gelen mesajları karşılıyor
        Gelen her clienti farklı bir thread karşılıyor
        Böyle olmasaydı aynı anda tek clientten veri alabilirdik.
        */
        while (true) {
            //ServerSocket.accept() is blocking method and blocks until a socket connection made. 
            System.out.println("Server is waiting for client connection...");
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("Client connected!  " + connectionSocket.getPort());
            Task task = new Task(connectionSocket, comingMessages);

            service.submit(task);

        }
    }
}

class PrintQueue implements Runnable {
    /*
    Subscriber görevi yapacak thread'in Runnable classı
    */

    private LinkedBlockingQueue<Message> incomingMessage;

    public PrintQueue(LinkedBlockingQueue<Message> incomingMessage) {
        this.incomingMessage = incomingMessage;

    }

    @Override
    public void run() {
        while (true) {
            try {
                /*
                Threadi kaç saniyede bir queuedan veriyi alıp ekrana basması için uyutuyorum
                */
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(PrintQueue.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (!incomingMessage.isEmpty()) {
                /*
                Queue'nun head elemanın bastırdıktan sonra onu queuedan alıyoruz ekrana bastırıyoruz.
                */
                System.out.println(incomingMessage.peek().getMessage() + "  " + incomingMessage.peek().getTopic() + "  Queue size is " + incomingMessage.size());
                incomingMessage.poll();

            }
        }

    }
}

final class Task implements Runnable {

    /*
    Bu class gelen objectleri karşılan executor thread havuzunun classı
    Objectler burada karşılandıktan sonra queueya alınıyor.
    */
    private Socket client = null;
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
                Queueya objectlerin eklendiği kısım
                */
                incomingMessage.add(((Message) objectInputStream.readObject()));
                
            } catch (IOException ex) {

                try {
                    /*
                    Bağlantı kopması durumunda connection kapatılıyor.
                    */
                    client.close();
                    System.out.println("Connection Reset " + client.getPort());
                    break;
                    
                } catch (IOException ex1) {
                    Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex1); }
               
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
