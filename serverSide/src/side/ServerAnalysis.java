package side;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Queue;

public class ServerAnalysis {

    private static float end;
    private int printSleepTime;


    // test variables
    static int incrementOfCounter = 50;                 // tutulma aralığını buraya yaz, ne sıklıkla tutulacak
    static int totalCounter = incrementOfCounter;
    static int limitOfCounter = 500;               // nereye kadar gidecek buraya onu yaz 30.000 e gidecekse bunu buraya yaz
    static int SizeOfArray = (int) (limitOfCounter / incrementOfCounter) + 1;
    static int arrayIndexNumber = 0;
    static float[] arr = new float[SizeOfArray];
    static int[] QueueSize = new int[SizeOfArray];
    static int[] droppedSize = new int[SizeOfArray];


    FileWriter fileWriterCounter = new FileOperationServer().createInputfile("counter");
    FileWriter fileWriterTime = new FileOperationServer().createInputfile("time");
    FileWriter fileWriterQueueSize = new FileOperationServer().createInputfile("queueSize");
    FileWriter fileWriterDroppedCount = new FileOperationServer().createInputfile("droppedCount");


    void publishersTimer(int incomingMessageSize, Counter counter, float start, Queue<WelcomeMessages> allClients ) {

        if ((counter.getCounter() == totalCounter) && (counter.getCounter() <= limitOfCounter)) {
            end = System.nanoTime() - start;
            arr[arrayIndexNumber] = (float) (end / 1e6);
            QueueSize[arrayIndexNumber] = incomingMessageSize;
            arrayIndexNumber++;
            totalCounter = totalCounter + incrementOfCounter;
            int totalDropped = 0;

                                                                        // burası total drop bakılacak
            for (WelcomeMessages eachClient : allClients) {
                // System.out.println("Dropped Messages" + eachClient.dropped);
                totalDropped += eachClient.dropped;
            }

            droppedSize[arrayIndexNumber] = totalDropped;

            //System.out.println("Total Number of Dropped Messages: " + totalDropped);
        }

    }

    public void printArr() throws IOException {
        int i;
        int counterNumber = 0;
        System.out.println("counterNumber********************************************************************************");
        for (i = 0; i < arr.length; i++) {
            counterNumber = counterNumber + incrementOfCounter;
            System.out.println(counterNumber);
            fileWriterCounter.write(counterNumber+"\n");
        }
        fileWriterCounter.close();

        counterNumber = 0;
        System.out.println("time:********************************************************************************");
        for (i = 0; i < arr.length; i++) {
            counterNumber = counterNumber + incrementOfCounter;
            System.out.println(arr[i]);
            fileWriterTime.write(arr[i]+"\n");
        }
        fileWriterTime.close();

        counterNumber = 0;
        System.out.println("Queue Size :********************************************************************************");
        for (i = 0; i < arr.length; i++) {
            counterNumber = counterNumber + incrementOfCounter;
            System.out.println(QueueSize[i]);
            fileWriterQueueSize.write(QueueSize[i]+"\n");
        }
        fileWriterQueueSize.close();

        counterNumber = 0;
        System.out.println("Dropped Count :********************************************************************************");
        for (i = 0; i < arr.length; i++) {
            counterNumber = counterNumber + incrementOfCounter;
            System.out.println(droppedSize[i]);
            fileWriterDroppedCount.write(droppedSize[i]+"\n");
        }
        fileWriterDroppedCount.close();
        }


}


