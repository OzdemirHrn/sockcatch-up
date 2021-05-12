package side;


import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

class DelayObject implements Delayed {

    private static double[][] waitingTimeArray = new double[1][2];
    Message message;
    private long time;

    public DelayObject(Message message, double delayTime) {
        this.message = message;
        this.time = System.currentTimeMillis()
                + (long) delayTime;
    }


    public long getDelay(TimeUnit unit) {
        long diff = time - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    public long getTime(TimeUnit unit) {
        return time;
    }

    public int compareTo(Delayed obj) {
        if (this.time < ((DelayObject) obj).time) {
            return -1;
        }
        if (this.time > ((DelayObject) obj).time) {
            return 1;
        }
        return 0;
    }


    public static double takeWaitingTime(double size, double RTT, double priority, double award, double queueOccupancy, double queue2) {
        double waitingTime;
        double x = (1 - queue2) * (priority * award - size - RTT) + queue2 * (-size - RTT);
        double y = (1 - queueOccupancy) * (priority * award - size) + queueOccupancy * ((-award) * (1 - priority) - size);

        if (x < 0 && y < 0) {
            if (Math.abs(x) > Math.abs(y)) {
                waitingTime = x * award * 10000;
            } else {
                waitingTime = y * award * 10000;
            }
        } else {
            waitingTime = Math.min(x, y) * award * 10000;
        }
        return Math.abs(waitingTime);
    }
}
