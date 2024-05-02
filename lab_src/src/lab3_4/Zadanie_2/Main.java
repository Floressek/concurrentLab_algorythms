package src.lab3_4.Zadanie_2;


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

        Peterson p1 = new Peterson(0, iterationCount, synchronizedMode);
        Peterson p2 = new Peterson(1, iterationCount, synchronizedMode);

        p1.start();
        p2.start();
    }
}
