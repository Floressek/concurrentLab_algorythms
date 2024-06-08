package src.simulation;

import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class Visitor extends Thread {

    private static Random RAND = new Random();

    private int x, y;

    private int sleep = 100;
    int step = 20;

    @Setter
    private Color color = Color.PINK;

    private void randomizeVisiting() {
        visiting = RAND.nextBoolean();
    }

    private boolean visiting = true;

    private Color resolveColor() {
        if (visiting) {
            return color;
        } else {
//            return color.brighter();
            return color.darker();
        }
    }

    @Setter
    private JPanel panel;

    private int size = 10;

    final private Plan plan;

    private Visitable visitable;
    private int index;

    private Rectangle goalRect;

    public Visitor(Plan plan) {
        this.plan = plan;
        this.index = 0;
        this.visitable = plan.getVisitable(index);
        this.goalRect = plan.resolveGoalRect(index);

        this.x = plan.getStartPosition().x;
        this.y = plan.getStartPosition().y;
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

            if (visitable instanceof Room) {
                randomizeVisiting();
            } else {
                visiting = false;
            }
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

        if (visiting) {
            moveStraight();
        } else {
            moveRandom();
        }

        detectGoal();
    }

    private void moveStraight() {
        // fixme
        // Move towards the center of the goal rectangle
        int goalCenterX = goalRect.x + goalRect.width / 2;
        int goalCenterY = goalRect.y + goalRect.height / 2;

        // Calculate the distance to the goal
        int dx = goalCenterX - x;
        int dy = goalCenterY - y;

        // Calculate the unit vector towards the goal
        double distance = Math.sqrt(dx * dx + dy * dy); // Pitagoras
        if (distance > 0) {
            // Normalize the vector? (make it a unit vector)
            double vx = dx / distance;
            double vy = dy / distance;

            // Move towards the goal step by step
            x += (int) (vx * step);
            y += (int) (vy * step);
        }

        // Ensure the visitor stays within the panel
        x = Math.max(x, visitable.area.x);
        x = Math.min(x, visitable.area.x + visitable.area.width - size);
        y = Math.max(y, visitable.area.y);
        y = Math.min(y, visitable.area.y + visitable.area.height - size);
//        moveRandom();
    }

    public void moveRandom() {
        int dx = ThreadLocalRandom.current().nextInt(-step, step + 1);
        int dy = ThreadLocalRandom.current().nextInt(-step, step + 1);
        x += dx;
        y += dy;

        // Ensure the visitor stays within the panel
        x = Math.max(x, visitable.area.x);
        x = Math.min(x, visitable.area.x + visitable.area.width - size);
        y = Math.max(y, visitable.area.y);
        y = Math.min(y, visitable.area.y + visitable.area.height - size);
    }

    public void run() {
        onEntry(visitable);

        while (true) {
            move();

            // Random movement of the visitor
            panel.repaint();

            try {
                Thread.sleep(sleep); // Delay for smooth animation
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void paint(Graphics g) {
        Color color = resolveColor();

        g.setColor(color);
        g.fillOval(x, y, size, size);

        if (goalRect == null) {
            return;
        }

        g.setColor(Color.GREEN);
        g.drawRoundRect(goalRect.x, goalRect.y, goalRect.width, goalRect.height, 3, 3);
    }
}