
package side;

import java.io.Serializable;

/**
 * @author hrnoz
 */

class Message implements Serializable {
    private String topic;
    private float message;
    private int operation;
    private float size;
    private boolean delayed = false;
    private double priority;
    private double initialPriorityIfDelayed;
    private int counter = 0;
    private double rtt = 0;

    public double getInitialPriorityIfDelayed() {
        return initialPriorityIfDelayed;
    }

    public void setInitialPriorityIfDelayed(double initialPriorityIfDelayed) {
        this.initialPriorityIfDelayed = initialPriorityIfDelayed;
    }

    public double getRtt() {
        return rtt;
    }

    public void setRtt(double rtt) {
        this.rtt = rtt;
    }


    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public String getTopic() {
        return topic;
    }

    public float getMessage() {
        return message;
    }

    public int getOperation() {
        return operation;
    }

    public Message(String topic, float message, int operation, float size) {
        this.topic = topic;
        this.message = message;
        this.operation = operation;
        this.size = size;

    }

    public boolean isDelayed() {
        return delayed;
    }

    public void setDelayedTrue() {
        this.delayed = true;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}