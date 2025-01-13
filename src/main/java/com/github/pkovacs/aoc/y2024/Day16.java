package com.github.pkovacs.aoc.y2024;

import java.util.Arrays;
import java.util.stream.Stream;

import com.github.pkovacs.util.CharTable;
import com.github.pkovacs.util.Dijkstra;
import com.github.pkovacs.util.Dir;
import com.github.pkovacs.util.Pos;
import com.github.pkovacs.util.WeightedGraph.Edge;

public class Day16 extends AbstractDay {

    public static void main(String[] args) {
        var lines = readLines(getInputPath());

        var table = new CharTable(lines);
        var start = table.find('S');
        var end = table.find('E');
        var endStates = Arrays.stream(Dir.values()).map(dir -> new State(end, dir)).toList();

        // Solve part 1 using Dijsktra's algorithm
        var paths = Dijkstra.findPaths(st -> getEdges(table, st, false), new State(start, Dir.E));
        long dist = endStates.stream().mapToLong(st -> paths.get(st).dist()).min().orElseThrow();

        // Solve part 2: run Dijsktra's algorithm in the reversed graph from the end states, and collect each state
        // for which the sum of the two calculated distances equals to the optimal total distance.
        // (An alternative approach would be to run BFS in the reversed graph instead, but only using the edges
        // that are "tight" with respect to the first run of Dijkstra's algorithm, i.e. the edges that constitute
        // the shortest paths.)
        var revPaths = Dijkstra.findPathsFromAny(st -> getEdges(table, st, true), endStates.stream());
        long count = paths.keySet().stream()
                .filter(st -> paths.get(st).dist() + revPaths.get(st).dist() == dist)
                .map(State::pos)
                .distinct().count();

        System.out.println("Part 1: " + dist);
        System.out.println("Part 2: " + count);
    }

    private static Stream<Edge<State>> getEdges(CharTable table, State st, boolean reversed) {
        var next = st.pos.neighbor(reversed ? st.dir.opposite() : st.dir);
        return Stream.of(
                new Edge<>(new State(next, st.dir), 1),
                new Edge<>(new State(st.pos, st.dir.rotateLeft()), 1000),
                new Edge<>(new State(st.pos, st.dir.rotateRight()), 1000)
        ).filter(e -> table.get(e.end().pos) != '#');
    }

    private record State(Pos pos, Dir dir) {}

}
