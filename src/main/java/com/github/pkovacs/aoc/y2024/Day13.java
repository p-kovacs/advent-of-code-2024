package com.github.pkovacs.aoc.y2024;

import java.util.List;

public class Day13 extends AbstractDay {

    private static final long SHIFT = 10000000000000L;

    public static void main(String[] args) {
        var machines = readLineBlocks(getInputPath());

        var ans1 = machines.stream().mapToLong(m -> solveMachine(m, 1)).sum();
        var ans2 = machines.stream().mapToLong(m -> solveMachine(m, 2)).sum();

        System.out.println("Part 1: " + ans1);
        System.out.println("Part 2: " + ans2);
    }

    /**
     * Finds the solution for the given machine using a simple system of linear equations. The two buttons and
     * the target (prize) can be described by three position vectors in 2D space: a, b, and t. If we push button A
     * i times and button B j times, then these equations must hold:
     * <pre>
     * (1) i * a.x + j * b.x = t.x
     * (2) i * a.y + j * b.y = t.y
     * </pre>
     * From (1), we can get the formula {@code j = (t.x - i * a.x) / b.x}. Substituting this into (2), we get
     * {@code i = (b.x * t.y - b.y * t.x) / (a.y * b.x - a.x * b.y)}. If the prize can be reached, then both
     * i and j must be integers, so it's enough to calculate i and j this way using integer arithmetic and check
     * if (1) and (2) actually hold with the i and j values we got.
     * <p>
     * Of course, these formulas can only be applied if the denominators {@code b.x} and {@code a.y * b.x - a.x * b.y}
     * are not zero. We assume that the input files are generated this way. Otherwise, this method throws
     * {@link ArithmeticException}.
     */
    private static long solveMachine(List<String> lines, int part) {
        var a = Pos.parse(lines.get(0));
        var b = Pos.parse(lines.get(1));
        var t = Pos.parse(lines.get(2));
        if (part == 2) {
            t = new Pos(t.x + SHIFT, t.y + SHIFT);
        }

        long i = (b.x * t.y - b.y * t.x) / (a.y * b.x - a.x * b.y);
        long j = (t.x - i * a.x) / b.x;

        return (i * a.x + j * b.x == t.x) && (i * a.y + j * b.y == t.y) ? (i * 3 + j) : 0;
    }

    private record Pos(long x, long y) {

        static Pos parse(String line) {
            var v = parseLongs(line);
            return new Pos(v[0], v[1]);
        }

    }

}
