package side;

import java.io.*;
import java.net.*;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
/**
 *
 * @author harunOzdemir
 */

class Counter {
    private AtomicInteger c = new AtomicInteger(0);

    public int increment() {
       c.getAndIncrement();
           return c.get();
    }
         public int getCounter() {
           return c.get();
    }


 }
class Once {
private final AtomicBoolean done = new AtomicBoolean();

public void run(Runnable task) {
  if (done.get()) return;
  if (done.compareAndSet(false, true)) {
      task.run();
  }
}

}

 class ClientSide {

	private static int topic2;
    /*
    Gidecek mesajlarÄ±n beklediÄŸi Queue
    Thread Safe iÃ§in Blocking Queue kullandÄ±m. Ama tekrar bakÄ±labilir -----
    */
    private static LinkedBlockingQueue<Message> goingMessages = new LinkedBlockingQueue<Message>();

    public static void main(String... topic) throws Exception
    {
        /*
        Client side main methodundan  argument alÄ±yor.
        Bu argument topic olarak gÃ¶rev yapÄ±yor.
        Bu client sadece bu topice message yolluyor
        */

        Socket clientSocket = new Socket("127.0.0.1", 6789);

        /*
        Message sÄ±nÄ±fÄ±ndan topic ve random value argumentleriyle
        objectler oluÅŸturan Thread.
        */
        Runnable creatingObject=new createObjects(goingMessages,topic[0]);
        Thread threadCreatingObject = new Thread(creatingObject);
        threadCreatingObject.start();

        /*
        Bu objectleri clientSocket'e gÃ¶nderen Thread.
        */
        Runnable sendingObjects=new sendObjects(goingMessages,clientSocket);
        Thread threadSendingObjects = new Thread(sendingObjects);
        threadSendingObjects.start();

    }


static int kalem2(int sifre){

	if(sifre ==9876565)
		return topic2;
	else
		return 000;
}


}

class sendObjects implements Runnable{
	final static Counter counter = new Counter();

    final static Once once = new Once();
     static float start=System.nanoTime();
     static float end;
     static float arr []=new float[9];

     static void publishersTimer(){

         if(counter.getCounter()>=9995 && counter.getCounter()<=10005 ){
             end=System.nanoTime()-start;
             arr[0]=(float) (end/1e6);

         }
         else if(counter.getCounter()>=24995 && counter.getCounter()<=25005){
             end=System.nanoTime()-start;
             arr[1]=(float) (end/1e6);
         }
         else if(counter.getCounter()>=49995 && counter.getCounter()<=50005){
             end=System.nanoTime()-start;
             arr[2]=(float) (end/1e6);
         }
         else if(counter.getCounter()>=74995 && counter.getCounter()<=75005){
             end=System.nanoTime()-start;
             arr[3]=(float) (end/1e6);
         }
         else if(counter.getCounter()>=99995 && counter.getCounter()<=100005){
             end=System.nanoTime()-start;
             arr[4]=(float) (end/1e6);
         }
         else if(counter.getCounter()>=199990 && counter.getCounter()<=200005){
             end=System.nanoTime()-start;
             arr[5]=(float) (end/1e6);
             //System.out.println(counter.getCounter());
         }
       /*  else if(counter.getCounter()>=499995 && counter.getCounter()<=500005){
             end=System.nanoTime()-start;
             arr[6]=(float) (end/1e6);


         }*/
         else if(counter.getCounter()>=200000){
           System.out.println(counter.getCounter());


         }
     }



    /*
    Objectleri Server'a gÃ¶nderen Thread ve onun Runnable classÄ±
    */
    private LinkedBlockingQueue<Message> outgoingMessage;
    private  Socket clientSocket;

    public sendObjects(LinkedBlockingQueue<Message> outgoingMessage, Socket clientSocket){
        this.outgoingMessage=outgoingMessage;
        this.clientSocket=clientSocket;
    }

    @Override
    public void run() {


    	final float start1=System.nanoTime();

        while(true){
           /*
            Buraya wait Thread methodu ekleyecektim. Loop sÃ¼rekli dÃ¶nmesin
            Queue boÅŸ olduÄŸunda beklesin diye.
            ---EKLEMEDIM SADECE IF STATEMENT---
            */



            if(!outgoingMessage.isEmpty()){

                ObjectOutputStream outToServer;
                try {
                    Thread.sleep(1000);
                    outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
                    System.out.println(outgoingMessage.peek().getMessage()+"  "+outgoingMessage.peek().getTopic()+"  Queue size is "+outgoingMessage.size());



                    /*
                    Thread sleep parametresine gÃ¶re belli aralÄ±klarla queuedaki
                    head elementi servera yollamaya Ã§alÄ±ÅŸÄ±yor.
                    */
                    outToServer.writeObject(outgoingMessage.poll());
                    counter.increment();
                	publishersTimer();
                	float son = System.nanoTime();
                	System.out.println("Counter: "+ counter.getCounter() + " Timer: "+(son-start1));




                    /*
                    poll-> return and remove yapÄ±yor.
                    */

                } catch (IOException ex) {
                    Logger.getLogger(sendObjects.class.getName()).log(Level.SEVERE, null, ex);
                    break;
                } catch (InterruptedException ex) {
                    Logger.getLogger(createObjects.class.getName()).log(Level.SEVERE, null, ex);
            }

            }
        }
        try {

            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(sendObjects.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}


class createObjects implements Runnable{
    /*
    Objectleri Ã¼reten Thread'in Runnable classÄ±
    */
    private LinkedBlockingQueue<Message> outgoingMessage;
    private int message;
    private String topic;
    private static final Random random = new Random();

 //   int harunaoldumu= ClientSide

    public createObjects(LinkedBlockingQueue<Message> outgoingMessage,String topic){
        this.outgoingMessage=outgoingMessage;
        this.topic=topic;
    }

    @Override
    public void run() {

        while(true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(createObjects.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*
            Belli aralÄ±klarla yeni objectler oluÅŸturup bunlarÄ± Queue'ya atÄ±yorum.
            MQTT'deki publisher client gÃ¶revi.
            */
            message = createRandomNumberBetween(1,100);
            outgoingMessage.add(new Message(topic,message));
        }

    }

    private int createRandomNumberBetween(int min, int max) {
        /*
        1-100 arasÄ± Integer deÄŸer Ã¼reten fonksiyon.
        */
        return random.nextInt(max - min + 1) + min;
    }

}
