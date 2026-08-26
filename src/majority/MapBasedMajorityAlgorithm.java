package majority;

import core.Algorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;

public class MapBasedMajorityAlgorithm extends Algorithm<int[], OptionalInt> {
    @Override
    public OptionalInt apply(int[] input) {
        Map<Integer, Integer> countMap = new HashMap<>();

        for (int i : input) {
            countMap.merge(i, 1, Integer::sum);
        }

        int majorityThreshold = input.length / 2;
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > majorityThreshold) {
                return OptionalInt.of(entry.getKey());
            }
        }

        return OptionalInt.empty();
    }

    @Override
    public String toString() {
        return "Map Based Majority Algorithm";
    }
}
