package side;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ServerAnalysis {

    private static float end;
    private int printSleepTime;


    // test variables
    static int incrementOfCounter = 50;                 // tutulma aralığını buraya yaz, ne sıklıkla tutulacak
    static int totalCounter = incrementOfCounter;
    static int limitOfCounter = 1000;               // nereye kadar gidecek buraya onu yaz 30.000 e gidecekse bunu buraya yaz
    static int SizeOfArray = (int) (limitOfCounter / incrementOfCounter) + 1;
    static int arrayIndexNumber = 0;
    static float[] arr = new float[SizeOfArray];
    static int[] QueueSize = new int[SizeOfArray];
    static int[] droppedSize = new int[SizeOfArray];


    void publishersTimer(int incomingMessageSize, Counter counter,float start ) {

        if ((counter.getCounter() == totalCounter) && (counter.getCounter() <= limitOfCounter)) {
            end = System.nanoTime() - start;
            arr[arrayIndexNumber] = (float) (end / 1e6);
            QueueSize[arrayIndexNumber] = incomingMessageSize;
            arrayIndexNumber++;
            totalCounter = totalCounter + incrementOfCounter;
            int totalDropped = 0;

           /*                                                              // burası total drop bakılacak
            for (WelcomeMessages eachClient : allClients) {

                // System.out.println("Dropped Messages" + eachClient.dropped);
                totalDropped += eachClient.dropped;

            }

            */
            droppedSize[arrayIndexNumber] = totalDropped;

            //System.out.println("Total Number of Dropped Messages: " + totalDropped);
        }

    }

    public void printArr() throws IOException {
        int i;
        int counterNumber = 0;
        String inputFileCounter = "";
        String inputFileTime = "";
        String inputFileQueueSize = "";
        String inputFileDroppedSize = "";


        String path = "C:\\Users\\malik türkoğlu\\Desktop\\";
        String inputCounter = path + "inputCounter" + ".txt";
        File fileCounter = null;
        FileWriter filewriterCounter = null;
        fileCounter = new File(inputCounter);
        filewriterCounter = new FileWriter(fileCounter);


        for (i = 0; i < arr.length; i++) {
            counterNumber = counterNumber + incrementOfCounter;
            System.out.println("Counter number: " + counterNumber + " time: " + arr[i] + "  Queue size is " + QueueSize[i] + " Dropped count: " + droppedSize[i]);
            inputFileCounter += counterNumber + "\n";;
        }
        System.out.println(inputFileCounter);


        filewriterCounter.write(inputFileCounter);
        filewriterCounter.close();
    }



}
