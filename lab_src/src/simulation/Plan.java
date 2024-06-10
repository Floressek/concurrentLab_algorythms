package src.simulation;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Plan class represents a plan in the simulation.
 * It contains a list of visitable objects and start and end positions.
 */
@Getter
@Setter
public class Plan {
    /**
     * Enum for the type of plan.
     */
    enum PlanType {
        A, B
    }

    // Start position for the plan
    private Point startPosition;

    // End position for the plan
    private Point endPosition;

    // List of visitable objects in the plan
    @Setter(AccessLevel.NONE)
    private List<Visitable> visitables = new ArrayList<>();

    /**
     * Adds a visitable object to the plan.
     *
     * @param visitable The visitable object to be added.
     */
    public void addVisitable(Visitable visitable) {
        visitables.add(visitable); // Add visitable to the list
    }

    /**
     * Returns the visitable object at the specified index.
     *
     * @param index The index of the visitable object.
     * @return The visitable object at the specified index, or null if the index is invalid.
     */
    public Visitable getVisitable(int index) {
        if (!exists(index)) { // Check if index is valid
            return null;
        }

        return visitables.get(index); // Return visitable at index
    }

    /**
     * Resolves the next visitable object in the plan.
     *
     * @param index The index of the current visitable object.
     * @return The next visitable object, or null if there is no next visitable object.
     */
    Visitable resolveNextVisitable(int index) { // Resolve next visitable
        if (!exists(index + 1)) {
            return null;
        }

        return visitables.get(index + 1); // Return next visitable
    }

    /**
     * Checks if a visitable object exists at the specified index.
     *
     * @param index The index to check.
     * @return true if a visitable object exists at the specified index, false otherwise.
     */
    private boolean exists(int index) {
        return index >= 0 && index < visitables.size(); // Check if index is valid
    }

    /**
     * Resolves the rectangle for the specified position.
     *
     * @param position The position to resolve the rectangle for.
     * @return The resolved rectangle.
     */
    public Rectangle resolveRect(Point position) {
        int size = 10;
        return new Rectangle(position.x - size / 2, position.y - size / 2, size, size); // Return rectangle at position with size 10
    }

    /**
     * Resolves the goal rectangle for the specified index.
     *
     * @param index The index to resolve the goal rectangle for.
     * @return The resolved goal rectangle, or null if the index is invalid.
     */
    public Rectangle resolveGoalRect(int index) { // Resolve goal rectangle
        if (!exists(index)) {
            return null;
        }

        var current = visitables.get(index); // Get visitable at index
        var next = resolveNextVisitable(index); // Get next visitable
        int goalSize = 20; // Set goal size

        if (next == null) {
            return new Rectangle(endPosition.x - goalSize / 2, endPosition.y - goalSize / 2, goalSize, goalSize); // Return goal rectangle at end position
        }

        var intersection = current.area.getBounds().intersection(next.area.getBounds()); // Get intersection of visitable and next area

        if (intersection.width == 0) {
            var goalRect = new Rectangle(intersection);
            goalRect.x -= goalSize / 2; // Set goal x
            goalRect.width = goalSize; // Set goal width
            if (goalRect.height < 0) {
                goalRect.height = Math.min(current.area.height, next.area.height); // Set goal height
            }
            return goalRect; // Return goal rectangle
        }

        if (intersection.height == 0) { // Check if intersection height is 0
            var goalRect = new Rectangle(intersection);
            goalRect.y -= goalSize / 2; // Set goal y
            goalRect.height = goalSize; // Set goal height
            if (goalRect.width < 0) {
                goalRect.width = Math.min(current.area.width, next.area.width); // Set goal width
            }
            return goalRect;
        }

        return null;
    }

    /**
     * Reverses the plan.
     *
     * @return The reversed plan.
     */
    public Plan reverse() {
        var plan = new Plan();

        var list = new ArrayList<>(visitables); // Create a new list
        Collections.reverse(list); // Reverse the list

        plan.visitables = list; // Set the list to the plan
        plan.startPosition = endPosition; // Set the start position to the end position
        plan.endPosition = startPosition; // Set the end position to the start position

        return plan;
    }

    /**
     * Paints the plan.
     *
     * @param g The Graphics object to paint on.
     */
    public void paint(Graphics g) {
        visitables.forEach(v -> v.paint(g)); // Paint visitables
        g.setColor(Color.BLACK); // Set color to black
        var startRect = resolveRect(startPosition); // Resolve start rectangle
        g.drawRoundRect(startRect.x, startRect.y, startRect.width, startRect.height, 3, 3); // Draw start rectangle
        var endRect = resolveRect(endPosition); // Resolve end rectangle
        g.drawRoundRect(endRect.x, endRect.y, endRect.width, endRect.height, 3, 3); // Draw end rectangle
    }
}