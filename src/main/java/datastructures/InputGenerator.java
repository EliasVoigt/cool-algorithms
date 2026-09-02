package main.java.datastructures;

public abstract class InputGenerator<I> {
    public abstract I generateInput();
    public abstract I generateInput(int size);
}
