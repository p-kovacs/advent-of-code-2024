package com.github.pkovacs.aoc.y2024;

import java.util.List;

import com.github.pkovacs.aoc.AbstractDay;
import com.github.pkovacs.util.data.Cell;
import com.github.pkovacs.util.data.CharTable;

public class Day04 extends AbstractDay {

    public static void main(String[] args) {
        var table = new CharTable(readLines(getInputPath()));

        System.out.println("Part 1: " + table.cells().mapToLong(p -> countXmasRays(table, p)).sum());
        System.out.println("Part 2: " + table.cells().filter(p -> isXmasCenter(table, p)).count());
    }

    private static long countXmasRays(CharTable table, Cell p) {
        return table.get(p) != 'X'
                ? 0
                : table.extendedNeighbors(p)
                        .map(n -> table.ray(p, n).map(table::get).limit(3).toList())
                        .filter(List.of('M', 'A', 'S')::equals)
                        .count();
    }

    private static boolean isXmasCenter(CharTable table, Cell p) {
        if (table.get(p) != 'A') {
            return false;
        }

        var diagonalNeighbors = table.extendedNeighbors(p).filter(n -> n.x() != p.x() && n.y() != p.y()).toList();
        return diagonalNeighbors.size() == 4 && diagonalNeighbors.stream().allMatch(n ->
                (table.get(n) == 'M' || table.get(n) == 'S') && table.get(oppositeNeighbor(p, n)) != table.get(n));
    }

    private static Cell oppositeNeighbor(Cell p, Cell neighbor) {
        return new Cell(p.y() + p.y() - neighbor.y(), p.x() + p.x() - neighbor.x());
    }

}
