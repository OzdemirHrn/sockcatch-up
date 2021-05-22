package side;

import java.io.FileWriter;
import java.io.IOException;

public class ClientAnalysis {
    static float start = System.nanoTime();
    static float end;
    static float[] arr = new float[20];


    void publishersTimer(Counter counter) {

        int interval = 50;
        for (int i = 0; i < 20; i++) {
            if (counter.getCounter() == interval) {
                end = System.nanoTime() - start;
                arr[i] = (float) (end / 1e6);
                break;
            }
            interval += 50;
        }
    }

    void printArr(FileWriter fileWriter) throws IOException {
        int i;
        int counterNumber = 0;
        for (i = 0; i < arr.length; i++) {
            counterNumber = counterNumber + 50;
            String info = "Counter number: " + counterNumber + " time: " + arr[i];
            fileWriter.write(info+"\n");
            System.out.println(info);
        }

    }
}
