package com.github.pkovacs.aoc.y2024;

import java.util.ArrayDeque;
import java.util.LinkedHashSet;

import com.github.pkovacs.util.CharTable;
import com.github.pkovacs.util.Dir;
import com.github.pkovacs.util.Pos;

public class Day15 extends AbstractDay {

    public static void main(String[] args) {
        var input1 = readString(getInputPath());
        var input2 = input1.replace("#", "##").replace("O", "[]").replace(".", "..").replace("@", "@.");

        System.out.println("Part 1: " + solve(input1));
        System.out.println("Part 2: " + solve(input2));
    }

    private static long solve(String input) {
        var sections = collectSections(input);
        var table = new CharTable(sections.get(0));
        var moves = String.join("", sections.get(1));

        var pos = table.find('@');
        for (var move : moves.toCharArray()) {
            // Collect cells to move (the current position and some boxes)
            var dir = Dir.fromChar(move);
            var cells = collectCellsToMove(pos, dir, table);

            // Move the cells if each of them can be moved
            if (cells.stream().allMatch(p -> table.get(p.neighbor(dir)) != '#')) {
                cells.reversed().forEach(p -> {
                    table.set(p.neighbor(dir), table.get(p));
                    table.set(p, '.');
                });
                pos = pos.neighbor(dir);
            }
        }

        return table.cells()
                .filter(p -> table.get(p) == 'O' || table.get(p) == '[')
                .mapToLong(p -> p.y * 100L + p.x).sum();
    }

    private static LinkedHashSet<Pos> collectCellsToMove(Pos pos, Dir dir, CharTable table) {
        var cells = new LinkedHashSet<Pos>();
        var queue = new ArrayDeque<Pos>();
        queue.add(pos);
        while (!queue.isEmpty()) {
            var current = queue.removeFirst();
            cells.add(current);
            var next = current.neighbor(dir);
            if (table.get(next) != '.' && table.get(next) != '#') {
                queue.add(next);
                if (dir.isVertical() && table.get(next) == '[') {
                    queue.add(next.neighbor(Dir.E));
                } else if (dir.isVertical() && table.get(next) == ']') {
                    queue.add(next.neighbor(Dir.W));
                }
            }
        }
        return cells;
    }

}
