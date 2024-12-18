package com.github.pkovacs.aoc.y2024;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import com.github.pkovacs.util.alg.Bfs;
import com.github.pkovacs.util.alg.Path;
import com.github.pkovacs.util.data.IntTable;

public class Day18 extends AbstractDay {

    public static void main(String[] args) {
        var lines = readLines(getInputPath());

        var table = parseInput(lines);
        long ans1 = dist(table, 1024).orElseThrow();
        String ans2 = IntStream.range(1024, lines.size())
                .filter(t -> dist(table, t + 1).isEmpty())
                .mapToObj(lines::get).findFirst().orElseThrow();

        System.out.println("Part 1: " + ans1);
        System.out.println("Part 2: " + ans2);
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
