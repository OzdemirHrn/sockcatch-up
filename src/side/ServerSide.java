package side;

import java.io.*; 
import java.net.*; 
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

class ServerSide { 
    
private static ExecutorService service = Executors.newFixedThreadPool(3);
private static LinkedBlockingQueue<Message> comingMessages = new LinkedBlockingQueue<Message>();//I think for thread safe--blocking queue--

  public static void main(String argv[]) throws Exception 
    { 
      
      
        
      ServerSocket welcomeSocket = new ServerSocket(6789); //create welcoming socket at port 6789
      Runnable printObjects=new PrintQueue(comingMessages);
      Thread threadPrintingObjects = new Thread(printObjects);
      threadPrintingObjects.start();
  
      while(true) { 
           //ServerSocket.accept() is blocking method and blocks until a socket connection made. 
           System.out.println("Server is waiting for client connection...");
           Socket connectionSocket = welcomeSocket.accept(); 
           System.out.println("Client connected!  "+connectionSocket.getPort());
           Task task=new Task(connectionSocket,comingMessages);

           service.submit(task);
              

          

        } 
    } 
} 
class PrintQueue implements Runnable{
    private LinkedBlockingQueue<Message> incomingMessage;
    
    public PrintQueue(LinkedBlockingQueue<Message> incomingMessage){
        this.incomingMessage=incomingMessage;
        
    }
    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(PrintQueue.class.getName()).log(Level.SEVERE, null, ex);
            }
           //blocking olması lazım.. queue boşsa beklesin...wait fonk koymadım if ile yaptım şimdilik!!
            if(!incomingMessage.isEmpty()){ 
                 System.out.println(incomingMessage.peek().getMessage()+"  "+incomingMessage.peek().getTopic() +"  Queue size is "+ incomingMessage.size());
                 incomingMessage.poll();
              
                
            }
        }
        
        
    }


}

final class Task implements Runnable{
    private Socket client = null;
    private int clientMessage; 
    private LinkedBlockingQueue<Message> incomingMessage;
    public Task(Socket clientSocket,LinkedBlockingQueue<Message> incomingMessage) {
        this.incomingMessage=incomingMessage;
        this.client = clientSocket;
        
    }
  
    @Override
    public void run() {
        //System.out.println("Task ID : " + this.client +" performed by " + Thread.currentThread().getName());
        
       
       while(true){
           
        try {
            InputStream inputStream = client.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
           /* Message obj1=(Message)objectInputStream.readObject();
            incomingMessage.add(obj1);*/
            incomingMessage.add(((Message) objectInputStream.readObject()));
            

            //System.out.println("Socket ID : " + this.client +" performed by " + Thread.currentThread().getName()+" message is: "+clientMessage);
        } catch (IOException ex) {
            
            try {
                client.close();
                System.out.println("Connection Reset "+ client.getPort());
                break;
            //Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex1) {
                Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }  catch (ClassNotFoundException ex) {
               Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
           } 
        
       }
        
        
    }
  
}

