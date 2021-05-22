package side;
import java.util.Random;

public class RandomVariable {
    private static final Random random = new Random();


    public int getRandomVariable() {
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

            randomSending = 0;

        }

        return randomSending;

    }
}
