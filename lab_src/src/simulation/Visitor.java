package src.simulation;

import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.StringJoiner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Visitor class that extends Paintable and implements Runnable and Controllable interfaces.
 * This class represents a visitor in the simulation.
 */
class Visitor extends Paintable implements Runnable, Controllable {

    private static Random RAND = new Random();

    private String name;

    private int x, y;

    private int sleep = 100;
    int step = 20;

    // Minimum of the time spent in a room(millis)
    long minSpentTime = 3 * 1000;
    long maxSpentTime = 7 * 1000;
    long entered;

    // Lock for synchronization and pause and terminate flags
    private final Object lock = new Object();
    private boolean paused = false;
    private boolean terminated = false;

    long pausedTime = 0;

    // Randomize the visiting of the visitor
    private void randomizeVisiting() {
        if (visitable instanceof Room) {
            visiting = RAND.nextBoolean();
        } else {
            visiting = false;
        }
    }

    // Flag for visiting
    private boolean visiting = true;

    // Color of the visitor based on the visiting status
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

    private int size = 20;

    final private Plan plan;

    // Visitable object and index
    private Visitable visitable;
    private int index;

    // Goal rectangle for the visitor
    private Rectangle goalRect;

    /**
     * Constructor for the Visitor class.
     * @param name Name of the visitor
     * @param plan Plan of the visitor
     */
    public Visitor(String name, Plan plan) {
        this.name = name;
        this.plan = plan;
        this.index = 0;

        this.x = plan.getStartPosition().x;
        this.y = plan.getStartPosition().y;
    }

    // Entry point for the visitor (goal)
    void detectGoal() {
        if (waitRequired()) {
            return;
        }

        var rect = getRect();

        if (rect.intersects(goalRect)) {
            moveNextVisitable();
        }
    }

    // Wait for the required time checker
    private boolean waitRequired() {
        var end = paused ? pausedTime : System.currentTimeMillis();

        return entered != -1 && end - entered < minSpentTime;
    }

    // MaxTime checker
    private boolean maxTimeSpentExceeded() {
        var end = paused ? pausedTime : System.currentTimeMillis();

        return entered != -1 && end - entered >= maxSpentTime;
    }

    //
    private void controlMaxSpentTime() {
        if (maxTimeSpentExceeded()) {
            visiting = false;
        }
    }

    void moveNextVisitable() {
        onExit(visitable); // Exit the current visitable
        onEntry(this.index + 1); // Move to the next visitable
    }


    void onEntry(int index) {
        visitable = plan.getVisitable(index);
        if (visitable == null) {
            this.index = -1;
            this.goalRect = null;
            this.x = plan.getEndPosition().x;
            this.y = plan.getEndPosition().y;
            return;
        }

        this.index = index; // Set the index
        this.goalRect = plan.resolveGoalRect(index == 0 ? index : this.index); // this makes it so that the goalRect is the next goalRect

        onEntry(visitable); // Entry the visitable
    }

    // Entry point for the visitor
    void onEntry(Visitable visitable) {
        try {
            visitable.onEntry(this); // Entry the visitor to the visitable

            if (visitable instanceof Room) {
                randomizeVisiting();
            } else {
                visiting = false;
            }

            registerEntryTime(visiting);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerEntryTime(boolean save) {
        entered = save ? System.currentTimeMillis() : -1; // Save the time if save is true
    }

    void onExit(Visitable visitable) {
        visitable.onExit(this);
    }

    public void move() {
        if (visitable == null) {
            return;
        }

        controlMaxSpentTime(); // Control the max spent time

        if (visiting) {
            moveRandom();
        } else {
            if (visitable instanceof Elevator) {
                moveWithElevator();
            } else {
                moveStraight();
            }
        }

        detectGoal();
    }

    public Rectangle getLocation() {
        return new Rectangle(x, y, size, size);
    }

    public Rectangle getRect() {
        if (visitable instanceof Elevator) {
            return ((Elevator) visitable).getLocation(); // Return the location of the elevator
        }

        return new Rectangle(x, y, size, size);
    }

    private void moveWithElevator() {
        if (!(visitable instanceof Elevator)) {
            return;
        }

        var location = ((Elevator) visitable).getLocation(); // Get the location of the elevator
        // Move the visitor to the center of the elevator
        x = location.x + location.width / 2;
        y = location.y + location.height / 2;
    }

    private void moveStraight() {
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
        ensureInArea();
    }

    private void ensureInArea() {
        x = Math.max(x, visitable.area.x);
        x = Math.min(x, visitable.area.x + visitable.area.width - size);
        y = Math.max(y, visitable.area.y);
        y = Math.min(y, visitable.area.y + visitable.area.height - size);
    }

    public void moveRandom() {
        int dx = ThreadLocalRandom.current().nextInt(-step, step + 1);
        int dy = ThreadLocalRandom.current().nextInt(-step, step + 1);
        x += dx;
        y += dy;

        // Ensure the visitor stays within the panel
        ensureInArea();
    }

    public void run() {
        onEntry(0);

        // Run until the visitor is terminated or paused
        while (!isTerminated() || visitable == null) {
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

    public void paint(Graphics g) {
        if (visitable instanceof Elevator) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;

        if (waitRequired()) {
            g.setColor(Color.GRAY);
            g.fillOval(x - 2, y - 2, size + 4, size + 4);
        } else if (maxTimeSpentExceeded()) {
            g.setColor(Color.BLACK);
            g.fillOval(x - 2, y - 2, size + 4, size + 4);
        }

        Color color = resolveColor();
        g.setColor(color);
        g.fillOval(x, y, size, size);

        if (name != null) {
            g.setColor(Color.BLACK);
            Font font = new Font("Serif", Font.BOLD, 12);
            g2.setFont(font);
            g2.drawString(name, x, y);
        }

        if (goalRect == null) {
            return;
        }

        // oznaczenie celu
        g.setColor(Color.GREEN);
        int goalCenterX = goalRect.x + goalRect.width / 2;
        int goalCenterY = goalRect.y + goalRect.height / 2;

        // This is the dashed line from the visitor to the goal
        var stroke = new BasicStroke(1f,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_MITER,
                5f,
                new float[]{10f, 15f},
                10f);
        g2.setStroke(stroke);
        g.setColor(Color.GREEN.darker());
        g.setColor(new Color(0, 128, 0, 64));
        g.drawLine(x, y, goalCenterX, goalCenterY);

        // Draw the goal rectangle BOX
        g.setColor(Color.GREEN);
        g2.setStroke(new BasicStroke());
        g.drawRoundRect(goalRect.x, goalRect.y, goalRect.width, goalRect.height, 3, 3);
    }

    // Terminate the visitor
    @Override
    public void terminate() {
        synchronized (lock) {
            terminated = true;
        }
    }

    // check if the visitor is terminated
    @Override
    public boolean isTerminated() {
        synchronized (lock) {
            return terminated;
        }
    }

    // Pause the visitor
    @Override
    public void pause() throws InterruptedException {
        synchronized (lock) {
            if (paused) {
                return;
            }

            paused = true;
            pausedTime = System.currentTimeMillis();
        }
    }

    @Override
    public void resume() {
        synchronized (lock) {
            if (!paused) {
                return;
            }

            paused = false;
            entered -= System.currentTimeMillis() - pausedTime;
        }
    }

    // Check if the visitor is paused
    @Override
    public boolean isPaused() {
        synchronized (lock) {
            return paused;
        }
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + "[", "]") // Return the visitor as a string
                .add("name='" + name + "'")
                .toString();
    }
}