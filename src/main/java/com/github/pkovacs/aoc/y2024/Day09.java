package com.github.pkovacs.aoc.y2024;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Day09 extends AbstractDay {

    public static void main(String[] args) {
        var input = readFirstLine(getInputPath());

        System.out.println("Part 1: " + solve(input, 1));
        System.out.println("Part 2: " + solve(input, 2));
    }

    private static long solve(String input, int part) {
        var disk = fillDisk(input);

        int[] searchFrom = new int[10];
        boolean wholeFiles = part == 2;
        for (int b = disk.size() - 1; b >= 0; b--) {
            int id = disk.get(b);
            if (id < 0) {
                continue;
            }

            // Find the chunk to move: a..b (in part 1, a == b)
            int a = b;
            while (wholeFiles && a - 1 >= 0 && disk.get(a - 1) == id) {
                a--;
            }
            int size = b - a + 1;

            // Move the chunk to the first free slot that is large enough (if any): i..j, where i < a
            for (int i = searchFrom[size]; i < a; i++) {
                if (disk.get(i) != -1) {
                    continue;
                }

                // Find the end of the free slot
                int j = i;
                while (j + 1 < disk.size() && disk.get(j + 1) == -1) {
                    j++;
                }

                // Move the chunk if the slot is large enough, continue the search otherwise
                if (j - i + 1 >= size) {
                    for (int k = 0; k < size; k++) {
                        disk.set(i + k, id);
                        disk.set(a + k, -1);
                    }
                    searchFrom[size] = i + size; // start search from here for the next chunk of the same size
                    break;
                } else {
                    i = j + 1;
                }
            }

            b = a;
        }

        return calculateChecksum(disk);
    }

    private static List<Integer> fillDisk(String input) {
        var disk = new ArrayList<Integer>();
        for (int i = 0; i < input.length(); i++) {
            int current = (i % 2 == 0) ? i / 2 : -1;
            IntStream.range(0, parseInt(input.charAt(i))).forEach(j -> disk.add(current));
        }
        return disk;
    }

    private static long calculateChecksum(List<Integer> disk) {
        return IntStream.range(0, disk.size()).mapToLong(i -> i * max(disk.get(i), 0L)).sum();
    }

}
