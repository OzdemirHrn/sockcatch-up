package side;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
    Objectleri Server'a gönderen Thread ve onun Runnable classı
    */

public class sendObjects implements Runnable {

    final static Counter counter = new Counter();
    static float start = System.nanoTime();
    static float end;
    static float[] arr = new float[20];
    private int sendObjectSleep;
    private static final Random random = new Random();
    private LinkedBlockingQueue<Message> outgoingMessage;
    private Socket clientSocket;
    static double estimatedRtt=0;

    public sendObjects(LinkedBlockingQueue<Message> outgoingMessage, Socket clientSocket, int sendObjectSleep) {
        this.outgoingMessage = outgoingMessage;
        this.clientSocket = clientSocket;
        this.sendObjectSleep = sendObjectSleep;
    }

    //TIMER
    static void publishersTimer() {

        if (counter.getCounter() == 50) {
            end = System.nanoTime() - start;
            arr[0] = (float) (end / 1e6);

        } else if (counter.getCounter() == 100) {
            end = System.nanoTime() - start;
            arr[1] = (float) (end / 1e6);

        } else if (counter.getCounter() == 150) {
            end = System.nanoTime() - start;
            arr[2] = (float) (end / 1e6);

        } else if (counter.getCounter() == 200) {
            end = System.nanoTime() - start;
            arr[3] = (float) (end / 1e6);

        } else if (counter.getCounter() == 250) {
            end = System.nanoTime() - start;
            arr[4] = (float) (end / 1e6);

        } else if (counter.getCounter() == 300) {
            end = System.nanoTime() - start;
            arr[5] = (float) (end / 1e6);

        } else if (counter.getCounter() == 350) {
            end = System.nanoTime() - start;
            arr[6] = (float) (end / 1e6);

        } else if (counter.getCounter() == 400) {
            end = System.nanoTime() - start;
            arr[7] = (float) (end / 1e6);

        } else if (counter.getCounter() == 450) {
            end = System.nanoTime() - start;
            arr[8] = (float) (end / 1e6);

        } else if (counter.getCounter() == 500) {
            end = System.nanoTime() - start;
            arr[9] = (float) (end / 1e6);

        } else if (counter.getCounter() == 550) {
            end = System.nanoTime() - start;
            arr[10] = (float) (end / 1e6);

        } else if (counter.getCounter() == 600) {
            end = System.nanoTime() - start;
            arr[11] = (float) (end / 1e6);

        } else if (counter.getCounter() == 650) {
            end = System.nanoTime() - start;
            arr[12] = (float) (end / 1e6);

        } else if (counter.getCounter() == 700) {
            end = System.nanoTime() - start;
            arr[13] = (float) (end / 1e6);

        } else if (counter.getCounter() == 750) {
            end = System.nanoTime() - start;
            arr[14] = (float) (end / 1e6);

        } else if (counter.getCounter() == 800) {
            end = System.nanoTime() - start;
            arr[15] = (float) (end / 1e6);

        } else if (counter.getCounter() == 850) {
            end = System.nanoTime() - start;
            arr[16] = (float) (end / 1e6);

        } else if (counter.getCounter() == 900) {
            end = System.nanoTime() - start;
            arr[17] = (float) (end / 1e6);

        } else if (counter.getCounter() == 950) {
            end = System.nanoTime() - start;
            arr[18] = (float) (end / 1e6);

        } else if (counter.getCounter() == 1000) {
            System.out.println(counter.getCounter());
            arr[19] = (float) (end / 1e6);

        }
    }

