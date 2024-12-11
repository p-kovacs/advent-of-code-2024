package com.github.pkovacs.aoc.y2024;

import java.util.Arrays;

import com.github.pkovacs.aoc.AbstractDay;
import com.github.pkovacs.util.data.CounterMap;

public class Day11 extends AbstractDay {

    public static void main(String[] args) {
        var values = readLongs(getInputPath());

        System.out.println("Part 1: " + solve(values, 25));
        System.out.println("Part 2: " + solve(values, 75));
    }

    private static long solve(long[] values, int blinkCount) {
        var stones = new CounterMap<Long>();
        Arrays.stream(values).forEach(stones::inc);
        for (int i = 0; i < blinkCount; i++) {
            var next = new CounterMap<Long>();
            for (long k : stones.keySet()) {
                long count = stones.get(k);
                String s = String.valueOf(k);
                if (k == 0) {
                    next.add(1L, count);
                } else if (s.length() % 2 == 0) {
                    next.add(Long.parseLong(s.substring(0, s.length() / 2)), count);
                    next.add(Long.parseLong(s.substring(s.length() / 2)), count);
                } else {
                    next.add(k * 2024, count);
                }
            }
            stones = next;
        }
        return stones.sum();
    }

}
