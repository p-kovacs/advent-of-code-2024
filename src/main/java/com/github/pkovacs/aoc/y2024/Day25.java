package com.github.pkovacs.aoc.y2024;

import java.util.stream.IntStream;

import com.github.pkovacs.util.CharTable;

public class Day25 extends AbstractDay {

    public static void main(String[] args) {
        var sections = readSections(getInputPath());

        var tables = sections.stream().map(CharTable::new).toList();
        var locks = tables.stream().filter(t -> t.get(0, 0) == '#').map(Day25::toNumbers).toList();
        var keys = tables.stream().filter(t -> t.get(0, 0) == '.').map(Day25::toNumbers).toList();

        long ans = locks.stream().mapToLong(lock -> keys.stream().filter(key -> match(lock, key)).count()).sum();

        System.out.println("Part 1: " + ans);
        System.out.println("Part 2: " + 0);
    }

    private static int[] toNumbers(CharTable table) {
        return IntStream.range(0, table.width())
                .map(i -> (int) table.colValues(i).filter(c -> c == '#').count() - 1).toArray();
    }

    private static boolean match(int[] lock, int[] key) {
        return IntStream.range(0, lock.length).allMatch(i -> lock[i] + key[i] <= 5);
    }

}
