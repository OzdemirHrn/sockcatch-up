package side;

import java.text.DecimalFormat;

public class PriorityKorcak implements IPriority {

    private double firstConsistentData = 0;
    private double priority = 1;
    private int counter = 1;

    public double priorityAssigner(float data) {
        DecimalFormat df = new DecimalFormat("#.00");
        double percentageDifference = calculateDifference(data, firstConsistentData);
        if (percentageDifference >= 1) {
            priority = 1;
            firstConsistentData = data;
            //System.out.println("Priority= "+Double.parseDouble(df.format(priority).replaceAll(",","."))+" Counter= "+counter);
            counter = 1;
        } else {
            priority = 1 / (10 * (1 - percentageDifference) - counter);

            System.out.println("priority=" + priority + " counter= " + counter);

            if (priority >= 1 || priority < 0) {
                priority = 1;
                firstConsistentData = data;
                //System.out.println("Priority= "+Double.parseDouble(df.format(priority).replaceAll(",","."))+" Counter= "+counter);
                counter = 0;
            }

            counter++;
        }


        //System.out.println("Priority= "+Double.parseDouble(df.format(priority).replaceAll(",","."))+" Counter= "+counter);


        return Double.parseDouble(df.format(priority).replaceAll(",", "."));


    }


    public double calculateDifference(double current, double prev) {
        double percentageDiff;
        percentageDiff = (Math.abs(current - prev) / ((current + prev) / 2)) * 100;
        if (current + prev == 0) return Integer.MAX_VALUE;
        if (percentageDiff == 0) return Integer.MAX_VALUE;
        double retDiff = Double.parseDouble(new DecimalFormat("##.###").format(percentageDiff).replace(',', '.'));
        if (retDiff < 0.001) return 0;
        System.out.println("Difference= "+ retDiff);
        return retDiff;
    }


    /*public void main() {
        DecimalFormat df = new DecimalFormat("#.00");

        int counter = 1;
        while (true) {
            double priority = priorityAssigner(counter);
            if (priority >= 1 || priority < 0) {
                priority=1;
                System.out.println("Priority= "+Double.parseDouble(df.format(priority).replaceAll(",","."))+" Counter= "+counter);
                counter = 1;

                continue;
            }
            System.out.println("Priority= "+Double.parseDouble(df.format(priority).replaceAll(",","."))+" Counter= "+counter);
            counter++;


        }


    }*/

    public void setFirstConsistentData(double firstConsistentData) {
        this.firstConsistentData = firstConsistentData;
    }
}
