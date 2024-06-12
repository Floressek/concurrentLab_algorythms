package src.simulation;

import lombok.Getter;

import java.awt.*;
import java.util.Queue;
import java.util.StringJoiner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;

/**
 * Abstract class Visitable that extends Paintable.
 * This class represents a visitable object in the simulation.
 * It has a name, an area, a capacity, and a visitor count.
 * It also has a semaphore for controlling access to the visitable.
 */

public abstract class Visitable extends Paintable {
    @Getter
    protected final String name;

    /**
     * Enum for the sides of the visitable.
     */
    enum Side {
        Top, Bottom, Left, Right
    }

    protected Rectangle area;
    protected int capacity = 1;

    int visitorCount = 0; // Number of visitors in the visitable

    private Semaphore semaphore;

    /**
     * Constructor for the Visitable class.
     * @param name Name of the visitable
     * @param area Area of the visitable
     * @param capacity Capacity of the visitable
     */
    public Visitable(String name, Rectangle area, int capacity) {
        this.name = name;
        this.area = area;
        this.capacity = capacity;
        this.semaphore = new Semaphore(capacity);
    }

    /**
     * Constructor for the Visitable class.
     * @param name Name of the visitable
     * @param area Area of the visitable
     */
    public Visitable(String name, Rectangle area) {
        this.name = name;
        this.area = area;
    }

    public void paint(Graphics g) {
        g.setColor(color);
        g.drawRect(area.x, area.y, area.width, area.height);

        if (g instanceof Graphics2D) {
            g.setColor(Color.BLACK);
            Graphics2D g2 = (Graphics2D)g;
            Font font = new Font("Serif", Font.BOLD, 12);
            g2.setFont(font);

            var sb = new StringBuffer();
            if (name != null) {
                sb.append(name);

                sb.append('[');
                sb.append(visitorCount - queue.size());
                sb.append(']');
            }

            g2.drawString(sb.toString(), area.x, area.y - 10);
        }
    }

    Rectangle createArea(Side side, int width, int height) {
        switch (side) {
            case Top -> {
                return new Rectangle(
                        area.x + (area.width - height) / 2,
                        area.y - width,
                        height,
                        width);
            }
            case Bottom -> {
                return new Rectangle(
                        area.x + (area.width - height) / 2,
                        area.y + area.height,
                        height,
                        width);
            }
            case Left -> {
                return new Rectangle(
                        area.x - width,
                        area.y + (area.height - height) / 2,
                        width,
                        height);
            }
            case Right -> {
                return new Rectangle(
                        area.x + area.width,
                        area.y + (area.height - height) / 2,
                        width,
                        height);
            }
            default -> {
                throw new IllegalArgumentException();
            }
        }
    }

    private final Object lock = new Object();

    private Queue queue = new LinkedBlockingQueue();

    public void onEntry(Visitor v) throws InterruptedException {
        if (!semaphore.tryAcquire()) {
            queue.offer(v);
            v.pause();
        }

        synchronized (lock) {
            visitorCount++;
        }
    }
    public void onExit(Visitor v) {
        semaphore.release(1);

        synchronized (lock) {
            visitorCount--;
            var next = (Visitor) queue.poll(); // Get next visitor
            if (next != null) {
                next.resume(); // Resume next visitor
            }
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .toString();
    }
}

