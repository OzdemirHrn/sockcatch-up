package side;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class RunConfig {
    private final static String path = "C:\\Users\\muham\\IdeaProjects\\sockcatch-up\\clientSide\\src\\runConfigurations.txt";

    public static List<String> readConfig(int line) throws IOException {

        String lineOfSensorConfig = Files.readAllLines(Path.of(path)).get(line);
        String[] elements = lineOfSensorConfig.split(" ");
        return Arrays.asList(elements);

    }

    public static int returnNumberOfSensors() throws IOException {
        return Integer.parseInt(Files.readAllLines(Path.of(path)).get(0));
    }
}
