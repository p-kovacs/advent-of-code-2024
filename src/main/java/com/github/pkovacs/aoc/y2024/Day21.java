package com.github.pkovacs.aoc.y2024;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.github.pkovacs.util.RegexUtils;
import com.github.pkovacs.util.data.CharTable;

public class Day21 extends AbstractDay {

    private static final CharTable numTable = new CharTable(List.of("789", "456", "123", ".0A"));
    private static final CharTable dirTable = new CharTable(List.of(".^A", "<v>"));

    public static void main(String[] args) {
        var lines = readLines(getInputPath());

        long ans1 = lines.stream().mapToLong(p -> calculateComplexity(p, 2)).sum();
        long ans2 = lines.stream().mapToLong(p -> calculateComplexity(p, 25)).sum();

        System.out.println("Part 1: " + ans1);
        System.out.println("Part 2: " + ans2);
    }

    private static long calculateComplexity(String path, int level) {
        return minLength(path, level + 1) * Long.parseLong(path.substring(0, path.length() - 1));
    }

    private static long minLength(String path, int level) {
        if (level == 0) {
            return path.length();
        }

        var key = new CacheKey(path, level);
        Long cached = cache.get(key);
        if (cached != null) {
            return cached;
        }

        // Convert the path once, split the results to segments ending with A, then convert each segment count-1
        // times recursively
        long minLength = convertPath(path).stream()
                .map(convPath -> RegexUtils.findAll("[^A]*A", convPath))
                .mapToLong(segments -> segments.stream().mapToLong(s -> minLength(s, level - 1)).sum())
                .min().orElseThrow();

        cache.put(key, minLength);
        return minLength;
    }

    /**
     * Converts the given path according to the corresponding keypad. The shortest paths between two buttons are
     * calculated by considering the only two reasonable orders of movements (X,Y and Y,X) and checking if they
     * avoid the gap.
     */
    private static List<String> convertPath(String path) {
        var table = Character.isDigit(path.charAt(0)) ? numTable : dirTable;
        var gap = table.find('.');

        var convertedPaths = List.of("");
        var pos = table.find('A');
        for (var ch : path.toCharArray()) {
            var next = table.find(ch);
            var xPath = (next.x() < pos.x() ? "<<" : ">>").substring(0, Math.abs(next.x() - pos.x()));
            var yPath = (next.y() < pos.y() ? "^^^" : "vvv").substring(0, Math.abs(next.y() - pos.y()));

            var segments = new HashSet<String>();
            if (!(pos.y() == gap.y() && next.x() == gap.x())) { // move in X,Y order if it avoids the gap
                segments.add(xPath + yPath + 'A');
            }
            if (!(pos.x() == gap.x() && next.y() == gap.y())) { // move in Y,X order if it avoids the gap
                segments.add(yPath + xPath + 'A');
            }

            convertedPaths = convertedPaths.stream()
                    .flatMap(convPath -> segments.stream().map(s -> convPath + s)).toList();
            pos = next;
        }
        return convertedPaths;
    }

    private static final Map<CacheKey, Long> cache = new HashMap<>();

    record CacheKey(String path, int count) {}

}
