import java.io.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {

        File file = new File("input.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));

        int n = Integer.parseInt(br.readLine());
        int sum = 0;
        String line;
        int[] arr = new int[n];

        while((line = br.readLine()) != null) {
            String[] numbers = line.split(" ");

            int index = 0;

            for(String nr : numbers) {
                arr[index++] = Integer.parseInt(nr);
            }
        }

        System.out.println("The sum is: " + Arrays.stream(arr).sum());
    }
}