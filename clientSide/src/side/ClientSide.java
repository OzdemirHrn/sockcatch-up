package side;


import java.io.*; 

import java.net.*; 
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

class ClientSide { 
    
    
    private static LinkedBlockingQueue<Message> goingMessages = new LinkedBlockingQueue<Message>();//I think for thread safe--blocking queue--

    
    public static void main(String... topic) throws Exception 
    { 
        //String topic="home/brightness1/livingroom";
        
        Socket clientSocket = new Socket("127.0.0.1", 6789); // create socket
        
        Runnable sendingObjects=new sendObjects(goingMessages,clientSocket);
        Thread threadSendingObjects = new Thread(sendingObjects);
        threadSendingObjects.start();
        
        Runnable creatingObject=new createObjects(goingMessages,topic[0]);
        Thread threadCreatingObject = new Thread(creatingObject);
        threadCreatingObject.start();
         
    } 

} 

class sendObjects implements Runnable{
    private LinkedBlockingQueue<Message> outgoingMessage;
    private  Socket clientSocket;
    
    public sendObjects(LinkedBlockingQueue<Message> outgoingMessage, Socket clientSocket){
        this.outgoingMessage=outgoingMessage;
        this.clientSocket=clientSocket;
    }

    @Override
    public void run() {
     ////////////////one thread-->>queue'dan alıp servera yolluyor
        while(true){
           //blocking olması lazım.. queue boşsa beklesin...wait fonk koymadım if ile yaptım şimdilik!!
            if(!outgoingMessage.isEmpty()){ 
                
                ObjectOutputStream outToServer;
                try {
                    Thread.sleep(1000);
                    outToServer = new ObjectOutputStream(clientSocket.getOutputStream()); //create output stream attached to socket
                    System.out.println(outgoingMessage.peek().getMessage()+"  "+outgoingMessage.peek().getTopic()+"  Queue size is "+outgoingMessage.size());
                    
                    outToServer.writeObject(outgoingMessage.poll());  //send value to server and remove it from queue
                } catch (IOException ex) {    
                    Logger.getLogger(sendObjects.class.getName()).log(Level.SEVERE, null, ex);
                    break;
                } catch (InterruptedException ex) {
                    Logger.getLogger(createObjects.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            }
        }
        try {
            //unreachable
            clientSocket.close();  //close socket
        } catch (IOException ex) {
            Logger.getLogger(sendObjects.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}


class createObjects implements Runnable{
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
     
        //////////////another thread-->>queue'ya yeni objectler oluşturuyor
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(createObjects.class.getName()).log(Level.SEVERE, null, ex);
            }
            message = createRandomNumberBetween(1,100);
            outgoingMessage.add(new Message(topic,message));

       
       
           
        
        }
        
    }
    
    private int createRandomNumberBetween(int min, int max) {//create random number between given two numbers

        return random.nextInt(max - min + 1) + min;
    }

}
