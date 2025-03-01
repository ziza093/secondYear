import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        int n;
        int[] vect;

        Scanner sc = new Scanner(System.in);

        n = sc.nextInt();
        vect = new int[n];

        for (int i = 0; i < n; i++) {
            vect[i] = sc.nextInt();
        }

        for(int i = 0; i < n-1; i++) {
            if(vect[i] > vect[i+1]){
                System.out.println("The vector isn't in ascending order!");
                return;
            }
        }

        System.out.println("The vector is in ascending order!");
    }
}