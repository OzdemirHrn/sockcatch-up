package side;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
/**
 * @author harunOzdemir
 */
public class ClientSide {

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

        /*
        Client side main methodundan  argument alıyor.
        Bu argument topic olarak görev yapıyor.
        Bu client sadece bu topice message yolluyor
        */
        Socket clientSocket = new Socket("192.168.1.42", 6789);
        /*
        Qmin ve Qmax'ı buradan alsam direkt???

         */

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
        Runnable sendingObjects = new sendObjects(goingMessages, clientSocket, sendObjectSleep, config.get(0));
        Thread threadSendingObjects = new Thread(sendingObjects);
        threadSendingObjects.start();

        threadCreatingObject.join();
        threadSendingObjects.join();

        System.exit(1);
    }

}


