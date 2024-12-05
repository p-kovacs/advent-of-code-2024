package com.github.pkovacs.aoc.y2024;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.github.pkovacs.aoc.AbstractDay;

import static java.util.stream.Collectors.toSet;

public class Day05 extends AbstractDay {

    public static void main(String[] args) {
        var blocks = readLineBlocks(getInputPath());

        var rules = blocks.get(0).stream().map(s -> new Rule(parseInts(s))).collect(toSet());
        var updates = blocks.get(1).stream().map(s -> listOf(parseInts(s))).toList();

        long ans1 = 0;
        long ans2 = 0;
        for (var up : updates) {
            if (isCorrect(up, rules)) {
                ans1 += up.get(up.size() / 2);
            } else {
                var ordered = reorder(up, rules);
                ans2 += ordered.get(ordered.size() / 2);
            }
        }

        System.out.println("Part 1: " + ans1);
        System.out.println("Part 2: " + ans2);
    }

    private static boolean isCorrect(List<Integer> up, Set<Rule> rules) {
        return rules.stream().noneMatch(
                rule -> up.contains(rule.a) && up.contains(rule.b) && up.indexOf(rule.a) > up.indexOf(rule.b));
    }

    private static List<Integer> reorder(List<Integer> up, Set<Rule> rules) {
        var list = new ArrayList<>(up);
        var result = new ArrayList<Integer>();

        // Topological sorting (slow method, but it's fine for this puzzle)
        while (!list.isEmpty()) {
            var min = list.stream()
                    .filter(a -> list.stream().noneMatch(b -> rules.contains(new Rule(b, a))))
                    .findFirst().orElseThrow();
            list.remove(min);
            result.add(min);
        }

        return result;
    }

    private static record Rule(int a, int b) {
        Rule(int[] array) {
            this(array[0], array[1]);
        }
    }

}
