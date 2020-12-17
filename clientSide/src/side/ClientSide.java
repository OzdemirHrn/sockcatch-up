package side;

import java.io.*; 
import java.net.*; 
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author harunOzdemir
 */

class ClientSide { 
    
    /*
    Gidecek mesajların beklediği Queue
    Thread Safe için Blocking Queue kullandım. Ama tekrar bakılabilir -----
    */
    private static LinkedBlockingQueue<Message> goingMessages = new LinkedBlockingQueue<Message>();

    
    public static void main(String... topic) throws Exception 
    { 
        /*
        Client side main methodundan  argument alıyor.
        Bu argument topic olarak görev yapıyor.
        Bu client sadece bu topice message yolluyor
        */
        
        Socket clientSocket = new Socket("127.0.0.1", 6789);
        
        /*
        Message sınıfından topic ve random value argumentleriyle
        objectler oluşturan Thread.
        */      
        Runnable creatingObject=new createObjects(goingMessages,topic[0]);
        Thread threadCreatingObject = new Thread(creatingObject);
        threadCreatingObject.start();
         
        /*
        Bu objectleri clientSocket'e gönderen Thread.
        */
        Runnable sendingObjects=new sendObjects(goingMessages,clientSocket);
        Thread threadSendingObjects = new Thread(sendingObjects);
        threadSendingObjects.start();
    } 

} 

class sendObjects implements Runnable{
    /*
    Objectleri Server'a gönderen Thread ve onun Runnable classı
    */
    private LinkedBlockingQueue<Message> outgoingMessage;
    private  Socket clientSocket;
    
    public sendObjects(LinkedBlockingQueue<Message> outgoingMessage, Socket clientSocket){
        this.outgoingMessage=outgoingMessage;
        this.clientSocket=clientSocket;
    }

    @Override
    public void run() {
    
        while(true){
           /*
            Buraya wait Thread methodu ekleyecektim. Loop sürekli dönmesin
            Queue boş olduğunda beklesin diye. 
            ---EKLEMEDIM SADECE IF STATEMENT---
            */
            if(!outgoingMessage.isEmpty()){ 
                
                ObjectOutputStream outToServer;
                try {
                    Thread.sleep(1000);
                    outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
                    System.out.println(outgoingMessage.peek().getMessage()+"  "+outgoingMessage.peek().getTopic()+"  Queue size is "+outgoingMessage.size());
                    /*
                    Thread sleep parametresine göre belli aralıklarla queuedaki 
                    head elementi servera yollamaya çalışıyor.
                    */
                    outToServer.writeObject(outgoingMessage.poll());
                    /*
                    poll-> return and remove yapıyor.
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
    Objectleri üreten Thread'in Runnable classı
    */
    private LinkedBlockingQueue<Message> outgoingMessage;
    private int message;
    private String topic;
    private static final Random random = new Random();   
    
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
            Belli aralıklarla yeni objectler oluşturup bunları Queue'ya atıyorum.
            MQTT'deki publisher client görevi.
            */
            message = createRandomNumberBetween(1,100);
            outgoingMessage.add(new Message(topic,message));
        }
        
    }
    
    private int createRandomNumberBetween(int min, int max) {
        /*
        1-100 arası Integer değer üreten fonksiyon.
        */
        return random.nextInt(max - min + 1) + min;
    }

}
