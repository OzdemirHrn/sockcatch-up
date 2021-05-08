package side;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileOperations {
    private static final String path = "C:/Users/hrnoz/Desktop/OutputSensor/";


    public static FileWriter createInputfile(String fileName) {

        String input = path + fileName.replace("/","") + ".txt";

        File file = null;
        FileWriter filewriter = null;


        file = new File(input);

        try {
            filewriter = new FileWriter(file);


            String inputFile = "";


            inputFile += "";


            inputFile += "\n";


            filewriter.write(inputFile);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return filewriter;
    }


}
