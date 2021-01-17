
package side;

import java.io.Serializable;

/**
 *
 * @author hrnoz
 */
class Message implements Serializable 
{

   private String topic;
   private int message;

   public Message(String topic,int message)
   {
     this.topic=topic;
     this.message=message;
     
     
   }
   
    public String getTopic() {
        return topic;
    }

    public int getMessage() {
        return message;
    }

 
}
