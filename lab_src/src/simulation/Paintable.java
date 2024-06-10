package src.simulation;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

/**
 * Abstract class Paintable.
 * This class provides a structure for objects that can be painted on a graphical interface.
 */
@Getter
@Setter
public abstract class Paintable {
    // Color of the paintable object, default is blue
    protected Color color = Color.BLUE;

    /**
     * Abstract method to paint the object.
     * This method needs to be implemented in any class that extends Paintable.
     *
     * @param g Graphics object used for painting.
     */
    abstract public void paint(Graphics g);
}
