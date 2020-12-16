
package side;

import java.io.Serializable;

/**
 *
 * @author hrnoz
 */
class Message implements Serializable 
{

    public String getTopic() {
        return topic;
    }

    public int getMessage() {
        return message;
    }
   private String topic;
   private int message;

   public Message(String topic,int message)
   {
     this.topic=topic;
     this.message=message;
     
     
   }

 
}
