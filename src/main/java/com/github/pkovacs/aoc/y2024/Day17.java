package com.github.pkovacs.aoc.y2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day17 extends AbstractDay {

    public static void main(String[] args) {
        var lines = readLines(getInputPath());

        long a = parseLongs(lines.get(0))[0];
        var prg = Arrays.stream(parseInts(lines.get(4))).boxed().toList();

        System.out.println("Part 1: " + solve1(prg, a));
        System.out.println("Part 2: " + solve2(prg));
    }

    private static String solve1(List<Integer> prg, long a) {
        return run(prg, a).stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    /**
     * Solves part 2 exploiting the particular behavior of the input program.
     * <p>
     * The program is a simple loop. In each iteration, a number is printed based on the current value of register A,
     * then this value is divided by 8 (i.e., the last 3 bits are removed). The loop terminates when the value of
     * register A becomes zero. Therefore, the last number of the output only depends on the first few (at most 3)
     * bits of the initial value of register A, and 3 additional bits are required for each preceding number.
     * <p>
     * Based on this, we collect the candidates for the initial value of register A for suffixes of increasing size
     * of the expected output (the program code).
     */
    private static long solve2(List<Integer> prg) {
        var candidates = List.of(0L);
        for (int size = 1; size <= prg.size(); size++) {
            var suffix = prg.subList(prg.size() - size, prg.size());
            candidates = candidates.stream()
                    .flatMap(a -> LongStream.range(0, 8).map(i -> (a << 3) | i).boxed()) // add 3 more bits
                    .filter(a -> run(prg, a).equals(suffix))
                    .toList();
        }
        return candidates.getFirst();
    }

    /**
     * Runs the given program with the given initial value of register A.
     * The initial values of registers B and C are not relevant (they are always 0 anyway).
     */
    private static List<Integer> run(List<Integer> prg, long a) {
        long b = 0;
        long c = 0;
        int ip = 0;
        var out = new ArrayList<Integer>();
        while (ip < prg.size()) {
            int op = prg.get(ip + 1);
            long combo = switch (op) {
                case 4 -> a;
                case 5 -> b;
                case 6 -> c;
                default -> op;
            };
            switch (prg.get(ip)) {
                case 0 -> a = a >> combo; // adv
                case 1 -> b = b ^ op; // bxl
                case 2 -> b = combo & 0x7; // bst
                case 3 -> { // jnz
                    if (a != 0) {
                        ip = op;
                        continue;
                    }
                }
                case 4 -> b = b ^ c; // bxc
                case 5 -> out.add((int) (combo & 0x7)); // out
                case 6 -> b = a >> combo; // bdv
                case 7 -> c = a >> combo; // cdv
                default -> throw new IllegalArgumentException();
            }
            ip += 2;
        }
        return out;
    }

}
