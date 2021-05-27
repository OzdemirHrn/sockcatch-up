package side;

import java.util.concurrent.atomic.AtomicInteger;


public class Counter {

    private AtomicInteger c = new AtomicInteger(0);
    private AtomicInteger droppedMessagesAfterSeveralTrialAttempts = new AtomicInteger(0);
    private AtomicInteger sendMessageInFirstAttempt = new AtomicInteger(0);
    private AtomicInteger sendMessageInSecondAttempt = new AtomicInteger(0);
    private AtomicInteger sendMessageInThirdAttempt = new AtomicInteger(0);
    private AtomicInteger sendMessageInFourthAttempt = new AtomicInteger(0);
    private AtomicInteger sizeOfDelayedQueue = new AtomicInteger(0);
    private AtomicInteger justOnceHistogram = new AtomicInteger(0);


    private int getCounter;


    public int incrementTotalMessageCounter() {
        getCounter = c.getAndIncrement();
        return getCounter;
    }

    public int getCounter() {
        return c.get();
    }

    public boolean isReachedToLimit(int limitMessage) {
        return getCounter > limitMessage;
    }

    public void incrementDroppedMessagesAfterSeveralAttempts() {
        droppedMessagesAfterSeveralTrialAttempts.incrementAndGet();
    }

    public int getDroppedMessagesAfterSeveralTrialAttempt() {
        return droppedMessagesAfterSeveralTrialAttempts.get();
    }

    public void incrementSendMessageInFirstAttempt() {
        sendMessageInFirstAttempt.incrementAndGet();
    }

    public int getSendMessageInFirstAttempt() {
        return sendMessageInFirstAttempt.get();
    }

    public void incrementSendMessageInSecondAttempt() {
        sendMessageInSecondAttempt.incrementAndGet();
    }

    public int getSendMessageInSecondAttempt() {
        return sendMessageInSecondAttempt.get();
    }

    public void incrementSendMessageInThirdAttempt() {
        sendMessageInThirdAttempt.incrementAndGet();
    }

    public int getSendMessageInThirdAttempt() {
        return sendMessageInThirdAttempt.get();
    }

    public void incrementSendMessageInFourthAttempt() {
        sendMessageInFourthAttempt.incrementAndGet();
    }

    public int getSendMessageInFourthAttempt() {
        return sendMessageInFourthAttempt.get();
    }

    public void incrementsizeOfDelayedQueue() {
        sizeOfDelayedQueue.incrementAndGet();
    }

    public void decrementtsizeOfDelayedQueue() {
        sizeOfDelayedQueue.decrementAndGet();
    }

    public int getsizeOfDelayedQueue() {
        return sizeOfDelayedQueue.get();
    }

    public int getAndIncrementHist() {
        return justOnceHistogram.getAndIncrement();
    }

}