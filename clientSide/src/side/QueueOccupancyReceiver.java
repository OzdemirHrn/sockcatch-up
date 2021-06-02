package side;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class QueueOccupancyReceiver implements Runnable {

    public static double queueOccupancy;
    private Socket fromServer;
    InputStream inFromServer;
    private byte[] reply = new byte[1024];
    private String dataString = "";
    private final int Qmin = ClientSide.Qmin, Qmax = ClientSide.Qmax = 80;
    private double rawQueueValue;

    public QueueOccupancyReceiver(Socket fromServer) throws IOException {
        this.fromServer = fromServer;
    }


    @Override
    public void run() {
        while (true) {
            try {
                // Request coming from Client



                inFromServer = fromServer.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inFromServer);


                rawQueueValue = (double) objectInputStream.readObject();
                queueOccupancy();



            } catch (IOException | ClassNotFoundException ex) {


            }
        }
    }

    private void queueOccupancy() {
        if (rawQueueValue < Qmin) queueOccupancy = 0;
        else if (rawQueueValue > Qmax) queueOccupancy = 1;
        else queueOccupancy = rawQueueValue / 100;
       // System.out.println("Queue Yoğunluğunu aldım --> " + rawQueueValue);
        dataString = "";

    }
}