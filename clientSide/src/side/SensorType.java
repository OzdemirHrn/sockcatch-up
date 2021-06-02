package side;
import java.util.Random;



import java.util.Random;


public class SensorType {
    private static final Random random = new Random();
    private int type = -1;
    private int counterFastWithSleep = 0;
    private int deciderFastWithSleep = 0;

    public SensorType(int type) {
        this.type = type;
    }

    public int operatingFrequency() {
        if (type == 1) {
            return fastSending();
        } else if (type == 2) { // 15ms-900ms
            return randomSending();
        } else if (type == 3) {
            return normalSending();
        } else if (type == 4) {
            return fastwithSleep();
        } else {
            System.err.println("An error has occurred at SensorType-operatingFrequency. Please check type.");
            return -100;
        }

    }

    private int fastwithSleep() {


        if ( deciderFastWithSleep == 1 &&  this.counterFastWithSleep < 50) {
            this.counterFastWithSleep++;
            return 10;
        } else {
            this.counterFastWithSleep = 0;
            this.deciderFastWithSleep = (int) ((Math.random() *2));
            return ((int) ((Math.random() * 10+2)*1000));
        }

    }

    private int normalSending() {
        return 400;
    }

    private int fastSending() {
        return 100;
    }

    private int randomSending() {
        int randomSending = random.nextInt(100);

        if (randomSending < 3) {
            randomSending = 50;
        } else if (randomSending < 6) {
            randomSending = 2000;
        }
        else {
            randomSending = randomSending * 10;
        }
//         else if (randomSending < 26) {
//
//        } else if (randomSending < 36) {
//            randomSending = randomSending * 4;
//        } else if (randomSending < 46) {
//
//        } else if (randomSending < 60) {
//            randomSending = randomSending * 5;
//        } else if (randomSending < 70) {
//
//        } else if (randomSending < 80) {
//            randomSending = randomSending * 9;
//        } else if (randomSending < 93) {
//            randomSending = 15;
//        }

        return randomSending;
    }

}