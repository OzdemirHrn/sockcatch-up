package side;

import java.util.Random;


public class SensorType {
    private static final Random random = new Random();
    private int type = -1;
    private int counterFastWithSleep = 0;

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
        if (this.counterFastWithSleep < 15) {
            this.counterFastWithSleep++;
            return 10;
        } else {
            this.counterFastWithSleep = 0;
            return ((int) ((Math.random() * 10+10)*1000));
        }

    }

    private int normalSending() {
        return 400;
    }

    private int fastSending() {
        return 50;
    }

    private int randomSending() {
        int randomSending = random.nextInt(100);

        if (randomSending < 5) {
            randomSending = random.nextInt(7);
        } else if (randomSending < 10) {
            randomSending = randomSending * 5;
        } else if (randomSending < 20) {
            randomSending = randomSending * 7;
        } else if (randomSending < 30) {
            randomSending = random.nextInt(3);
        } else if (randomSending < 40) {
            randomSending = randomSending * 4;
        } else if (randomSending < 50) {
            randomSending = random.nextInt(3);
        } else if (randomSending < 60) {
            randomSending = randomSending * 5;
        } else if (randomSending < 70) {
            randomSending = random.nextInt(7);
        } else if (randomSending < 80) {
            randomSending = randomSending * 9;
        } else if (randomSending < 93) {
            //19k oalbilrş

            randomSending = 15;

        }

        return randomSending;
    }

}