package side;

import java.util.concurrent.atomic.AtomicInteger;


public class Counter {

    private AtomicInteger c = new AtomicInteger(0);

    public int increment() {
        c.getAndIncrement();
        return c.get();
    }

    public int getCounter() {
        return c.get();
    }
}
