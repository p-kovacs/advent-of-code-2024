package com.github.pkovacs.aoc.y2024;

import java.util.HashSet;
import java.util.Set;

import com.github.pkovacs.aoc.AbstractDay;
import com.github.pkovacs.util.data.Cell;
import com.github.pkovacs.util.data.CharTable;
import com.github.pkovacs.util.data.Direction;

import static java.util.stream.Collectors.toSet;

public class Day06 extends AbstractDay {

    public static void main(String[] args) {
        var lines = readLines(getInputPath());

        var table = new CharTable(lines);
        var start = table.find('^');

        var visited = patrol(table, start, null).stream().map(State::p).collect(toSet());
        long ans1 = visited.size();
        long ans2 = visited.stream().filter(b -> patrol(table, start, b) == null).count();

        System.out.println("Part 1: " + ans1);
        System.out.println("Part 2: " + ans2);
    }

    private static Set<State> patrol(CharTable table, Cell start, Cell block) {
        if (block != null && table.get(block) == '.') {
            table = new CharTable(table);
            table.set(block, '#');
        }

        var dir = Direction.NORTH;
        var pos = start;
        var states = new HashSet<State>();
        while (table.containsCell(pos)) {
            if (!states.add(new State(pos, dir))) {
                return null; // a loop is found
            }
            var next = pos.neighbor(dir);
            if (table.containsCell(next) && table.get(next) == '#') {
                dir = dir.rotateRight();
            } else {
                pos = next;
            }
        }

        return states;
    }

    private record State(Cell p, Direction d) {}

}
