package src.lab3_4.Zadanie_3;


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

        for
        (int i = 0; i < LamportMulti.COUNT; i++) {
            new LamportMulti(i, iterationCount, synchronizedMode).start();
//            var l = new LamportMulti(i, iterationCount, synchronizedMode);
//            l.start();
        }
    }
}
