package com.github.pkovacs.aoc.y2024;

import java.util.List;
import java.util.stream.LongStream;

import com.github.pkovacs.aoc.AbstractDay;

public class Day07 extends AbstractDay {

    public static void main(String[] args) {
        var lines = readLines(getInputPath());

        System.out.println("Part 1: " + solve(lines, 1));
        System.out.println("Part 2: " + solve(lines, 2));
    }

    private static long solve(List<String> lines, int part) {
        long result = 0;
        for (var line : lines) {
            long target = Long.parseLong(line.split(": ")[0]);
            long[] params = parseLongs(line.split(": ")[1]);
            if (canMatch(target, params, part, 1, params[0])) {
                result += target;
            }
        }
        return result;
    }

    private static boolean canMatch(long target, long[] params, int part, int i, long result) {
        // Minor speed-up: each number is non-negative, so no operation can decrease the accumulated result
        if (result > target) {
            return false;
        }

        return i == params.length
                ? result == target
                : applyOperations(result, params[i], part).anyMatch(x -> canMatch(target, params, part, i + 1, x));
    }

    private static LongStream applyOperations(long a, long b, int part) {
        return part == 1
                ? LongStream.of(a + b, a * b)
                : LongStream.of(a + b, a * b, Long.parseLong(String.valueOf(a) + b));
    }

}
