package side;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DataSet {

    private final static String path="C:\\Users\\malik türkoğlu\\Desktop\\sockcatch-up\\\\clientSide\\src\\dataset.txt";

    private DataSet() {

    }

    public static ArrayList<Float> readFromFile(int count) throws IOException {
        File file = new File(path);
        byte[] directlyToClient = new byte[(int) file.length()];
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream;
        bufferedInputStream = new BufferedInputStream(fileInputStream);
        bufferedInputStream.read(directlyToClient, 0, directlyToClient.length);
        String str = new String(directlyToClient);
        String[] strSplit;
        strSplit = str.split(" ");
        ArrayList<Float> sensorValuesFromDataSet = new ArrayList<>();


        while (count < strSplit.length) {
            sensorValuesFromDataSet.add(Float.parseFloat(strSplit[count]));
            count += 23;
        }

        return sensorValuesFromDataSet;

    }


}
