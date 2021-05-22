package side;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Queue;


public class Priority implements IPriority {

    private double tempAnnealing = 0.1;
    private double firstConsistentData = 0;
    private double priority = 1;


    public Priority() {

        //this.data = data;// send object classında gönderilecek get(0).
    }


    @Override
    public double priorityAssigner(float data) {
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
        DecimalFormat df = new DecimalFormat("#.00");

        return Double.parseDouble(df.format(priority).replaceAll(",","."));

    }

    @Override
    public double calculateDifference(double current, double prev) {
        double percentageDiff;
        percentageDiff = (Math.abs(current - prev) / ((current + prev) / 2)) * 100;
        if(current+prev==0) return Integer.MAX_VALUE;
        if(percentageDiff==0) return Integer.MAX_VALUE;
        double retDiff = Double.parseDouble(new DecimalFormat("##.###").format(percentageDiff).replace(',', '.'));
        if(retDiff<0.001)return 0;
        return Double.parseDouble(new DecimalFormat("##.###").format(percentageDiff).replace(',', '.'));
    }



    /**
     * For Test
     *
     */
/*
    public static void main(String...args){
        Priority priority=new Priority();
        int k=0;

        while(k<100){
            double random = Math.random();
            DecimalFormat df = new DecimalFormat("#.00");
            System.out.print("Delta= "+Double.parseDouble(df.format(random).replaceAll(",","."))+"  ");
            System.out.println(" p="+priority.priorityTest(random)+" "+" t= "+Double.parseDouble(df.format(priority.tempAnnealing).replaceAll(",",".")));


            k++;
        }
    }

    double priorityTest(double delta) {
        //System.out.println("Data is "+data);
        //System.out.println("Difference is: % " + calculateDifference(data, firstConsistentData));
        if (delta >= 1) {
            firstConsistentData = delta;
            priority = 1;
            tempAnnealing = 0.1;
        } else {
            priority = Math.exp((-tempAnnealing) / (delta-(counter/10)));
            // 2 kat değil de 1.5 falan artar belki
            counter++;
            tempAnnealing *= 1.5;

            /*if(counter==9){
                priority=1;
                tempAnnealing = 0.1;
                firstConsistentData = delta;
                counter=0;
            }

            if(priority==1 || priority<0 || priority>1){
                priority=1;
                counter=0;
                tempAnnealing=0.1;
            }

        }

        //System.out.println("Priority is " + priority+" fd "+firstConsistentData);
        DecimalFormat df = new DecimalFormat("#.00");

        return Double.parseDouble(df.format(priority).replaceAll(",","."));

    }

*/



}