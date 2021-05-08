
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
    private double priority ;

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
        this.delayed = delayed;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }
}