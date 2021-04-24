package side;

public class Rtt {
    static double arrRttSample[] = new double[510];
    static double arrRttEstimated[] = new double[510];
    static double arrRttPriority[] = new double[510];
    static double arrRttaverage[] = new double[510];
    static double arrRttaveragePriority[] = new double[510];


    static double estimatedRtt = 0;
    static double estimatedRtt1 = 0;
    static double estimatedRtt2 = 0;
    static double averageRtt = 0;
    static double averageCounterRtt = 0;
    double alpha = 0;
    static int counter = 0;

    public Rtt(double alpha) {
        this.alpha = alpha;
    }


    public void arrayAnalysis(double sampleRtt) {
        arrRttSample[counter] = (int) (sampleRtt * 1000);
        arrRttEstimated[counter] = (int) (calculateEstimatedRtt(sampleRtt) * 1000);
        arrRttaverage[counter] = (int) (calculateAverageRtt(sampleRtt) * 1000);
        arrRttPriority[counter] = (int) (calculateEstimatedRtt125(sampleRtt) * 1000);
        arrRttaveragePriority[counter] = (int) (calculateEstimatedRtt2(sampleRtt) * 1000);

        //   	arrRttaveragePriority[counter] = calculateRTT(arrRttSample[counter], arrRttaverage[counter]);
        counter++;
    }

    public void printArray() {
        int i;
        System.out.println("rttsample *********************************************");

        for (i = 0; i < arrRttSample.length; i++)
            System.out.println(arrRttSample[i]);

        System.out.println("*****************************  rttEstimated");

        for (i = 0; i < arrRttSample.length; i++)
            System.out.println(arrRttEstimated[i]);

        System.out.println("*****************************  rttaverage");

        for (i = 0; i < arrRttSample.length; i++)
            System.out.println(arrRttaverage[i]);

        System.out.println("rtt yüzde 11111111111111111111111111111 *****");
        for (i = 0; i < arrRttSample.length; i++)
            System.out.println(arrRttPriority[i]);

        System.out.println("rttPriority average*****");
        for (i = 0; i < arrRttSample.length; i++)
            System.out.println(arrRttaveragePriority[i]);

        System.out.println("rttTime *****");
        for (i = 1; i < arrRttSample.length; i++)
            System.out.println(i);

    }

    public double calculateEstimatedRtt125(double sampleRtt) {
        if (estimatedRtt == 0)  // initializing, first input.
        {
            estimatedRtt = sampleRtt;
            return estimatedRtt;
        } else {
            double a = 0.125;
            estimatedRtt = (1 - a) * estimatedRtt + a * sampleRtt;
            return estimatedRtt;
        }
    }

    public double calculateEstimatedRtt(double sampleRtt) {
        if (estimatedRtt1 == 0)  // initializing, first input.
        {
            estimatedRtt1 = sampleRtt;
            return estimatedRtt1;
        } else {
            double a = this.alpha;
            estimatedRtt1 = (1 - a) * estimatedRtt1 + a * sampleRtt;
            return estimatedRtt1;
        }
    }

    public double calculateEstimatedRtt2(double sampleRtt) {
        if (estimatedRtt2 == 0)  // initializing, first input.
        {
            estimatedRtt2 = sampleRtt;
            return estimatedRtt2;
        } else {
            double a = 0.005;
            estimatedRtt2 = (1 - a) * estimatedRtt2 + a * sampleRtt;
            return estimatedRtt2;
        }
    }

    public double calculateAverageRtt(double sampleRtt) {

        if (averageRtt == 0)  // initializing, first input.
        {
            averageRtt = sampleRtt;
            averageCounterRtt = 1;

        } else {
//   		System.out.println("averageRtt "+ averageRtt+" sample: "+ sampleRtt);
            double holder = averageRtt * averageCounterRtt;
            averageCounterRtt++;
            averageRtt = (holder + sampleRtt) / averageCounterRtt;
//System.out.println("işlemden sonraki hali: "+ averageRtt);
        }
        return averageRtt;
    }

    public double calculateRTT(double sampleRtt, double estimatedRtt) {

        if (sampleRtt >= (estimatedRtt * 2))
            return 1;

        if (sampleRtt > estimatedRtt)
            return (0.5 + (((sampleRtt - estimatedRtt) / estimatedRtt) / 2.0));

        if (sampleRtt == estimatedRtt)
            return 0.5;


        if (sampleRtt < estimatedRtt && sampleRtt > (estimatedRtt / 2.0))
            return (0.5 - (((estimatedRtt - sampleRtt) / estimatedRtt) / 2.0));

        if (sampleRtt <= (estimatedRtt / 2.0))
            return 0;

        return -1.0;
    }

}