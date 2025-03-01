package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        int n;
        int[] vect;

        Scanner scanner = new Scanner(System.in);

        n = scanner.nextInt();
        vect = new int[n];

        for(int i = 0; i < n; i++) {
            vect[i] = scanner.nextInt();
        }

        int min = vect[0];

        for(int i = 0; i < n; i++) {
            if(vect[i] < min) {
                min = vect[i];
            }
        }

        System.out.println("The minimum number is: " + min);
    }
}