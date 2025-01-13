package com.github.pkovacs.aoc.y2024;

import java.util.List;

import com.github.pkovacs.util.Bfs;
import com.github.pkovacs.util.Box;
import com.github.pkovacs.util.CharTable;
import com.github.pkovacs.util.Range;

public class Day20 extends AbstractDay {

    public static void main(String[] args) {
        var lines = readLines(getInputPath());

        System.out.println("Part 1: " + solve(lines, 2));
        System.out.println("Part 2: " + solve(lines, 20));
    }

    private static long solve(List<String> lines, int cheatTime) {
        var table = new CharTable(lines);
        var start = table.find('S');
        var end = table.find('E');

        var paths = Bfs.findPaths(table.graph(c -> c != '#'), start);
        var revPaths = Bfs.findPaths(table.graph(c -> c != '#'), end);
        long totalDist = paths.get(end).dist();

        var cheats = new Box(new Range(-cheatTime, cheatTime), new Range(-cheatTime, cheatTime)).stream()
                .filter(p -> p.dist1() <= cheatTime).toList();

        return paths.keySet().stream().mapToLong(from -> {
            // From the current position, apply each potential cheat (as a "jump"), check if we end up at a
            // free position reachable from the end, calculate the saved time, and then aggregate the results
            long distFrom = paths.get(from).dist();
            return cheats.stream()
                    .map(from::plus)
                    .filter(revPaths::containsKey)
                    .mapToLong(to -> totalDist - (distFrom + from.dist1(to) + revPaths.get(to).dist()))
                    .filter(saved -> saved >= 100)
                    .count();
        }).sum();
    }

}
