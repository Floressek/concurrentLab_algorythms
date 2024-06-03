package src.simulation;

import java.awt.*;

public class Lift extends Visitable {
    public Lift(Rectangle area, int capacity) {
        super(area, capacity);
        color = Color.ORANGE;
    }
}