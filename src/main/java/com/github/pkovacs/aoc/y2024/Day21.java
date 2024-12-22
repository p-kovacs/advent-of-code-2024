package com.github.pkovacs.aoc.y2024;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pkovacs.util.data.CharTable;

public class Day21 extends AbstractDay {

    private static final CharTable numTable = new CharTable(List.of("789", "456", "123", ".0A"));
    private static final CharTable dirTable = new CharTable(List.of(".^A", "<v>"));

    public static void main(String[] args) {
        var lines = readLines(getInputPath());

        System.out.println("Part 1: " + solve(lines, 3));
        System.out.println("Part 2: " + solve(lines, 26));
    }

    private static long solve(List<String> lines, int level) {
        return lines.stream()
                .mapToLong(p -> minLength(p, level) * Long.parseLong(p.substring(0, p.length() - 1))).sum();
    }

    private static long minLength(String path, int level) {
        if (level == 0) {
            return path.length();
        }

        var key = new CacheKey(path, level);
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        var chars = ('A' + path).toCharArray();
        long minLength = IntStream.range(0, chars.length - 1)
                .mapToLong(i -> getPaths(chars[i], chars[i + 1]).stream()
                        .mapToLong(s -> minLength(s, level - 1)).min().orElseThrow()).sum();

        cache.put(key, minLength);
        return minLength;
    }

    /**
     * Returns the shortest paths between the given two buttons. Only two orders of movements (X,Y and Y,X) are
     * considered, as other paths surely become suboptimal on the next level.
     */
    private static List<String> getPaths(char ch1, char ch2) {
        var table = Character.isDigit(ch1) || Character.isDigit(ch2) ? numTable : dirTable;

        var a = table.find(ch1);
        var b = table.find(ch2);
        var gap = table.find('.');
        var xPath = (b.x() < a.x() ? "<<<" : ">>>").substring(0, Math.abs(b.x() - a.x()));
        var yPath = (b.y() < a.y() ? "^^^" : "vvv").substring(0, Math.abs(b.y() - a.y()));

        return Stream.of(
                (gap.x() == b.x() && gap.y() == a.y()) ? null : (xPath + yPath + 'A'),
                (gap.x() == a.x() && gap.y() == b.y()) ? null : (yPath + xPath + 'A')
        ).filter(Objects::nonNull).distinct().toList();
    }

    private static final Map<CacheKey, Long> cache = new HashMap<>();

    record CacheKey(String path, int count) {}

}
