# Mine Simulation Project (Concurrent Programming)

## Project Structure 🗂️
The project is part of a larger repository containing different laboratory works. The main simulation project is located in the `simulation` folder, while other directories contain labs from different classes.

```
.
├── .idea
├── demo
├── lab_src
│   ├── .idea
│   └── src
│       ├── lab3_4
│       ├── lab5_6
│       ├── lab7_8
│       ├── lab9_10
│       └── simulation
│           ├── Connector.java
│           ├── Controllable.java
│           ├── Elevator.java
│           ├── Paintable.java
│           ├── Plan.java
│           ├── Room.java
│           ├── Simulation.java
│           ├── Visitable.java
│           ├── Visitor.java
│           ├── config.properties
│           ├── .gitignore
│           └── lab3_4.iml
├── .gitignore
├── LICENSE.md
├── README.md
└── lab3_4.iml
```

## About the Project 📝
This is a concurrent programming project that simulates a salt mine tourist attraction in the city of Saltville. The simulation demonstrates the concurrent movement of visitors through different rooms and elevators while respecting capacity constraints and movement rules.

### Core Components

- `Simulation.java`: Main class that controls the entire simulation
- `Visitor.java`: Represents individual tourists in the simulation
- `Room.java`: Represents visitable rooms in the mine
- `Elevator.java`: Handles vertical transportation of visitors
- `Plan.java`: Manages the visiting routes (Plan A and Plan B)
- `Connector.java`: Handles connections between rooms
- Other utility classes (`Paintable.java`, `Controllable.java`, `Visitable.java`)

## Key Features 🔑

- Multi-threaded simulation of tourist movement
- Two distinct visiting plans (Plan A and Plan B)
- Graphical user interface showing real-time simulation
- Configurable parameters (number of visitors, room capacities, etc.)
- Synchronized access to shared resources (rooms, elevators)
- Pause/Resume/Restart functionality
- Visual representation of visitor paths and states

## Configuration ⚙️

The simulation parameters can be configured through the `config.properties` file:

```properties
simulation.visitor-count=20
simulation.small-capacity=5
simulation.big-capacity=10
simulation.lift-capacity=5
```

## Movement Rules 📋

1. Visitors cannot stop in passages between rooms
2. Passages are bidirectional
3. Passages allow only one person at a time
4. Visitors can pass through rooms without visiting them
5. Rooms cannot be divided into restricted access zones

## GUI Controls 🎮

- **Pause/Resume Button**: Toggles the simulation state
- **Restart Button**: Restarts the simulation with current settings
- **Visitor Count Slider**: Adjusts the number of visitors in real-time

## Notes 📌

- Each room displays a counter showing the current number of visitors
- Green paths indicate visitor routes
- Visitors can move either randomly (when visiting) or directly (when passing through)
- The Pause button changes to Resume when simulation is paused
- Multiple rapid clicks on the Restart button should be avoided to prevent unstable behavior

## Implementation Details 🔧

The project uses various Java concurrency mechanisms:
- `ExecutorService` for thread management
- `Semaphore` for controlling room access
- `synchronized` blocks for thread safety
- `BlockingQueue` for managing visitor queues
- Virtual threads for efficient concurrent execution

## Project Status ⌛
This project was completed on 06/10/2024 as part of the Concurrent Programming course (PW-12/2024) under the supervision of dr inż. Jarosław Rulka.
