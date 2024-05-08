package src.lab5_6.test1;

import java.util.concurrent.Semaphore;

public class Producent extends Thread {


    int id;
    Bufor bufor = new Bufor();

    String dana;
    Semaphore sem;

    public Producent(Semaphore semaphor, int id, Bufor buffer) {
        this.id = id;
        bufor = buffer;
        sem = semaphor;
    }


    public void run() {
        for (int i = 1; i <= 50; i++) {
            try {
                Thread.sleep((long) (Math.random() * 1 + 10));
                dana = "Dana=[P-" + id + ", " + i + ", ";


                sem.acquire();

                bufor.dodaj(dana, Math.random() * 99);


            } catch (InterruptedException ignored) {
            }

            sem.release();
        }
    }


}