    @Override
    public void run() {
        // Burada first consistent mesaj olan, ilk mesajı almaya çalışıyoruö
      /*  try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        synchronized(createObjects.o) {
            try {
                // Calling wait() will block this thread until another thread
                // calls notify() on the object.
                createObjects.o.wait();
            } catch (InterruptedException e) {
                // Happens if someone interrupts your thread.
            }
        }

        Priority priority = new Priority();

        //bu time'ın burda başlaması bence doğru değil.
        //Bu timer yanlış ölçüyor zaten
        final float start1 = System.nanoTime();

        while (true) {
           /*
            Buraya wait Thread methodu ekleyecektim. Loop sürekli dönmesin
            Queue boş olduğunda beklesin diye.
            ---EKLEMEDIM SADECE IF STATEMENT---
            */

            if (!outgoingMessage.isEmpty()) { // bu line assert ile değiştirilebilir

                ObjectOutputStream outToServer;
                try {
                    int randomSending = getRandomVariable();

                    Thread.sleep(sendObjectSleep + randomSending);

                    /*
                    Burda priorty..
                    First Consistent olayında son gönderdiğim mesajı burda anlarım
                    Write object kısmına geçtiyse göndermiştir gibi.
                     */




                    outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
                    System.out.println("Topic is: "+outgoingMessage.peek().getTopic()+
                            " Message is: "+outgoingMessage.peek().getMessage()+" Priority is: "+
                    priority.priorityAssigner(outgoingMessage.peek().getMessage()));
                    System.out.println();
                    long rttTimeStart = System.nanoTime();
                    outToServer.writeObject(outgoingMessage.poll());
                    long rttTimeEnd = System.nanoTime();

                    DecimalFormat dfrtt = new DecimalFormat("#.###");
                    double rttTime = rttTimeEnd-rttTimeStart;
                    System.out.println("rttStart "+rttTimeStart+" | rttend"+rttTimeEnd+" | aradaki fark"+ rttTime + " Timer: "+dfrtt.format(rttTime));

                    double sampleRtt = rttTime / 1000000;   // convert nanosecond to milliseconds

                    double sampleRttDeneme = (int)(sampleRtt*1000);
                    double estimatedRtt = (int)(calculateEstimatedRtt(sampleRtt)*1000);

                    System.out.println("sampleRtt: "+sampleRttDeneme + "estimatedRtt " +estimatedRtt );



                    counter.increment();
                    publishersTimer();
                    float son = System.nanoTime();
                    DecimalFormat df = new DecimalFormat("#.###");
                    double time = son - start1;
                    double time1 = time % 1000000;
                    time = (time - time1) / 1000000;
                    System.out.println("Counter: " + counter.getCounter() + " Timer: " + df.format(time));

                } catch (IOException ex) {            // buraya yada+++++++
                    System.out.println("Server connection closed!");
                    printArr();
                    System.exit(1);
                    break;
                } catch (InterruptedException ex) {        // buraya eklenecek
                    Logger.getLogger(createObjects.class.getName()).log(Level.SEVERE, null, ex);
                    printArr();
                }
            }
        }
        try {

            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(sendObjects.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getRandomVariable() {
        int randomSending = random.nextInt(100);

        if (randomSending < 5) {
            randomSending = random.nextInt(7);
        } else if (randomSending < 10) {
            randomSending = randomSending * 5;
        } else if (randomSending < 20) {
            randomSending = randomSending * 7;
        } else if (randomSending < 30) {
            randomSending = random.nextInt(3);
        } else if (randomSending < 40) {
            randomSending = randomSending * 4;
        } else if (randomSending < 50) {
            randomSending = random.nextInt(3);
        } else if (randomSending < 60) {
            randomSending = randomSending * 5;
        } else if (randomSending < 70) {
            randomSending = random.nextInt(7);
        } else if (randomSending < 80) {
            randomSending = randomSending * 9;
        } else if (randomSending < 93) {
            //19k oalbilrş

            randomSending = 0;

        }

        return randomSending;

    }

    public double calculateEstimatedRtt(double sampleRtt){
        if(estimatedRtt == 0)  // initializing, first input.
        {
            estimatedRtt = sampleRtt;
            return estimatedRtt;
        }
        else
        {
            double a = 0.125;
            estimatedRtt = (1-a) * estimatedRtt + a*sampleRtt;
            return estimatedRtt;
        }
    }


    public void printArr() {
        int i;
        int counterNumber = 0;
        for (i = 0; i < arr.length; i++) {
            counterNumber = counterNumber + 50;
            System.out.println("Counter number: " + counterNumber + " time: " + arr[i]);
        }

    }

}
