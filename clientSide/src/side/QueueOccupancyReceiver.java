package side;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class QueueOccupancyReceiver implements Runnable {

    private Socket fromServer;
    InputStream inFromServer;
    private byte[] reply = new byte[1024];
    private String dataString = "";
    static double queueOccupancy;

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
        System.out.println("Queue Yoğunluğunu aldım --> " +str);
        queueOccupancy = Double.parseDouble(str);

        dataString = "";


    }
}
