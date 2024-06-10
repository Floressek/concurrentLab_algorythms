package src.simulation;

// lombok imports for getter and setter

import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Elevator extends Visitable implements Runnable, Controllable {

    // Lock for synchronization
    private final Object lock = new Object();

    // Pause and terminate flags
    private boolean paused = false;
    private boolean terminated = false;

    private Queue<Visitor> upQueue = new LinkedBlockingQueue();
    private Queue<Visitor> downQueue = new LinkedBlockingQueue();

    private Queue<Visitor> insideQueue = new LinkedBlockingQueue();


    public enum Direction {Up, Down}

    private int transferredCount = 0;

    @Setter
    private JPanel panel;

    // Elevator properties
    private int x;
    private int y;

    private int size;

    // Tolerance, wait, sleep, and step values
    private int tolerance = 10;
    private int wait = 1000;
    private int sleep = 100;
    private int step = 2;

    private Direction direction = Direction.Down;

    public Elevator(String name, Rectangle area, int capacity) {
        super(name, area, capacity);
        color = Color.ORANGE;

        size = area.width - 2;
        x = area.x + 1;
        y = area.y + area.height - size - 1;
        step = Math.max(2, area.height / 31);

        insideQueue = new LinkedBlockingQueue<Visitor>(capacity);
    }


    private boolean isUp() {
        return y <= area.y + tolerance;
    }

    private boolean isCloseToTop(Rectangle rect) {
        return rect.intersects(new Rectangle(area.x, area.y - tolerance, area.width, area.height / 2));
    }

    private boolean isCloseToBottom(Rectangle rect) {
        return rect.intersects(new Rectangle(area.x, area.y + area.height / 2 + tolerance, area.width, area.height / 2));
    }

    private boolean isDown() {
        return y >= area.y + area.height - tolerance;
    }

    @Override
    public void run() {
        while (!isTerminated()) {
            if (!isPaused()) {
                move();
                panel.repaint();
            }

            try {
                Thread.sleep(sleep); // Delay for smooth animation
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void move() {
        int dx = 0;
        int dy = direction == Direction.Up ? -step : step;

        if (y + dy + size >= area.y + area.height || y + dy <= area.y) {
            direction = direction == Direction.Up ? Direction.Down : Direction.Up;

            if (direction == Direction.Up) {
                notifyDown();
            } else {
                notifyUp();
            }

            try {
                Thread.sleep(wait); // Delay for smooth animation
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        // fixme
        x += dx; // Move x not needed
        y += dy; // Move y
    }

    public void paint(Graphics g) {
        super.paint(g); // Call the parent's paint method

        Graphics2D g2 = (Graphics2D) g;

        // Draw the elevator
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y, size, size);

        Font font = new Font("Serif", Font.BOLD, 12);
        g2.setFont(font);

        if (name != null) { // Draw the name of the elevator
            g.setColor(Color.BLACK);
            g2.drawString(String.valueOf(insideQueue.size()), x + size / 2, y + size / 2);
        }

        // Draw the number of transferred visitors
        g2.drawString(String.format("T: %d", transferredCount), area.x - 30, area.y + 10);

        // Draw the up queue
        g2.drawString(String.format("Q: %d", upQueue.size()), area.x + area.width + 5, area.y + 10);
        g2.drawString(String.format("Q: %d", downQueue.size()), area.x + area.width + 5, area.y + area.height - 10);
    }

    public void onEntry(Visitor v) throws InterruptedException {
        Queue queue = null; // Initialize queue

        var vLoc = v.getLocation(); // Get visitor location

        if (isCloseToTop(vLoc)) { // Check if visitor is close to top
            queue = upQueue; // Set queue to up queue
        } else if (isCloseToBottom(vLoc)) { // Check if visitor is close to bottom
            queue = downQueue; // Set queue to down queue
        } else {
            throw new IllegalArgumentException("Visitor is not close to top or bottom"); // Throw exception
        }

        synchronized (lock) {
            if (queue.offer(v)) {
                v.pause(); // Pause visitor
            }
            visitorCount++;
        }
    }


    public void onExit(Visitor v) {
        synchronized (lock) {
            visitorCount--;
        }
    }


    private void notifyUp() {
        synchronized (lock) {
            transferredCount += emptyInsideQueue();
            fillInsideQueue(upQueue);
        }
    }

    private void notifyDown() {
        synchronized (lock) {
            emptyInsideQueue();
            fillInsideQueue(downQueue);
        }
    }

    private int emptyInsideQueue() {
        int count = 0;
        do {
            var v = insideQueue.poll();
            if (v != null) {
                v.resume();
                count++;
            }
        } while (!insideQueue.isEmpty());
        return count;
    }

    private void fillInsideQueue(Queue<Visitor> source) {
        if (source.size() != 0) { // Check if source is not empty
            do {
                var v = source.peek(); // Get visitor
                if (insideQueue.offer(v)) { // Offer visitor to inside queue
                    source.remove(v); // Remove visitor from source
                    try {
                        v.pause(); // Pause visitor
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    break;
                }
            } while (!source.isEmpty()); // Repeat until source is empty
        }
    }

    // terminate, isTerminated, pause, resume, isPaused, getLocation methods
    @Override
    public void terminate() {
        synchronized (lock) {
            terminated = true;
        }
    }

    @Override
    public boolean isTerminated() {
        synchronized (lock) {
            return terminated;
        }
    }

    @Override
    public void pause() throws InterruptedException {
        synchronized (lock) {
            if (paused) { // Check if paused
                return;
            }
            paused = true; // Set paused to true
        }
    }

    @Override
    public void resume() {
        synchronized (lock) {
            if (!paused) { // Check if not paused
                return;
            }
            paused = false; // Set paused to false
        }
    }

    @Override
    public boolean isPaused() {
        synchronized (lock) {
            return paused;
        }
    }

    public Rectangle getLocation() {
        return new Rectangle(x, y, size, size);
    }
}