package com.github.pkovacs.aoc.y2024;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

public class Day23 extends AbstractDay {

    public static void main(String[] args) {
        var lines = readLines(getInputPath());

        var graph = buildGraph(lines);
        var nodes = graph.keySet().stream().filter(node -> node.charAt(0) == 't').toList();
        var cliques = nodes.stream().flatMap(node -> findCliques(graph, node).stream()).collect(toSet());
        var maxClique = cliques.stream().max(comparing(Set::size)).orElseThrow();

        long ans1 = cliques.stream().filter(c -> c.size() == 3).count();
        String ans2 = maxClique.stream().sorted().collect(joining(","));

        System.out.println("Part 1: " + ans1);
        System.out.println("Part 2: " + ans2);
    }

    private static Map<String, Set<String>> buildGraph(List<String> lines) {
        var graph = new HashMap<String, Set<String>>();
        for (var line : lines) {
            var a = line.substring(0, 2);
            var b = line.substring(3, 5);
            graph.putIfAbsent(a, new HashSet<>());
            graph.putIfAbsent(b, new HashSet<>());
            graph.get(a).add(b);
            graph.get(b).add(a);
        }
        return graph;
    }

    private static Set<Set<String>> findCliques(Map<String, Set<String>> graph, String node) {
        var result = new HashSet<Set<String>>();
        var queue = new ArrayDeque<Set<String>>();

        result.add(Set.of(node));
        queue.add(Set.of(node));
        while (!queue.isEmpty()) {
            var clique = queue.remove();
            for (var next : graph.get(node)) {
                if (!clique.contains(next) && graph.get(next).containsAll(clique)) {
                    var newClique = new HashSet<>(clique);
                    newClique.add(next);
                    if (result.add(newClique)) {
                        queue.add(newClique);
                    }
                }
            }
        }

        return result;
    }

}
