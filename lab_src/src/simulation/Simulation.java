package src.simulation;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * The Simulation class represents the main simulation environment.
 * It extends JFrame to create a GUI for the simulation.
 * It contains a list of plans and visitors for the simulation.
 * It also contains an ExecutorService to manage threads for the simulation.
 */
public class Simulation extends JFrame {

    // Default values for visitor count, small capacity, big capacity, and lift capacity
    @Getter
    @Setter
    private int visitorCount = 10;
    @Getter
    @Setter
    private int smallCapacity = 5;
    @Getter
    @Setter
    private int bigCapacity = smallCapacity * 2;
    @Getter
    @Setter
    private int liftCapacity = 5;

    // Panel for the simulation
    private JPanel panel;
    // List of plans for the simulation
    private List<Plan> plans = new ArrayList<>();
    // List of visitors for the simulation
    private List<Visitor> visitors = new ArrayList<>();

    // ExecutorService for managing threads
    private ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    /**
     * Constructor for the Simulation class.
     * It initializes the GUI for the simulation.
     */
    public Simulation() throws HeadlessException {
        setTitle("Visitor Simulation");
        setSize(1980, 1024);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create buttons and slider for the simulation
        var btnPause = new JToggleButton("Pause");
        var btnRestart = new JButton("Restart");
        var sliderVisitorCount = getjSlider();


        // Create command panel
        var cmdPanel = new JToolBar();
        cmdPanel.add(btnRestart);
        cmdPanel.addSeparator();
        cmdPanel.add(btnPause);
        cmdPanel.addSeparator();
        cmdPanel.add(new JLabel("Visitors:"));
        cmdPanel.add(sliderVisitorCount);

        btnPause.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();

                if (state == ItemEvent.SELECTED) {
                    pauseSimulation();
                    btnPause.setText("Resume");
                } else {
                    resumeSimulation();
                    btnPause.setText("Pause");
                }
            }
        });

        btnRestart.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartSimulation();
            }
        });

        panel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                plans.forEach(v -> v.paint(g));
                visitors.forEach(v -> v.paint(g));
            }
        };
        add(cmdPanel, BorderLayout.PAGE_START);
        add(panel, BorderLayout.CENTER);
    }

    // Create slider for visitor count
    private JSlider getjSlider() {
        var sliderVisitorCount = new JSlider(JSlider.HORIZONTAL, 10, 100, 10);
        sliderVisitorCount.setMajorTickSpacing(10);
        sliderVisitorCount.setMinorTickSpacing(5);
        sliderVisitorCount.setPaintTicks(true);
        sliderVisitorCount.setPaintLabels(true);
        sliderVisitorCount.addChangeListener(e -> {
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {
                visitorCount = (int)source.getValue();
                restartSimulation();  // Restart simulation with new visitor count
            }
        });
        return sliderVisitorCount;
    }

    // Get list of runnable objects
    List<Runnable> getRunnable() {
        var result = new ArrayList<Runnable>(visitors);

        // Add visitables that are runnable
        if (plans.size() != 0) {
            // Step 1: Get the first plan, then get the visitables from the plan,
            // then filter the visitables to get only the runnable visitables, then add the visitables to the result list
            plans.get(0).getVisitables().stream()
                    .filter(v -> v instanceof Runnable)
                    .map(Runnable.class::cast)
                    .forEach(v -> result.add(v));
        }
        return result;
    }

    // Create simulation
    private void createSimulation() {
        visitors = new ArrayList<>();
        plans = new ArrayList<>();

        // Create plan A
        var planA = createPlan(Plan.PlanType.A);
        // Create plan B
        var planB = planA.reverse();

        // Create visitors and assign plans
        for (int i = 0; i < visitorCount; i++) {
            var plan = i % 2 == 0 ? planA : planB;
            var color = i % 2 == 0 ? Color.BLUE : Color.RED;

            var visitor = new Visitor(String.format("%s", i + 1), plan);

            visitor.setColor(color);
            visitor.setPanel(panel);

            visitors.add(visitor);
        }

        plans.add(planB);
    }

    // Force restart simulation
    void restartSimulation() {
        for (var v : getRunnable()) {
            if (v instanceof Controllable)
                ((Controllable) v).terminate();
        }
        createSimulation();
        startSimulation();
    }

    // Start simulation
    void startSimulation() {
        for (var v : getRunnable()) {
            executor.execute(Thread.ofVirtual().start(v));
        }
    }

    // Pause simulation
    void pauseSimulation() {
        for (var v : getRunnable()) {
            try {
                if (v instanceof Controllable) { // Check if v is an instance of Controllable
                    ((Controllable) v).pause();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Resume simulation
    void resumeSimulation() {
        for (var v : getRunnable()) {
            if (v instanceof Controllable) {
                ((Controllable) v).resume();
            }
        }
    }

    // Create plan
    Plan createPlan(Plan.PlanType planType) {
        Plan plan = new Plan();

        // Create plan based on plan type
        switch (planType) {
            case A -> {
                var smallRoomSize = 100 * 3;
                var bigRoomSize = (int) (smallRoomSize * Math.sqrt(2));
                var connWidth = 30;
                var connHeight = 26;
                var liftWidth = 80 * 3;
                var liftHeight = 30;

                // room 1
                var room1 = new Room("R1", new Rectangle(100, 100 * 3, smallRoomSize, smallRoomSize), smallCapacity);

                // conn12
                var area = room1.createArea(Visitable.Side.Right, connWidth, connHeight);
                var conn12 = new Connector(area);

                // room2
                var room2 = new Room("R2", new Rectangle(conn12.area.x + conn12.area.width, room1.area.y, bigRoomSize, bigRoomSize), bigCapacity);

                // lift1
                area = room2.createArea(Visitable.Side.Top, liftWidth, liftHeight);
                area.x = area.x + bigRoomSize / 4;
                var elevator1 = new Elevator("E1", area, liftCapacity);
                elevator1.setPanel(panel);

                // conn23
                area = room2.createArea(Visitable.Side.Right, connWidth, connHeight);
                area.y = conn12.area.y;
                var conn23 = new Connector(area);

                // room3
                var room3 = new Room("R3", new Rectangle(conn23.area.x + conn23.area.width, room1.area.y, smallRoomSize, smallRoomSize), smallCapacity);

                // lift2
                area = room3.createArea(Visitable.Side.Top, liftWidth, liftHeight);
                var elevator2 = new Elevator("E2", area, liftCapacity);
                elevator2.setPanel(panel);

                // con34
                area = room3.createArea(Visitable.Side.Bottom, connWidth, connHeight);
                var conn34 = new Connector(area);

                // room4
                var room4 = new Room("R4", new Rectangle(room3.area.x, conn34.area.y + conn34.area.height, smallRoomSize, smallRoomSize), smallCapacity);

                // Add visitables to plan
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

    // Load configuration from file
    private static Properties loadConfig(String path) {
        // load data
        if (path == null) {
            path = Simulation.class.getResource("config.properties").getPath();
        }

        try {
            var config = new Properties();
            config.load(new FileInputStream(path));
            return config;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Main method
    public static void main(String[] args) {
        String path = args.length != 0 ? args[0] : null;
        var config = loadConfig(path);

        var simulation = new Simulation();

        if (config != null) {
            simulation.loadFromConfig(config);
        }

        simulation.createSimulation();
        simulation.startSimulation();

        SwingUtilities.invokeLater(() -> {
            simulation.setVisible(true);
        });
    }

    // Load configuration for the external file
    private void loadFromConfig(Properties config) {
        int count = Integer.valueOf(config.getProperty("simulation.visitor-count", "10"));
        visitorCount = count;
        int small = Integer.valueOf(config.getProperty("simulation.small-capacity", "5"));
        smallCapacity = small;
        int big = Integer.valueOf(config.getProperty("simulation.big-capacity", "10"));
        bigCapacity = big;
        int lift = Integer.valueOf(config.getProperty("simulation.lift-capacity", "5"));
        liftCapacity = lift;


    }
}
