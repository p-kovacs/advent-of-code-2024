package com.github.pkovacs.aoc.y2024;

import java.util.List;
import java.util.Optional;

import com.github.pkovacs.util.alg.Bfs;
import com.github.pkovacs.util.alg.Path;
import com.github.pkovacs.util.data.IntTable;

public class Day18 extends AbstractDay {

    public static void main(String[] args) {
        var lines = readLines(getInputPath());
        var table = parseInput(lines);

        System.out.println("Part 1: " + dist(table, 1024).orElseThrow());
        System.out.println("Part 2: " + solve2(lines, table));
    }

    /** Solves part 2. Binary search is used to make it faster. */
    private static String solve2(List<String> lines, IntTable table) {
        int low = 1024;
        int high = lines.size() - 1;
        while (low < high) {
            int mid = (low + high) / 2;
            if (dist(table, mid + 1).isEmpty()) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return lines.get(low);
    }

    private static Optional<Long> dist(IntTable table, int time) {
        return Bfs.findPath(table.topLeft(), p -> table.neighbors(p).filter(n -> table.get(n) >= time).toList(),
                table.bottomRight()::equals).map(Path::dist);
    }

    private static IntTable parseInput(List<String> lines) {
        var table = new IntTable(71, 71, Integer.MAX_VALUE);
        for (int i = 0; i < lines.size(); i++) {
            var ints = parseInts(lines.get(i));
            table.set(ints[0], ints[1], i);
        }
        return table;
    }

}
