import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        int n,m;
        int number;
        int counter = 0;

        Scanner sc = new Scanner(System.in);

        n = sc.nextInt();
        m = sc.nextInt();

        int[][] A = new int[n][m];

        System.out.println("Read the number!");
        number = sc.nextInt();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                A[i][j] = sc.nextInt();
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if(A[i][j] == number){
                    counter++;
                }
            }
        }


        System.out.println("The number: " + number + " has appeared " + counter + " times.");
    }
}