package bavteqdoit.carhealthcheck.model;

import java.util.function.Function;

public record SidePair(
        String label,
        Function<PaintCheck, Integer> leftMin,
        Function<PaintCheck, Integer> leftMax,
        Function<PaintCheck, Integer> rightMin,
        Function<PaintCheck, Integer> rightMax
) {}
