package side;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public class Priority {

    private static Queue<Double> last10 = new LinkedList<>();
    private double tempAnnealing = 0.1;
    private double firstConsistentData = 0;
    private double priority = 1;
    private float data;

    public Priority() {

        //this.data = data;// send object classında gönderilecek get(0).
    }

    double priorityAssigner(float data) {
       //System.out.println("Data is "+data);
        //System.out.println("Difference is: % " + calculateDifference(data, firstConsistentData));
        if (calculateDifference(data, firstConsistentData) >= 1) {
            firstConsistentData = data;
            priority = 1;
            tempAnnealing = 0.1;
        } else {
            priority = Math.exp((-tempAnnealing) / calculateDifference(data, firstConsistentData));
            // 2 kat değil de 1.5 falan artar belki
            tempAnnealing *= 1.5;
        }

        //System.out.println("Priority is " + priority+" fd "+firstConsistentData);
        return priority;

    }

    private double calculateDifference(double current, double prev) {
        double percentageDiff;
        percentageDiff = (Math.abs(current - prev) / ((current + prev) / 2)) * 100;
        return Double.parseDouble(new DecimalFormat("##.###").format(percentageDiff).replace(',', '.'));
    }


    private void last10Data(Double data) {
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


}
