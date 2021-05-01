package side;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class WelcomeMessages implements Runnable {

    /*
    Bu class gelen objectleri karşılayan executor thread havuzunun classı
    Objectler burada karşılandıktan sonra queueya alınıyor.
     */
    private Socket client = null;
    private LinkedBlockingQueue<Message> incomingMessage;
    /*
     şimdi Qmin ve Qmaxı her new connection established'ta göndermem lazım.
     sadece 1 kere gönderilecek.
     */
    int dropped;


    public WelcomeMessages(Socket clientSocket, LinkedBlockingQueue<Message> incomingMessage) {
        this.incomingMessage = incomingMessage;
        this.client = clientSocket;

    }

    @Override
    public void run() {

        while (true) {

            try {

                InputStream inputStream = client.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

                 /**
                ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
                oos.writeObject("Hi Client, BU KUYRUK YOUĞUNLUĞUDUR " + incomingMessage.size());
                */

                /*
                Queueya objectlerin eklendiği kısım
                 */

                try {
                    incomingMessage.add(((Message) objectInputStream.readObject()));
                } catch (IllegalStateException | ClassNotFoundException e) {
                    dropped++;
                    //System.out.println("dropped count: "+ dropped );
                }

            } catch (IOException ex) {

                try {
                    /*
                    Bağlantı kopması durumunda connection kapatılıyor
                     */
                    client.close();
                    System.out.println("Connection Reset " + client.getPort());
                    break;

                } catch (IOException ex1) {
                    Logger.getLogger(WelcomeMessages.class.getName()).log(Level.SEVERE, null, ex1);
                }

            }
        }

    }

}
