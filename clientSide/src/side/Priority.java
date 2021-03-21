package side;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


public class Priority {

    private static Queue<Double> last10 = new LinkedList<>();
    private static double tempAnnealing = 0.1;
    private static double firstConsistentData;
    private static double priority = 1;

    private static void priorityAssigner(Double data) {

        System.out.println("Difference is: % " + calculateDifference(data, firstConsistentData));
        if (calculateDifference(data, firstConsistentData) >= 1) {
            firstConsistentData = data;
            priority = 1;
            tempAnnealing = 0.1;
        } else {
            priority = Math.exp((-tempAnnealing) / calculateDifference(data, firstConsistentData));
            // 2 kat değil de 1.5 falan artar belki
            tempAnnealing *= 1.5;
        }

        System.out.println("Priority is " + priority);

    }

    private static double calculateDifference(double current, double prev) {
        double percentageDiff;
        percentageDiff = (Math.abs(current - prev) / ((current + prev) / 2)) * 100;
        return Double.parseDouble(new DecimalFormat("##.###").format(percentageDiff).replace(',', '.'));
    }

    private static void last10Data(Double data) {
        // belki burdan mean falan return ederim.
        // priority hesaplarken kullanmak için
        if (last10.size() < 10)
            last10.add(data);
        else {
            last10.poll();
            last10.add(data);
        }
        int counter = 1;
        double mean = 0;
        for (double f : last10) {
            mean += f;
            // System.out.println(counter + " " + f);
            counter++;
        }

       // System.out.println("Mean value of data: " + mean / (counter - 1));

    }

    public static void main(String... args) throws IOException {

        ArrayList<Double> temperature = readFromFile(30);
        int count = 0;
        for (Double f : temperature) {
           System.out.println("New Data has arrived! " + f);
            if (count == 0) {
                firstConsistentData = f;
            } else
                priorityAssigner(f);

            last10Data(f);
            count++;

        }


    }

    private static ArrayList<Double> readFromFile(int count) throws IOException {
        File file = new File("C:\\Users\\hrnoz\\IdeaProjects\\sockcatch-up\\clientSide\\src\\dataset1.txt");
        byte[] directlyToClient = new byte[(int) file.length()];
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream;
        bufferedInputStream = new BufferedInputStream(fileInputStream);
        bufferedInputStream.read(directlyToClient, 0, directlyToClient.length);
        String str = new String(directlyToClient);
        String[] strSplit;
        strSplit = str.split(" ");
        ArrayList<Double> temperatureSensor1 = new ArrayList<>();
        ArrayList<Double> co2Sensor1 = new ArrayList<>();
        String[] timeLine;

        while (count < strSplit.length) {
            temperatureSensor1.add(Double.parseDouble(strSplit[count]));
            co2Sensor1.add(Double.parseDouble(strSplit[count + 3]));
            count += 23;
        }

        return temperatureSensor1;

    }
}
