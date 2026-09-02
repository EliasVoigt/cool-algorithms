package main.java.core.benchmarking.visualization;

import java.util.Arrays;
import java.util.OptionalInt;

public final class Formatter {
    private Formatter() {
    }

    public static String format(Object obj) {
        if (obj == null) {
            return "null";
        }
        return switch (obj) {
            case OptionalInt i -> i.isPresent() ? String.valueOf(i) : "none";
            case int[] arr -> Arrays.toString(arr);
            default -> obj.toString();
        };
    }
}