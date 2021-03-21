package side;

import java.io.*;
import java.util.ArrayList;

public class DataSet {

    public static void main(String[] args) throws IOException {
        readFromFile();
    }

    public static void readFromFile() throws IOException {
        File file = new File("C:\\Users\\hrnoz\\IdeaProjects\\sockcatch-up\\clientSide\\src\\dataset1.txt");
        byte[] directlyToClient = new byte[(int) file.length()];
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream;
        bufferedInputStream = new BufferedInputStream(fileInputStream);
        bufferedInputStream.read(directlyToClient, 0, directlyToClient.length);
        String str = new String(directlyToClient);
        String[] strSplit;
        strSplit = str.split(" ");
        ArrayList<Float> temperatureSensor1 = new ArrayList<>();
        ArrayList<Float> co2Sensor1 = new ArrayList<>();
        String[] timeLine;
        int count = 27;
        while (count < strSplit.length) {
            temperatureSensor1.add(Float.parseFloat(strSplit[count]));
            co2Sensor1.add(Float.parseFloat(strSplit[count+3]));
            count += 23;
        }

        for (Float f : temperatureSensor1) {
            System.out.println(f);

        }

        for (Float f : co2Sensor1) {
            System.out.println(f);

        }

    }

}
