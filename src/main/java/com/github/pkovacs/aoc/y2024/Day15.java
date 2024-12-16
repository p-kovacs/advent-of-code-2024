package com.github.pkovacs.aoc.y2024;

import java.util.ArrayDeque;
import java.util.LinkedHashSet;

import com.github.pkovacs.util.data.Cell;
import com.github.pkovacs.util.data.CharTable;
import com.github.pkovacs.util.data.Direction;

public class Day15 extends AbstractDay {

    public static void main(String[] args) {
        var input1 = readString(getInputPath());
        var input2 = input1.replace("#", "##").replace("O", "[]").replace(".", "..").replace("@", "@.");

        System.out.println("Part 1: " + solve(input1));
        System.out.println("Part 2: " + solve(input2));
    }

    private static long solve(String input) {
        var blocks = collectLineBlocks(input);
        var table = new CharTable(blocks.get(0));
        var moves = String.join("", blocks.get(1));

        var pos = table.find('@');
        for (var move : moves.toCharArray()) {
            // Collect cells to move (the current position and some boxes)
            var dir = Direction.fromChar(move);
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
                .mapToLong(p -> p.row() * 100L + p.col()).sum();
    }

    private static LinkedHashSet<Cell> collectCellsToMove(Cell pos, Direction dir, CharTable table) {
        var cells = new LinkedHashSet<Cell>();
        var queue = new ArrayDeque<Cell>();
        queue.add(pos);
        while (!queue.isEmpty()) {
            var current = queue.removeFirst();
            cells.add(current);
            var next = current.neighbor(dir);
            if (table.get(next) != '.' && table.get(next) != '#') {
                queue.add(next);
                if (dir.isVertical() && table.get(next) == '[') {
                    queue.add(next.neighbor(Direction.EAST));
                } else if (dir.isVertical() && table.get(next) == ']') {
                    queue.add(next.neighbor(Direction.WEST));
                }
            }
        }
        return cells;
    }

}
