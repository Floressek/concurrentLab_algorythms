package src.simulation;

/**
 * Controllable interface for controlling the simulation.
 * This interface provides methods to pause, resume, terminate the simulation and check its status.
 */
public interface Controllable {

    /**
     * Pauses the simulation.
     * @throws InterruptedException if the simulation is interrupted while paused
     */
    void pause() throws InterruptedException;

    /**
     * Resumes the simulation.
     */
    void resume();

    /**
     * Checks if the simulation is paused.
     * @return true if the simulation is paused, false otherwise
     */
    boolean isPaused();

    /**
     * Terminates the simulation.
     */
    void terminate();

    /**
     * Checks if the simulation is terminated.
     * @return true if the simulation is terminated, false otherwise
     */
    boolean isTerminated();

}