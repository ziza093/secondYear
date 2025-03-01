import java.io.*;
import java.util.Scanner;

import static java.lang.Character.isLowerCase;

public class Main {
    public static void main(String[] args) throws IOException {

        File file = new File("input.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"));

        String line;
        String output = "";

        while ((line = br.readLine()) != null) {
            String[] chars = line.split("(?!^)");
            for(String c : chars){
                if(c.toLowerCase().equals(c)){
                    c = c.toUpperCase();
                }
                else{
                    c = c.toLowerCase();
                }

                output = output.concat(c);
            }
        }

        bw.flush();
        bw.write(output);
        bw.close();
    }
}