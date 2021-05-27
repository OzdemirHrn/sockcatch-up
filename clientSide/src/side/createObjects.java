package side;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;


public class createObjects implements Runnable {

    /*
     Objectleri üreten Thread'in Runnable classı
     */
    private LinkedBlockingDeque<Message> outgoingMessage;
    private float message;
    private int operation;
    private String topic;
    private float size;
    private static final Random random = new Random();
    private int createObjectSleep;
    private int capacityOfQueue;
    private int count;
    static final Object o = new Object();

    public createObjects(LinkedBlockingDeque<Message> outgoingMessage, String topic, int createObjectSleep, int capacityOfQueue, int count, float sizeOfSensor) {
        this.outgoingMessage = outgoingMessage;
        this.topic = topic;
        this.createObjectSleep = createObjectSleep;
        this.capacityOfQueue = capacityOfQueue;
        this.count = count;
        this.size = (float) (sizeOfSensor * 0.1 + Math.random() * 0.1);
    }

    @Override
    public void run() {
        int dropped = 0;
        int index = 0;

        ArrayList<Float> temperatureSensor1 = null;
        try {
            temperatureSensor1 = DataSet.readFromFile(count);
        } catch (IOException e) {
            e.printStackTrace();
        }


        while (true) {


            try {
                Thread.sleep(createObjectSleep);
            } catch (InterruptedException ex) {
                Logger.getLogger(createObjects.class.getName()).log(Level.SEVERE, null, ex);
            }

            operation = createRandomNumberBetween(1, 100);

            try {

                message = temperatureSensor1.get(index);
                index++;
                outgoingMessage.add(new Message(topic, message, operation, size));


            } catch (IndexOutOfBoundsException e) {

            }
            synchronized (o) {
                // Calling wait() will block this thread until another thread
                // calls notify() on the object.
                o.notify();
            }
        }


    }


    private int createRandomNumberBetween(int min, int max) {

        int selectOperationType = (random.nextInt(3) + 1) * 1000;
        return selectOperationType + 1;

    }

}
