package side;

import java.util.concurrent.atomic.AtomicInteger;


public class Counter {

    private AtomicInteger c = new AtomicInteger(0);
    private int getCounter;
    public int increment() {
        getCounter = c.getAndIncrement();
        return getCounter;
    }

    public int getCounter() {
        return c.get();
    }

    public boolean isReachedToLimit(int limitMessage){
        return getCounter > limitMessage;
    }
}
