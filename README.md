# Java Synchronization Algorithms

This project demonstrates the implementation of classic synchronization algorithms in Java, including Dekker's, Lamport's, and Peterson's algorithms, along with an example of a semaphore. These implementations are designed to help understand the basics of process synchronization and mutual exclusion in concurrent programming.

## Overview

- **Dekker's Algorithm**: Ensures mutual exclusion between two threads.
- **Lamport's Bakery Algorithm**: A more general algorithm that allows for mutual exclusion among N threads.
- **Peterson's Algorithm**: Simplifies Dekker's approach for two threads, ensuring mutual exclusion with a simpler logic.
- **Semaphore Example**: Demonstrates the use of semaphores to control access to a shared resource by multiple threads.

# Main project of the mine-sightseeing simulation can be found in lab_src/src/simulation repository.

## Getting Started

### Prerequisites

- Java JDK 11 or later
- Your favorite IDE or text editor

### Running the Examples

1. Clone the repository:
```bash
git clone https://your-repository-url-here.git](https://github.com/Floressek/concurrentLab_algorythms)
```

3. Navigate to the cloned directory:
```bash
cd java-synchronization-algorithms
```

3. Compile the Java files (example):
```bash
javac Zadanie_1/Dekker.java Zadanie_2/Peterson.java Zadanie_3/Lamport.java Zadanie_4/SemaphoreExample.java
```

4. Run the compiled Java classes (example):
```bash
java Zadanie_1.Dekker
java Zadanie_2.Peterson
java Zadanie_3.Lamport
java Zadanie_4.SemaphoreExample
```

## Contributing

Contributions are welcome! Please feel free to submit a pull request.

## License

This project is open-sourced under the MIT License. See the LICENSE file for more details.
