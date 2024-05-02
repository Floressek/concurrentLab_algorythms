package src.lab3_4.Zadanie_1;

import java.util.Scanner;

class Main {
    public static void main(String[] args){
        int iterationCount = 10;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Input 1 for synchronized mode, 0 for not synchronized mode: ");
        int mode = scanner.nextInt();
//        int mode = 1;

        // Checks logic sentence
        boolean synchronizedMode = mode == 1;

        Dekker t1 = new Dekker(0, iterationCount, synchronizedMode);
        Dekker t2 = new Dekker(1, iterationCount, synchronizedMode);

        t1.start();
        t2.start();
    }
}
