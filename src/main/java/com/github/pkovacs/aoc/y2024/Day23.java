package com.github.pkovacs.aoc.y2024;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;

public class Day23 extends AbstractDay {

    public static void main(String[] args) {
        var lines = readLines(getInputPath());
        var graph = buildGraph(lines);

        System.out.println("Part 1: " + solve1(graph));
        System.out.println("Part 2: " + solve2(graph));
    }

    private static Map<String, Set<String>> buildGraph(List<String> lines) {
        var graph = new HashMap<String, Set<String>>();
        for (var line : lines) {
            var a = line.substring(0, 2);
            var b = line.substring(3, 5);
            graph.computeIfAbsent(a, k -> new HashSet<>()).add(b);
            graph.computeIfAbsent(b, k -> new HashSet<>()).add(a);
        }
        return graph;
    }

    private static int solve1(Map<String, Set<String>> graph) {
        var cliques = new HashSet<Set<String>>();
        graph.keySet().stream().filter(a -> a.charAt(0) == 't').forEach(a ->
                graph.get(a).forEach(b -> graph.get(a).stream().filter(graph.get(b)::contains)
                        .map(c -> Set.of(a, b, c)).forEach(cliques::add))
        );
        return cliques.size();
    }

    private static String solve2(Map<String, Set<String>> graph) {
        var maxClique = findMaximalCliques(graph).stream().max(comparing(List::size)).orElseThrow();
        return maxClique.stream().sorted().collect(joining(","));
    }

    /**
     * Finds all maximal cliques using the
     * <a href="https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm">Bron-Kerbosch algorithm</a>.
     */
    private static <T> List<List<T>> findMaximalCliques(Map<T, ? extends Collection<T>> graph) {
        var results = new ArrayList<List<T>>();
        bronKerbosch(graph, results, new ArrayList<>(), new ArrayList<>(graph.keySet()), new ArrayList<>());
        return results;
    }

    private static <T> void bronKerbosch(Map<T, ? extends Collection<T>> graph, List<List<T>> results,
            List<T> clique, List<T> candidates, List<T> exclude) {
        if (candidates.isEmpty() && exclude.isEmpty()) {
            results.add(clique);
        } else {
            for (var node : List.copyOf(candidates)) {
                var neighbors = graph.get(node);
                var newClique = new ArrayList<>(clique);
                newClique.add(node);
                var newCandidates = new ArrayList<>(candidates);
                newCandidates.retainAll(neighbors);
                var newExclude = new ArrayList<>(exclude);
                newExclude.retainAll(neighbors);

                bronKerbosch(graph, results, newClique, newCandidates, newExclude);

                candidates.remove(node);
                exclude.add(node);
            }
        }
    }

}
