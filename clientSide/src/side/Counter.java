package side;

import java.util.concurrent.atomic.AtomicInteger;


public class Counter {

    private AtomicInteger c = new AtomicInteger(0);
    private AtomicInteger droppedMessagesAfterSeveralTrialAttempts = new AtomicInteger(0);
    private int getCounter;

    public int incrementTotalMessageCounter() {
        getCounter = c.getAndIncrement();
        return getCounter;
    }

    public void incrementDroppedMessagesAfterSeveralTrialAttempts(){
        droppedMessagesAfterSeveralTrialAttempts.incrementAndGet();
    }

    public int getDroppedMessagesAfterSeveralTrialAttempt(){
        return droppedMessagesAfterSeveralTrialAttempts.get();
    }

    public int getCounter() {
        return c.get();
    }

    public boolean isReachedToLimit(int limitMessage){
        return getCounter > limitMessage;
    }
}
