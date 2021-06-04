package side;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

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
        int sizeOfSensor = Integer.parseInt(config.get(5));
        int sensorType = Integer.parseInt(config.get(6));

        LinkedBlockingDeque<Message> goingMessages = new LinkedBlockingDeque<>(capacityOfQueue);

        /*
        Client side main methodundan  argument alıyor.
        Bu argument topic olarak görev yapıyor.
        Bu client sadece bu topice message yolluyor
        */
        Socket clientSocket = new Socket("25.57.115.7", 6789);

        /*
        Message sınıfından topic ve random value argumentleriyle
        objectler oluşturan Thread.
        */
        Runnable creatingObject = new createObjects(goingMessages, config.get(0), createObjectSleep, capacityOfQueue,datasetRow,sizeOfSensor);
        Thread threadCreatingObject = new Thread(creatingObject);
        threadCreatingObject.start();

        /*
        Bu objectleri clientSocket'e gönderen Thread.
        */
        Runnable sendingObjects = new SendObjects(goingMessages, clientSocket, sendObjectSleep, config.get(0),sensorType);
        Thread threadSendingObjects = new Thread(sendingObjects);
        threadSendingObjects.start();



        threadSendingObjects.join();

       // new Histogram();
        Thread.sleep(10000);
        System.exit(1);
    }

}


