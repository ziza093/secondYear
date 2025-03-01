import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        int n,m;

        Scanner sc = new Scanner(System.in);

        n = sc.nextInt();
        m = sc.nextInt();

        int[][] A = new int[n][m];
        int sum = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                A[i][j] = sc.nextInt();
            }
        }

        for (int i = 0; i < n; i++) {
            sum += A[i][0];
            sum += A[i][m-1];
        }

        for (int j = 1; j < m-1; j++) {
            sum+= A[0][j];
            sum+= A[n-1][j];
        }

//        1 2 3 4
//        5 6 7 8
//        9 1 1 2
//
        System.out.println("The sum on the margin is: " + sum);
    }
}