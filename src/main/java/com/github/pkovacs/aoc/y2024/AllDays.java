package com.github.pkovacs.aoc.y2024;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;

/**
 * Verifies the solution for each day against the expected answers for my puzzle input files.
 */
public class AllDays {

    private static final List<Day> DAYS = List.of(
            new Day("Day 01", Day01::main, "1388114", "23529853"),
            new Day("Day 02", Day02::main, "220", "296"),
            new Day("Day 03", Day03::main, "173529487", "99532691"),
            new Day("Day 04", Day04::main, "2562", "1902"),
            new Day("Day 05", Day05::main, "4689", "6336"),
            new Day("Day 06", Day06::main, "5531", "2165"),
            new Day("Day 07", Day07::main, "2437272016585", "162987117690649"),
            new Day("Day 08", Day08::main, "367", "1285"),
            new Day("Day 09", Day09::main, "6301895872542", "6323761685944"),
            new Day("Day 10", Day10::main, "796", "1942"),
            new Day("Day 11", Day11::main, "183484", "218817038947400"),
            new Day("Day 12", Day12::main, "1431316", "821428"),
            new Day("Day 13", Day13::main, "37297", "83197086729371"),
            new Day("Day 14", Day14::main, "231782040", "6475"),
            new Day("Day 15", Day15::main, "1463715", "1481392"),
            new Day("Day 16", Day16::main, "72400", "435"),
            new Day("Day 17", Day17::main, "2,1,0,4,6,2,4,2,0", "109685330781408"),
            new Day("Day 18", Day18::main, "374", "30,12"),
            new Day("Day 19", Day19::main, "293", "623924810770264"),
            new Day("Day 20", Day20::main, "1530", "1033983"),
//                new Day("Day 21", Day21::main, "0", "0"),
//                new Day("Day 22", Day22::main, "0", "0"),
//                new Day("Day 23", Day23::main, "0", "0"),
//                new Day("Day 24", Day24::main, "0", "0"),
//                new Day("Day 25", Day25::main, "0", "0")
            new Day("The End", null, null, null)
    );

    public static void main(String[] args) {
        String format = "%-12s%-8s%-8s%8s%n";
        System.out.printf(format, "Day", "Part 1", "Part 2", "Time");

        DAYS.stream().filter(day -> day.mainMethod != null).forEach(day -> {
            long start = System.nanoTime();
            var results = runDay(day);
            long time = (System.nanoTime() - start) / 1_000_000L;

            System.out.printf(format, day.name, evaluate(day, results, 0), evaluate(day, results, 1), time + " ms");
        });
    }

    private static String evaluate(Day day, List<String> results, int index) {
        var expected = index == 0 ? day.expected1 : day.expected2;
        return results.size() == 2 && expected.equals(results.get(index)) ? "\u2714" : "FAILED";
    }

    private static List<String> runDay(Day day) {
        var origOut = System.out;
        try {
            var out = new ByteArrayOutputStream(200);
            System.setOut(new PrintStream(out));
            day.mainMethod.accept(null);
            return out.toString(StandardCharsets.UTF_8).lines().map(l -> l.split(": ")[1]).toList();
        } catch (Exception e) {
            return List.of();
        } finally {
            System.setOut(origOut);
        }
    }

    private record Day(String name, Consumer<String[]> mainMethod, String expected1, String expected2) {}

}
