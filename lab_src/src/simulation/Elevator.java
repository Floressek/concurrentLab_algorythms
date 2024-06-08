package src.simulation;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

public class Elevator extends Visitable implements Runnable{

    public enum Direction {
        UP, DOWN
    }
    @Setter
    private JPanel panel;

    private int x;
    private int y;

    private int size;

    private int tolerance;
    private int wait = 500;
    private int sleep = 100;
    private int step = 2;

    private Direction direction = Direction.DOWN; // Default direction

    public Elevator(Rectangle area, int capacity) {
        super(area, capacity);
        color = Color.ORANGE;

        x = area.x + 1;
        y = area.y + 1;
        size = area.width - 2;
    }

    private boolean isUp() {
        return y <= area.y + tolerance;
    }

    private boolean isDown() {
        return y >= area.y + area.height - tolerance;
    }

    @Override
    public void run() {
        while(true) {
            int dx = 0;
            int dy = direction == Direction.UP ? -step : step; // Move up or down

            if(y + dy + size >= area.y + area.height || y + dy <= area.y) { // Check if elevator is at the top or bottom
                direction = direction == Direction.UP ? Direction.DOWN : Direction.UP; // Change direction

                try {
                    Thread.sleep(wait); // Wait for a while
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                continue;
            }
            x += dx;
            y += dy;

            panel.repaint();

            try {
                Thread.sleep(sleep); // Delay for smooth animation
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void paint(Graphics g) {
        super.paint(g); // Call the parent's paint method

        g.setColor(color);
        g.fillRect(x, y, size, size);
    }
}