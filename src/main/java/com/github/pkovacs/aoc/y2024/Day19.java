package com.github.pkovacs.aoc.y2024;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day19 extends AbstractDay {

    public static void main(String[] args) {
        var lines = readLines(getInputPath());

        var towels = Arrays.stream(lines.get(0).split(", ")).toList();
        var designs = lines.stream().skip(2).toList();

        long ans1 = designs.stream().filter(s -> countPossible(towels, s) > 0).count();
        long ans2 = designs.stream().mapToLong(s -> countPossible(towels, s)).sum();

        System.out.println("Part 1: " + ans1);
        System.out.println("Part 2: " + ans2);
    }

    private static final Map<String, Long> cache = new HashMap<>();

    private static long countPossible(List<String> towels, String str) {
        if (str.isEmpty()) {
            return 1;
        } else if (cache.containsKey(str)) {
            return cache.get(str);
        }

        long result = towels.stream().filter(str::startsWith)
                .mapToLong(t -> countPossible(towels, str.substring(t.length()))).sum();
        cache.put(str, result);
        return result;
    }

}
