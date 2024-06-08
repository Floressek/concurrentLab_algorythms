package src.simulation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;


public class Simulation extends JFrame {
    private JPanel panel;

    List<Plan> plans = new ArrayList<>();
    List<Visitor> visitors = new ArrayList<>();

    public Simulation() throws HeadlessException {
        setTitle("Visitor Simulation");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                plans.forEach(v -> v.paint(g));
                visitors.forEach(v -> v.paint(g));
            }
        };
        add(panel, BorderLayout.CENTER);

        var planA = createPlan(Plan.PlanType.A);
        var planB = planA.reverse();

        visitors = new ArrayList<>();
        var count = 50;

        var executor = Executors.newVirtualThreadPerTaskExecutor();

        for (int i = 0; i < count; i++) {
            var plan = i % 2 == 0 ? planA : planB;
            var color = i % 2 == 0 ? Color.BLUE : Color.RED;

            var visitor = new Visitor(plan);

            visitor.setColor(color);
            visitor.setPanel(panel);

            visitors.add(visitor);
        }

        plans.add(planB);

        for (var v : visitors) {
//            v.start();
            executor.execute(Thread.ofVirtual().start(v));
        }

        plans.get(0).getVisitables().stream()
                .filter(v -> v instanceof Runnable) // Filter only Runnable objects
                .map(Runnable.class::cast) // Cast to Runnable for each object in the stream
                .forEach(v -> executor.execute(Thread.ofVirtual().start(v))); // Execute each Runnable object in the stream using the executor

    }

    Plan createPlan(Plan.PlanType planType) {
        Plan plan = new Plan();

        switch (planType) {
            case A -> {
                var smallRoomSize = 100;
                var bigRoomSize = (int) (smallRoomSize * Math.sqrt(2));
                var connWidth = 30;
                var connHeight = 26;
                var liftWidth = 80;
                var liftHeight = 30;
                var smallCapacity = 5;
                var bigCapacity = smallCapacity * 2;
                var liftCapacity = 10;

                // room 1
                var room1 = new Room(new Rectangle(100, 100, smallRoomSize, smallRoomSize), smallCapacity);

                // conn12
                var area = room1.createArea(Visitable.Side.Right, connWidth, connHeight);
                var conn12 = new Connector(area);

                // room2
                var room2 = new Room(new Rectangle(conn12.area.x + conn12.area.width, room1.area.y, bigRoomSize, bigRoomSize), bigCapacity); // description: room2 is created with the area of conn12 and the size of bigRoomSize and bigCapacity

                // lift1
                area = room2.createArea(Visitable.Side.Top, liftWidth, liftHeight);
                area.x = area.x + bigRoomSize / 4;
                var elevator1 = new Elevator(area, liftCapacity);
                elevator1.setPanel(panel);

                // conn23
                area = room2.createArea(Visitable.Side.Right, connWidth, connHeight);
                area.y = conn12.area.y;
                var conn23 = new Connector(area);

                // room3
                var room3 = new Room(new Rectangle(conn23.area.x + conn23.area.width, room1.area.y, smallRoomSize, smallRoomSize), smallCapacity);

                // lift2
                area = room3.createArea(Visitable.Side.Top, liftWidth, liftHeight);
                var elevator2 = new Elevator(area, liftCapacity);
                elevator2.setPanel(panel);

                // con34
                area = room3.createArea(Visitable.Side.Bottom, connWidth, connHeight);
                var conn34 = new Connector(area);

                // room4
                var room4 = new Room(new Rectangle(room3.area.x, conn34.area.y + conn34.area.height, smallRoomSize, smallRoomSize), smallCapacity);

//                plan.addVisitable(room1);
//                plan.addVisitable(conn12);
//                plan.addVisitable(room2);
//                plan.addVisitable(conn23);
//                plan.addVisitable(room3);
//                plan.addVisitable(conn34);
//                plan.addVisitable(room4);
//                plan.setStarPosition(new Point(room1.area.x + 1, room1.area.y + room1.area.height / 2));
//                plan.setEndPosition(new Point(room4.area.x + room4.area.width / 2, room4.area.y + room1.area.height - 1));

                plan.addVisitable(elevator1);
                plan.addVisitable(room2);
                plan.addVisitable(conn12);
                plan.addVisitable(room1);
                plan.addVisitable(conn12);
                plan.addVisitable(room2);
                plan.addVisitable(conn23);
                plan.addVisitable(room3);
                plan.addVisitable(conn34);
                plan.addVisitable(room4);
                plan.addVisitable(conn34);
                plan.addVisitable(room3);
                plan.addVisitable(elevator2);
                plan.setStartPosition(new Point(elevator1.area.x + elevator1.area.width / 2, elevator1.area.y));
                plan.setEndPosition(new Point(elevator2.area.x + elevator2.area.width / 2, elevator2.area.y));
            }
            case B -> {
                throw new RuntimeException("Not implemented");
            }

            default -> throw new RuntimeException("Not implemented");
        }

        return plan;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var simulation = new Simulation();
            simulation.setVisible(true);
        });
    }
}
