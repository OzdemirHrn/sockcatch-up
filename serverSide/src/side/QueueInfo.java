package side;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueInfo implements Runnable {

    private final Socket sensor;
    private LinkedBlockingQueue<Message> incomingMessage;
    OutputStream outToSensor;
    byte[] toSensor;


    public QueueInfo(Socket toSensor, LinkedBlockingQueue<Message> incomingMessage) {
        this.sensor = toSensor;
        this.incomingMessage = incomingMessage;

    }


    @Override
    public void run() {

        while (true) {
            /*
                Burada gereksiz loopa sokmamam lazım
                wait-notify is a solution
             */
            try {
                sendToClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void sendToClient() throws IOException {
        //Sensore göndermesi lazım queue bilgisi
        toSensor = ByteBuffer.allocate(4).putInt(incomingMessage.size()).array();
        System.out.println("Queue Occupancy information is sent");
        outToSensor = sensor.getOutputStream();
        outToSensor.write(toSensor);
        sensor.close();
    }
}
