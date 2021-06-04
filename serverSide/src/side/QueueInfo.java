package side;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueInfo implements Runnable {

    private final Socket sensor;
    private LinkedBlockingQueue<Message> incomingMessage;
    ObjectOutputStream outToSensor;
    byte[] toSensor=new byte[1024];


    public QueueInfo(Socket sensor, LinkedBlockingQueue<Message> incomingMessage) {
        this.sensor = sensor;
        this.incomingMessage = incomingMessage;

    }


    @Override
    public void run() {

        while (true) {
            /*
                Burada gereksiz loopa sokmamam lazım
                wait-notify is a solution
             */

            /*
                Şimdi burada göndereceğim queue size'ı zaten direkt queuedan reference
                alıp yollicam.
                Bir tane queue'nun priority gibi basit bir class yapsam


             */
            try {
                // Bu hiç güzel bir busy waitingten kaçış değil ama iş görür şimdilik
                // Şuan saniyede bir gönderiyor.
                Thread.sleep(50);
                sendToClient();
            } catch (IOException | InterruptedException e) {
                try {
                    sensor.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }

    }



    private void sendToClient() throws IOException {
        //Sensore göndermesi lazım queue bilgisi
        //toSensor = ByteBuffer.allocate(4).putInt(incomingMessage.size()).array();
        //System.out.println("Queue Occupancy sent-> "+incomingMessage.size());
        //outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
        outToSensor = new ObjectOutputStream(sensor.getOutputStream());
        //String message=""+incomingMessage.size();
        //toSensor=message.getBytes();
        outToSensor.writeObject((double) incomingMessage.size());
        //outToSensor.write(toSensor);
    }
}