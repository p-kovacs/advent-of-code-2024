package com.github.pkovacs.aoc.y2024;

import java.util.stream.IntStream;

import com.github.pkovacs.util.Utils;

public class Day02 extends AbstractDay {

    public static void main(String[] args) {
        var lines = readLines(getInputPath());
        var reports = lines.stream().map(Utils::parseInts).toList();

        long ans1 = reports.stream().filter(Day02::isSafe).count();
        long ans2 = reports.stream()
                .filter(report -> IntStream.range(0, report.length).anyMatch(i -> isSafe(remove(report, i))))
                .count();

        System.out.println("Part 1: " + ans1);
        System.out.println("Part 2: " + ans2);
    }

    private static boolean isSafe(int[] a) {
        return IntStream.range(0, a.length - 1).allMatch(i -> a[i + 1] > a[i] && a[i + 1] <= a[i] + 3)
                || IntStream.range(0, a.length - 1).allMatch(i -> a[i + 1] < a[i] && a[i + 1] >= a[i] - 3);
    }

    private static int[] remove(int[] a, int index) {
        var result = new int[a.length - 1];
        System.arraycopy(a, 0, result, 0, index);
        System.arraycopy(a, index + 1, result, index, a.length - index - 1);
        return result;
    }

}
