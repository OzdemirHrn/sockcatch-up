package side;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
/**
 * @author harunOzdemir
 */
public class ClientSide {

    public static void main(List<String> config) throws Exception {

        int createObjectSleep = Integer.parseInt(config.get(1));
        int sendObjectSleep = Integer.parseInt(config.get(2));
        int capacityOfQueue = Integer.parseInt(config.get(3));
        int datasetRow = Integer.parseInt(config.get(4));

        LinkedBlockingQueue<Message> goingMessages = new LinkedBlockingQueue<>(capacityOfQueue);

        Socket clientSocket = new Socket("192.168.1.35", 6789);

        Runnable creatingObject = new createObjects(goingMessages, config.get(0), createObjectSleep, capacityOfQueue,datasetRow);
        Thread threadCreatingObject = new Thread(creatingObject);
        threadCreatingObject.start();
        
        Runnable sendingObjects = new sendObjects(goingMessages, clientSocket, sendObjectSleep);
        Thread threadSendingObjects = new Thread(sendingObjects);
        threadSendingObjects.start();

    }

}


