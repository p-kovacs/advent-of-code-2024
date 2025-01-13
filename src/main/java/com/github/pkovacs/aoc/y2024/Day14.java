package com.github.pkovacs.aoc.y2024;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import com.github.pkovacs.util.Pos;

public class Day14 extends AbstractDay {

    private static final int WIDTH = 101;
    private static final int HEIGHT = 103;

    /**
     * The suspected minimum length of a continuous horizontal line in the Christmas tree we search for in part 2.
     * For my input, the actual line length is 21 for the tree itself and 31 for the border.
     */
    private static final int MIN_TREE_LINE_LENGTH = 15;

    public static void main(String[] args) {
        var lines = readLines(getInputPath());

        var robots = lines.stream().map(Robot::parse).toList();

        System.out.println("Part 1: " + solve1(robots));
        System.out.println("Part 2: " + solve2(robots));
    }

    private static long solve1(List<Robot> robots) {
        var positions = robots.stream().map(r -> r.getPos(100)).toList();

        long[] c = new long[4];
        for (var p : positions) {
            if (p.x != WIDTH / 2 && p.y != HEIGHT / 2) {
                int quadrant = (p.x > WIDTH / 2 ? 1 : 0) + (p.y > HEIGHT / 2 ? 2 : 0);
                c[quadrant]++;
            }
        }
        return c[0] * c[1] * c[2] * c[3];
    }

    private static int solve2(List<Robot> robots) {
        return IntStream.range(0, Integer.MAX_VALUE).filter(t -> xmasTreeFound(robots, t)).findFirst().orElseThrow();
    }

    /**
     * Checks if the robots are arranged into a picture of a Christmas tree after the given amount of seconds.
     * Well, the puzzle description doesn't contain enough information about what kind of picture we search for,
     * but one might suspect that a relatively long continuous horizontal line should be included in it. So this
     * method simply checks this condition.
     */
    private static boolean xmasTreeFound(List<Robot> robots, int time) {
        var positions = robots.stream().map(r -> r.getPos(time))
                .sorted(Comparator.comparing(p -> p.y * 1000 + p.x)).toList();
        return IntStream.range(0, positions.size() - MIN_TREE_LINE_LENGTH).anyMatch(i ->
                positions.get(i + MIN_TREE_LINE_LENGTH).equals(positions.get(i).plus(MIN_TREE_LINE_LENGTH, 0)));
    }

    private record Robot(Pos p, Pos v) {

        static Robot parse(String line) {
            var c = parseInts(line);
            return new Robot(new Pos(c[0], c[1]), new Pos(c[2], c[3]));
        }

        Pos getPos(int time) {
            return new Pos(wrapIndex(p.x + v.x * time, WIDTH), wrapIndex(p.y + v.y * time, HEIGHT));
        }

    }

}
