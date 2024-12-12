package com.github.pkovacs.aoc.y2024;

import com.github.pkovacs.util.RegexUtils;

public class Day03 extends AbstractDay {

    public static void main(String[] args) {
        var input = readString(getInputPath());

        var instructions = RegexUtils.findAll("(mul[(][0-9]{1,3},[0-9]{1,3}[)]|do[(][)]|don't[(][)])", input);

        long ans1 = 0;
        long ans2 = 0;
        boolean enabled = true;
        for (var instruction : instructions) {
            switch (instruction) {
                case "do()" -> enabled = true;
                case "don't()" -> enabled = false;
                default -> {
                    long[] op = parseLongs(instruction);
                    ans1 += op[0] * op[1];
                    if (enabled) {
                        ans2 += op[0] * op[1];
                    }
                }
            }
        }

        System.out.println("Part 1: " + ans1);
        System.out.println("Part 2: " + ans2);
    }

}
