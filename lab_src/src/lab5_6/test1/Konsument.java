package src.lab5_6.test1;

import java.util.concurrent.Semaphore;

public class Konsument extends Thread {

    int id;
    Bufor bufor = new Bufor();

    String dana;
    Semaphore sem;

    public Konsument(Semaphore semaphor, int id, Bufor buffer) {
        this.id = id;
        bufor = buffer;
        sem = semaphor;
    }


    public void run() {
        for (int i = 1; i <= 40; i++) {
            try {
                Thread.sleep((long) (Math.random() * 2 + 12));
                System.out.println("Wstrzymano watek K-" + id);


                sem.acquire();
                System.out.println("[K-" + id + ", " + i + ", " + bufor.pozycja() + "] :: " + bufor.odczyt());


            } catch (InterruptedException ignored) {
            }

            sem.release();
        }
    }


}
