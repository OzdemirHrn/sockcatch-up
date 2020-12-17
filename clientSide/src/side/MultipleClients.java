package side;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author harunOzdemir
 */

public class MultipleClients {
   
     public static void main(String... args){
         /*
         Burada bazı sıkıntılar var.
         Eş zamanlı farklı parametrelerle çalıştıramıyorum.
         Mesajlar birbirine giriyor ve random değer hep aynı oluyor.
         */
         Runnable bathroomB=new Irun("home/brightness/bathroom4");
         Thread threadBathroomB=new Thread(bathroomB);
         threadBathroomB.start();
         
         /*Runnable bathroomT=new Irun("home/temperature/bathroom");
         Thread threadBathroomT=new Thread(bathroomT);
         threadBathroomT.start();*/

    
}
     }

class Irun implements Runnable{
    private String topic;
    public Irun(String topic){
        this.topic=topic;
    }

    @Override
    public void run() {
        try {
            ClientSide.main(topic);
        } catch (Exception ex) {
            Logger.getLogger(Irun.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}

