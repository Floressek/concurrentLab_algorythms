package src.simulation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class Plan {
    enum PlanType {
        A, B
    }

    private Point starPosition;

    private Point endPosition;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<Visitable> visitables = new ArrayList<>();

    public void addVisitable(Visitable visitable) {
        visitables.add(visitable);
    }

    public Visitable getVisitable(int index) {
        if (!exists(index)) {
            return null;
        }

        return visitables.get(index);
    }

    Visitable resolveNextVisitable(int index) {
        if (!exists(index + 1)) {
            return null;
        }

        return visitables.get(index + 1);
    }

    private boolean exists(int index) {
        return index >= 0 && index < visitables.size();
    }

    public Rectangle resolveRect(Point position) {
        int size = 10;
        return new Rectangle(position.x - size / 2, position.y - size / 2, size, size);
    }

    public Rectangle resolveGoalRect(int index) {
        if (!exists(index)) {
            return null;
        }

        var visitable = visitables.get(index);
        var next = resolveNextVisitable(index);
        int goalSize = 20;

        if (next == null) {
            return new Rectangle(endPosition.x - goalSize / 2, endPosition.y - goalSize / 2, goalSize, goalSize);
        }

        var intersection = visitable.area.getBounds().intersection(next.area.getBounds());

        if (intersection.width == 0) {
            var goalRect = new Rectangle(intersection);
            goalRect.x -= goalSize / 2;
            goalRect.width = goalSize;
            return goalRect;
        }

        if (intersection.height == 0) {
            var goalRect = new Rectangle(intersection);
            goalRect.y -= goalSize / 2;
            goalRect.height = goalSize;
            return goalRect;
        }

        return null;
    }

    public Plan reverse() {
        var plan = new Plan();

        var list = new ArrayList<>(visitables);
        Collections.reverse(list);

        plan.visitables = list;
        plan.starPosition = endPosition;
        plan.endPosition = starPosition;

        return plan;
    }


    public void paint(Graphics g) {
        visitables.forEach(v -> v.paint(g));
        g.setColor(Color.BLACK);
        var startRect = resolveRect(starPosition);
        g.drawRoundRect(startRect.x, startRect.y, startRect.width, startRect.height, 3, 3);
        var endRect = resolveRect(endPosition);
        g.drawRoundRect(endRect.x, endRect.y, endRect.width, endRect.height, 3, 3);
    }

}