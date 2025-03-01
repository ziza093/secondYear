import java.io.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {

        File file = new File("input.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;

        while((line = br.readLine()) != null) {
            System.out.println(line);
        }

    }
}