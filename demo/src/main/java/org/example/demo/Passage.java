package org.example.demo;

import java.util.concurrent.Semaphore;

public class Passage {
    private final Semaphore access = new Semaphore(1);

    public void goThrough() throws InterruptedException {
        access.acquire();
        try {
            // Simulating the passage time, which lasts 100 ms
            Thread.sleep(100);
        } finally {
            access.release();
        }
    }
}
