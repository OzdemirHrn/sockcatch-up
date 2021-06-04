package side;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MultipleClients {

    public static Counter counter = new Counter();
    public static HashMap<Integer, ArrayList<Double>> delayedMessagesPriority = new HashMap<>();

    static {
        for (int i = 0; i < 5; i++) {
            delayedMessagesPriority.put(i, new ArrayList<>());
        }
    }


    public static void main(String... args) throws IOException {


        for (int i = 2; i < RunConfig.returnNumberOfSensors() + 2; i++) {
            new Thread(new RunSensorsSeparately(RunConfig.readConfig(i))).start();
        }
//
//        Runnable receivingQueueOcc = new QueueOccupancyReceiver(clientSocket);
//        Thread threadReceivingQueueOcc = new Thread(receivingQueueOcc);
//        threadReceivingQueueOcc.start();

    }
}

class RunSensorsSeparately implements Runnable {

    private final List<String> sensorConfig;

    public RunSensorsSeparately(List<String> sensorConfig) {
        this.sensorConfig = sensorConfig;
    }

    @Override
    public void run() {
        try {

            new ClientSide().main(sensorConfig);
        } catch (Exception ex) {
            Logger.getLogger(RunSensorsSeparately.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
