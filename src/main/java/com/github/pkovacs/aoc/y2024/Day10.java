package com.github.pkovacs.aoc.y2024;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import com.github.pkovacs.aoc.AbstractDay;
import com.github.pkovacs.util.data.Cell;
import com.github.pkovacs.util.data.CharTable;

public class Day10 extends AbstractDay {

    public static void main(String[] args) {
        var table = new CharTable(readLines(getInputPath()));

        System.out.println("Part 1: " + solve(table, 1));
        System.out.println("Part 2: " + solve(table, 2));
    }

    private static long solve(CharTable table, int part) {
        return table.findAll('0').mapToLong(head -> countTrails(table, head, part)).sum();
    }

    private static long countTrails(CharTable table, Cell head, int part) {
        var trails = new ArrayList<List<Cell>>();
        var queue = new ArrayDeque<List<Cell>>();
        queue.add(List.of(head));
        while (!queue.isEmpty()) {
            var p = queue.removeFirst();
            var last = table.get(p.getLast());
            if (last == '9') {
                trails.add(p);
            } else {
                table.neighbors(p.getLast()).filter(n -> table.get(n) - last == 1)
                        .map(n -> extend(p, n))
                        .forEach(queue::add);
            }
        }
        return part == 1 ? trails.stream().map(List::getLast).distinct().count() : trails.size();
    }

    private static <T> List<T> extend(List<T> list, T element) {
        var result = new ArrayList<>(list);
        result.add(element);
        return result;
    }

}
