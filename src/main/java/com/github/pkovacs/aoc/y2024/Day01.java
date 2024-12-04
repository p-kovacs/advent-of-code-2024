package com.github.pkovacs.aoc.y2024;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.IntStream;

import com.github.pkovacs.aoc.AbstractDay;

public class Day01 extends AbstractDay {

    public static void main(String[] args) {
        var lines = readLines(getInputPath());

        var left = new ArrayList<Long>();
        var right = new ArrayList<Long>();
        for (var line : lines) {
            long[] a = parseLongs(line);
            left.add(a[0]);
            right.add(a[1]);
        }

        Collections.sort(left);
        Collections.sort(right);

        long ans1 = IntStream.range(0, left.size()).mapToLong(i -> Math.abs(right.get(i) - left.get(i))).sum();
        long ans2 = left.stream().mapToLong(i -> i * right.stream().filter(i::equals).count()).sum();

        System.out.println("Part 1: " + ans1);
        System.out.println("Part 2: " + ans2);
    }

}
