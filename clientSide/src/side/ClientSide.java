package side;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
/**
 * @author harunOzdemir
 */
public class ClientSide {

    static int Qmin,Qmax;
    /*
    Gidecek mesajların beklediği Queue
    Thread Safe için Blocking Queue kullandım. Ama tekrar bakılabilir -----
    */
    public void main(List<String> config) throws Exception {

        int createObjectSleep = Integer.parseInt(config.get(1));
        int sendObjectSleep = Integer.parseInt(config.get(2));
        int capacityOfQueue = Integer.parseInt(config.get(3));
        int datasetRow = Integer.parseInt(config.get(4));


        LinkedBlockingDeque<Message> goingMessages = new LinkedBlockingDeque<>(capacityOfQueue);
        BlockingQueue<DelayObject> DQ = new DelayQueue<>();
        /*
        Client side main methodundan  argument alıyor.
        Bu argument topic olarak görev yapıyor.
        Bu client sadece bu topice message yolluyor
        */
        Socket clientSocket = new Socket("192.168.1.42", 6789);
        /*
        Qmin ve Qmax'ı buradan alsam direkt???

         */

        ObjectInputStream ois;

        ois = new ObjectInputStream(clientSocket.getInputStream());
        String message = (String) ois.readObject();
        Qmin= Integer.parseInt(message.substring(0,2));
        Qmax=Integer.parseInt(message.substring(5,7));
        System.out.println(Qmin+"  "+Qmax);

        Runnable receivingQueueOcc = new QueueOccupancyReceiver(clientSocket);
        Thread threadReceivingQueueOcc = new Thread(receivingQueueOcc);
        threadReceivingQueueOcc.start();

       /*
        Message sınıfından topic ve random value argumentleriyle
        objectler oluşturan Thread.
        */
        Runnable creatingObject = new createObjects(goingMessages, config.get(0), createObjectSleep, capacityOfQueue,datasetRow);
        Thread threadCreatingObject = new Thread(creatingObject);
        threadCreatingObject.start();

        /*
        Bu objectleri clientSocket'e gönderen Thread.
        */
        Runnable sendingObjects = new sendObjects(DQ,goingMessages, clientSocket, sendObjectSleep, config.get(0));
        Thread threadSendingObjects = new Thread(sendingObjects);
        threadSendingObjects.start();

        Runnable sendDelayedObjects = new SendDelayedObject(DQ , clientSocket, sendObjectSleep,config.get(0));
        Thread threadSendDelayedObject = new Thread(sendDelayedObjects);
        threadSendDelayedObject.start();


        threadSendDelayedObject.join();
        threadCreatingObject.join();
        threadReceivingQueueOcc.join();
        threadSendingObjects.join();


        System.exit(1);
    }

}


