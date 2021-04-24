package side;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
/**
 * @author harunOzdemir
 */
public class ClientSide {

    static float Qmin,Qmax;
    /*
    Gidecek mesajların beklediği Queue
    Thread Safe için Blocking Queue kullandım. Ama tekrar bakılabilir -----
    */
    public static void main(String... topic) throws Exception {

        int createObjectSleep = Integer.parseInt(topic[1]);
        int sendObjectSleep = Integer.parseInt(topic[2]);
        int capacityOfQueue = Integer.parseInt(topic[3]);
        int datasetRow = Integer.parseInt(topic[4]);


        LinkedBlockingQueue<Message> goingMessages = new LinkedBlockingQueue<Message>(capacityOfQueue);

        /*
        Client side main methodundan  argument alıyor.
        Bu argument topic olarak görev yapıyor.
        Bu client sadece bu topice message yolluyor
        */
        Socket clientSocket = new Socket("192.168.1.33", 6789);
        /*
        Qmin ve Qmax'ı buradan alsam direkt???

         */

        ObjectInputStream ois = null;

        ois = new ObjectInputStream(clientSocket.getInputStream());
        String message = (String) ois.readObject();
        Qmin=Float.parseFloat(message.substring(0,3));
        Qmax=Float.parseFloat(message.substring(5,8));
        System.out.println(Qmin+"  "+Qmax);

        Runnable receivingQueueOcc = new QueueOccupancyReceiver(clientSocket);
        Thread threadReceivingQueueOcc = new Thread(receivingQueueOcc);
        threadReceivingQueueOcc.start();

       /*
        Message sınıfından topic ve random value argumentleriyle
        objectler oluşturan Thread.
        */
        Runnable creatingObject = new createObjects(goingMessages, topic[0], createObjectSleep, capacityOfQueue,datasetRow);
        Thread threadCreatingObject = new Thread(creatingObject);
        threadCreatingObject.start();

        /*
        Bu objectleri clientSocket'e gönderen Thread.
        */
        Runnable sendingObjects = new sendObjects(goingMessages, clientSocket, sendObjectSleep);
        Thread threadSendingObjects = new Thread(sendingObjects);
        threadSendingObjects.start();

    }

}


