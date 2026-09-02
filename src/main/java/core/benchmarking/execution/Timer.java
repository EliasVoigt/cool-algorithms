package main.java.core.benchmarking.execution;

public class Timer {
    private long startTime;
    private long endTime;
    private boolean running;

    public void start() {
        if (running) throw new IllegalStateException("Timer is already running");
        startTime = System.nanoTime();
        running = true;
    }

    public void stop() {
        if (!running) throw new IllegalStateException("Timer is not running");
        endTime = System.nanoTime();
        running = false;
    }

    public long elapsedNanos() {
        if (running) {
            return System.nanoTime() - startTime;
        }
        return endTime - startTime;
    }

    public double elapsedMillis() {
        return elapsedNanos() / 1_000_000.0;
    }

    public double elapsedSeconds() {
        return elapsedNanos() / 1_000_000_000.0;
    }
}
