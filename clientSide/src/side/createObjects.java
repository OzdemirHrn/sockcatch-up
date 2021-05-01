package side;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;


public class createObjects implements Runnable {

    /*
     Objectleri üreten Thread'in Runnable classı
     */
    private LinkedBlockingQueue<Message> outgoingMessage;
    private float message;
    private int operation;
    private String topic;
    private float size = 0.5f;
    private final float award = 4;

    private static final Random random = new Random();
    private int createObjectSleep;
    private int capacityOfQueue;
    private int count;
    static final Object o=new Object();

    public createObjects(LinkedBlockingQueue<Message> outgoingMessage, String topic, int createObjectSleep, int capacityOfQueue, int count) {
        this.outgoingMessage = outgoingMessage;
        this.topic = topic;
        this.createObjectSleep = createObjectSleep;
        this.capacityOfQueue = capacityOfQueue;
        this.count = count;
    }

    @Override
    public void run() {
        int dropped = 0;
        int index = 0;
        /*
        obje oluştursam.


         */




        ArrayList<Float> temperatureSensor1 = null;
        try {
            temperatureSensor1 = DataSet.readFromFile(count);
        } catch (IOException e) {
            e.printStackTrace();
        }


        while (true) {


            /*

            priority objesinden emthod çağırsam current value ile..
             */


            try {
                Thread.sleep(createObjectSleep);
            } catch (InterruptedException ex) {
                Logger.getLogger(createObjects.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*
            Belli aralıklarla yeni objectler oluşturup bunları Queue'ya atıyorum.
            MQTT'deki publisher client görevi.
            */

            /*
            Burada artık random olmayacak.
             */
            operation = createRandomNumberBetween(1, 100);


            try {
                if (outgoingMessage.size() < capacityOfQueue) {

                    message = temperatureSensor1.get(index);
                    index++;
                    //System.out.println(message);
                    outgoingMessage.add(new Message(topic, message, operation, size));



                } else {
                    dropped++;
                    System.out.println("Drop sayısı:" + dropped);
                }

            } catch (IndexOutOfBoundsException e) {

            }
            synchronized(o) {
                // Calling wait() will block this thread until another thread
                // calls notify() on the object.
                o.notify();
            }
        }



    }


    private int createRandomNumberBetween(int min, int max) {

        int randomNumber = random.nextInt(max - min + 1) + min;//1-100
        int selectOperationType = (random.nextInt(3) + 1) * 1000;
        return selectOperationType + randomNumber;

    }

}
