package side;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
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
    private double size = 0.5;
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
            temperatureSensor1 = readFromFile(count);
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


    private static ArrayList<Float> readFromFile(int count) throws IOException {
        File file = new File("C:\\Users\\hrnoz\\IdeaProjects\\sockcatch-up\\clientSide\\src\\dataset1.txt");
        byte[] directlyToClient = new byte[(int) file.length()];
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream;
        bufferedInputStream = new BufferedInputStream(fileInputStream);
        bufferedInputStream.read(directlyToClient, 0, directlyToClient.length);
        String str = new String(directlyToClient);
        String[] strSplit;
        strSplit = str.split(" ");
        ArrayList<Float> temperatureSensor1 = new ArrayList<>();
        ArrayList<Float> co2Sensor1 = new ArrayList<>();

        while (count < strSplit.length) {
            temperatureSensor1.add(Float.parseFloat(strSplit[count]));
            co2Sensor1.add(Float.parseFloat(strSplit[count + 3]));
            count += 23;
        }

        /*for (Float f : temperatureSensor1) {
            System.out.println(f);

        }*/

        /*for (Float f : co2Sensor1) {
            System.out.println(f);

        }*/

        return temperatureSensor1;

    }

    private int createRandomNumberBetween(int min, int max) {

        int randomNumber = random.nextInt(max - min + 1) + min;//1-100
        int selectOperationType = (random.nextInt(3) + 1) * 1000;
        return selectOperationType + randomNumber;

    }

}
