package src.simulation;

import java.awt.*;

/**
 * The Room class extends the Visitable class.
 * It represents a room in the simulation.
 */
public class Room extends Visitable {

    /**
     * Constructor for the Room class.
     *
     * @param area The area of the room.
     * @param capacity The capacity of the room.
     * @param name The name of the room.
     */
    public Room(String name, Rectangle area, int capacity) {
        super(name, area, capacity);
    }

}