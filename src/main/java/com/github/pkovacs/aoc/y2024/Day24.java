package com.github.pkovacs.aoc.y2024;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Day24 extends AbstractDay {

    public static void main(String[] args) {
        var input = readString(getInputPath());

        System.out.println("Part 1: " + solve1(input));
        System.out.println("Part 2: " + solve2(input));
    }

    /**
     * Solves part 1. Slow method, but it's fine for this puzzle.
     */
    private static long solve1(String input) {
        var sections = collectSections(input);
        var gates = sections.get(1).stream().map(Gate::parse).toList();

        var values = new HashMap<String, Integer>();
        for (var line : sections.get(0)) {
            var parts = line.split(": ");
            values.put(parts[0], parseInt(parts[1]));
        }

        var queue = new ArrayDeque<Gate>();
        gates.stream().filter(g -> values.containsKey(g.left) && values.containsKey(g.right)).forEach(queue::add);
        while (!queue.isEmpty()) {
            var gate = queue.poll();
            values.put(gate.result, gate.evaluate(values));
            gates.stream().filter(g -> (g.left.equals(gate.result) && values.containsKey(g.right))
                    || (g.right.equals(gate.result) && values.containsKey(g.left))).forEach(queue::add);
        }

        return values.keySet().stream().filter(s -> s.charAt(0) == 'z').sorted(Comparator.reverseOrder())
                .mapToLong(values::get).reduce(0, (a, b) -> (a << 1) | b);
    }

    /**
     * Solves part 2. This method heavily exploits particular assumptions about the input.
     * <p>
     * If we inspect the input file, we can realize that the expected system of the gates is the following.
     * In fact, this kind of logical circuit is known as a
     * <a href="https://en.wikipedia.org/wiki/Adder_(electronics)#Ripple-carry_adder">Ripple-carry adder</a>.
     * <pre>
     *     x[0] XOR y[0]   -> z[0]
     *     x[0] AND y[0]   -> C[0]
     *     ...
     *     x[i] XOR y[i]   -> A[i]    (1)
     *     x[i] AND y[i]   -> B[i]    (2)
     *     C[i-1] XOR A[i] -> z[i]    (3)
     *     C[i-1] AND A[i] -> D[i]    (4)
     *     D[i] OR B[i]    -> C[i]    (5)
     *     ...
     * </pre>
     * x[i], y[i], z[i] denote the wires x.., y.., z.. (x[0] denotes x00, x[1] denotes x1, and so on).
     * A[i], B[i], C[i], D[i] represent wires with arbitrary names. C[i] denote the "carry" bits, the last one
     * actually equals to the last z wire.
     * <p>
     * Furthermore, we make the assumption that each swap either involves a gate of type (3) for which the
     * corresponding gate (1) is correct; or it swaps the output of gates (1) and (2) for the same index i.
     * Multiple input files fulfill this assumption, so it seems sufficient to restrict the solution to checking
     * only such swaps, which greatly simplifies the code compared to a (more) general method.
     */
    private static String solve2(String input) {
        var sections = collectSections(input);
        var gates = new ArrayList<>(sections.get(1).stream().map(Gate::parse).toList());

        var swapped = new ArrayList<String>();
        for (int i = 1, count = sections.get(0).size() / 2; i < count; i++) {
            var x = String.format("x%02d", i);
            var z = String.format("z%02d", i);
            var gate1 = find(gates, Operator.XOR, x).orElseThrow();
            var gate2 = find(gates, Operator.AND, x).orElseThrow();
            var gate3 = find(gates, Operator.XOR, gate1.result);

            if (gate3.isPresent() && !gate3.get().result.equals(z)) {
                // The output of gate3 is swapped with another gate
                swapped.add(gate3.get().result);
                swapped.add(z);
            } else if (find(gates, Operator.OR, gate2.result).isEmpty()) {
                // The outputs of gate1 and gate2 are swapped
                swapped.add(gate1.result);
                swapped.add(gate2.result);
            }
        }

        return swapped.stream().sorted().collect(Collectors.joining(","));
    }

    private static Optional<Gate> find(List<Gate> gates, Operator op, String wire) {
        return gates.stream().filter(g -> op == g.op && (g.left.equals(wire) || g.right.equals(wire))).findFirst();
    }

    private enum Operator { XOR, AND, OR }

    private record Gate(Operator op, String left, String right, String result) {

        static Gate parse(String line) {
            var parts = line.split(" ");
            return new Gate(Operator.valueOf(parts[1]), parts[0], parts[2], parts[4]);
        }

        int evaluate(Map<String, Integer> values) {
            return switch (op) {
                case XOR -> values.get(left) ^ values.get(right);
                case AND -> values.get(left) & values.get(right);
                case OR -> values.get(left) | values.get(right);
            };
        }

    }

}
