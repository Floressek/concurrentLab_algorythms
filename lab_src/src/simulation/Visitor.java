package src.simulation;

import lombok.Setter;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

class Visitor extends Thread {
    private int x, y;

    @Setter
    private Color color = Color.PINK;

    @Setter
    private JPanel panel;

    private int size = 10;

    private Plan plan;

    private Visitable visitable;
    private int index;

    private Rectangle goalRect;

    public Visitor(Plan plan) {
        this.plan = plan;
        this.index = 0;
        this.visitable = plan.getVisitable(index);
        this.goalRect = plan.resolveGoalRect(index);

        this.x = plan.getStarPosition().x;
        this.y = plan.getStarPosition().y;
    }

    void detectGoal() {
        var rect = new Rectangle(x, y, size, size);

        if (rect.intersects(goalRect)) {
            moveNextVisitable();
        }
    }

    void moveNextVisitable() {
        onExit(visitable);

        visitable = plan.resolveNextVisitable(index);
        index++;
        goalRect = plan.resolveGoalRect(index);

        if (visitable != null) {
            onEntry(visitable);
        }
    }

    void onEntry(Visitable visitable) {
        try {
            visitable.onEntry();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    void onExit(Visitable visitable) {
        visitable.onExit();
    }

    public void move() {
        if (visitable == null) {
            return;
        }

        var step = 20;

        int dx = ThreadLocalRandom.current().nextInt(-step, step + 1);
        int dy = ThreadLocalRandom.current().nextInt(-step, step + 1);
        x += dx;
        y += dy;

        // Ensure the visitor stays within the panel
        x = Math.max(x, visitable.area.x);
        x = Math.min(x, visitable.area.x + visitable.area.width - size);
        y = Math.max(y, visitable.area.y);
        y = Math.min(y, visitable.area.y + visitable.area.height - size);

        detectGoal();


//        x = Math.max(0, Math.min(x, panel.getWidth() - 10));
//        y = Math.max(0, Math.min(y, panel.getHeight() - 10));
    }

    public void run() {
//        try {
//            Thread.sleep(new Random().nextInt(0, 30000));
////            Thread.sleep(ThreadLocalRandom.current().nextInt(0, 30000));
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        onEntry(visitable);

        while (true) {
            move();

            // Random movement of the visitor
            panel.repaint();

            try {
                Thread.sleep(100); // Delay for smooth animation
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void paint(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, size, size);

        if (goalRect == null) {
            return;
        }

        g.setColor(Color.GREEN);
        g.drawRoundRect(goalRect.x, goalRect.y, goalRect.width, goalRect.height, 3, 3);
    }
}