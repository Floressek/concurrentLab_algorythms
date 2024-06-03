package src.simulation;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
public abstract class Paintable {
    protected Color color = Color.BLUE;

    abstract public void paint(Graphics g);
}
