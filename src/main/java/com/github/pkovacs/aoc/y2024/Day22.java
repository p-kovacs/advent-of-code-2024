package com.github.pkovacs.aoc.y2024;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Day22 extends AbstractDay {

    private static final int COUNT = 2000;
    private static final int BIT_MASK = 16777215;

    private static final int RADIX = 19;
    private static final int RADIX3 = (int) pow(RADIX, 3);
    private static final int RADIX4 = (int) pow(RADIX, 4);

    public static void main(String[] args) {
        var seeds = readInts(getInputPath());

        System.out.println("Part 1: " + solve1(seeds));
        System.out.println("Part 2: " + solve2(seeds));
    }

    private static long solve1(int[] seeds) {
        return Arrays.stream(seeds).mapToLong(seed -> next(seed, COUNT)).sum();
    }

    private static int solve2(int[] seeds) {
        int[] counter = new int[RADIX4];
        for (var seed : seeds) {
            var prices = IntStream.iterate(seed, Day22::next).map(i -> i % 10).limit(COUNT + 1).toArray();
            int diff = 0;
            var found = new boolean[RADIX4];
            for (int i = 1; i < prices.length; i++) {
                // Calculate the next 4-length difference sequence as an integer in radix 19
                diff = (diff % RADIX3) * RADIX + (prices[i] - prices[i - 1] + 9);
                if (i >= 4 && !found[diff]) {
                    counter[diff] += prices[i];
                    found[diff] = true;
                }
            }
        }
        return Arrays.stream(counter).max().orElseThrow();
    }

    private static int next(int s, int count) {
        for (int i = 0; i < count; i++) {
            s = next(s);
        }
        return s;
    }

    private static int next(int s) {
        s = (s ^ (s << 6)) & BIT_MASK;
        s = (s ^ (s >> 5)) & BIT_MASK;
        s = (s ^ (s << 11)) & BIT_MASK;
        return s;
    }

}
