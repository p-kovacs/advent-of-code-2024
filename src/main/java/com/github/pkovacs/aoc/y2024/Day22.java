package com.github.pkovacs.aoc.y2024;

import java.util.Arrays;

public class Day22 extends AbstractDay {

    private static final int COUNT = 2000;
    private static final int BIT_MASK = 16777215;

    private static final int RADIX = 19;
    private static final int RADIX3 = RADIX * RADIX * RADIX;
    private static final int RADIX4 = RADIX * RADIX * RADIX * RADIX;

    public static void main(String[] args) {
        var seeds = readInts(getInputPath());

        System.out.println("Part 1: " + Arrays.stream(seeds).mapToLong(Day22::iterate).sum());
        System.out.println("Part 2: " + solve2(seeds));
    }

    private static int solve2(int[] seeds) {
        int[] counter = new int[RADIX4];
        for (var seed : seeds) {
            var prices = prices(seed);
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

    private static int iterate(int s) {
        for (int i = 0; i < COUNT; i++) {
            s = next(s);
        }
        return s;
    }

    private static int[] prices(int s) {
        int[] a = new int[COUNT + 1];
        a[0] = s % 10;
        for (int i = 1; i < a.length; i++) {
            s = next(s);
            a[i] = s % 10;
        }
        return a;
    }

    private static int next(int s) {
        s = (s ^ (s << 6)) & BIT_MASK;
        s = (s ^ (s >> 5)) & BIT_MASK;
        s = (s ^ (s << 11)) & BIT_MASK;
        return s;
    }

}
