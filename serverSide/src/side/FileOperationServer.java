package side;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileOperationServer {
    private static final String path = "C:\\Users\\muham\\Desktop\\OutputSensor\\OutputSensor";

    public  FileWriter createInputfile(String fileName) {

        String input = path + fileName.replace("/","") + ".txt";

        File file;
        FileWriter filewriter = null;


        file = new File(input);

        try {
            filewriter = new FileWriter(file);



        } catch (IOException e) {
            e.printStackTrace();
        }

        return filewriter;
    }


}