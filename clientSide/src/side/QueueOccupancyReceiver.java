package side;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class QueueOccupancyReceiver implements Runnable {

    public static double queueOccupancy;
    private Socket fromServer;
    InputStream inFromServer;
    private byte[] reply = new byte[1024];
    private String dataString = "";
    private final int Qmin = ClientSide.Qmin, Qmax=ClientSide.Qmax = 80;

    public QueueOccupancyReceiver(Socket fromServer) throws IOException {
        this.fromServer = fromServer;
    }


    @Override
    public void run() {
        while (true) {
            try {
                // Request coming from Client
                inFromServer = fromServer.getInputStream();

                int bytesRead1;

            /*
            Read request from InputStream
             */
                while ((bytesRead1 = inFromServer.read(reply)) != -1) {

                    dataString += new String(reply, 0, bytesRead1);
                    queueOccupancy(dataString);

                }


            } catch (IOException ex) {

            }
        }
    }

    private void queueOccupancy(String str) {
        double rawQueueValue = Double.parseDouble(str);
        if (rawQueueValue < Qmin) queueOccupancy = 0;
        else if (rawQueueValue > Qmax) queueOccupancy = 1;
        else queueOccupancy = rawQueueValue / 100;
        System.out.println("Queue Yoğunluğunu aldım --> " + queueOccupancy);
        dataString = "";

    }
}
