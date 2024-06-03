package src.simulation;

import java.awt.*;
import java.util.concurrent.Semaphore;

public abstract class Visitable extends Paintable {
    enum Side {
        Top, Bottom, Left, Right
    }

    protected Rectangle area;
    protected int capacity = 1;

    private Semaphore semaphore;

    public Visitable(Rectangle area, int capacity) {
        this.area = area;
        this.capacity = capacity;
        this.semaphore = new Semaphore(capacity);
    }

    public Visitable(Rectangle area) {
        this.area = area;
    }

    public void paint(Graphics g) {
        g.setColor(color);
        g.drawRect(area.x, area.y, area.width, area.height);
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

    void onEntry() throws InterruptedException {
        semaphore.acquire();
    }
    void onExit() {
        semaphore.release();
    }
}

