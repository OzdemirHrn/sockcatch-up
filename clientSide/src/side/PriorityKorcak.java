package side;

import java.text.DecimalFormat;

public class PriorityKorcak implements IPriority{

    private double firstConsistentData = 0;
    private double priority = 1;
    private int counter=1;

    public double priorityAssigner(float data) {
        /*double random = Math.random();

        System.out.print("Delta= "+Double.parseDouble(df.format(random).replaceAll(",","."))+"  ");*/
        DecimalFormat df = new DecimalFormat("#.00");
        if(calculateDifference(data,firstConsistentData)>=1){
            priority=1;
            firstConsistentData = data;
            //System.out.println("Priority= "+Double.parseDouble(df.format(priority).replaceAll(",","."))+" Counter= "+counter);
            counter = 1;
        }
        else{
            priority = 1 / (10 * (1 - calculateDifference(data,firstConsistentData)) - counter);

            if (priority >= 1 || priority < 0) {
                priority=1;
                firstConsistentData = data;
                //System.out.println("Priority= "+Double.parseDouble(df.format(priority).replaceAll(",","."))+" Counter= "+counter);
                counter = 0;
            }
            counter++;
        }



        //System.out.println("Priority= "+Double.parseDouble(df.format(priority).replaceAll(",","."))+" Counter= "+counter);



        return Double.parseDouble(df.format(priority).replaceAll(",","."));


    }


    public double calculateDifference(double current, double prev) {
        double percentageDiff;
        percentageDiff = (Math.abs(current - prev) / ((current + prev) / 2)) * 100;
        return Double.parseDouble(new DecimalFormat("##.###").format(percentageDiff).replace(',', '.'));
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


}
