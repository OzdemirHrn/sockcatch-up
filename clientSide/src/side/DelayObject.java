package side;



import java.util.concurrent.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
class DelayObject implements Delayed {

    Message message;
    private long time;

    public DelayObject(Message message, long delayTime)
    {
        this.message = message;
        this.time = System.currentTimeMillis()
                + delayTime;
    }



    public long getDelay(TimeUnit unit)
    {
        long diff = time - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }
    public long getTime(TimeUnit unit)
    {
        return time;
    }

    public int compareTo(Delayed obj)
    {
        if (this.time < ((DelayObject)obj).time) {
            return -1;
        }
        if (this.time > ((DelayObject)obj).time) {
            return 1;
        }
        return 0;
    }

    public void delayQueue(DelayQueue DQ,Message delayObject, long delayTime){
        DQ.add(new DelayObject(delayObject,delayTime));
    }
}
