package core;

public abstract class Problem<I, R> {
    public abstract I generateInput();

    public abstract boolean isCorrect(I input, R result);

    public abstract String formatInput(I input);

    public abstract String formatResult(R result);

    public abstract String getName();
}
