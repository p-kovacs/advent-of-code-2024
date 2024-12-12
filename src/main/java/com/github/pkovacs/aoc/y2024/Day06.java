package com.github.pkovacs.aoc.y2024;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.BiConsumer;

import com.github.pkovacs.util.data.Cell;
import com.github.pkovacs.util.data.CharTable;
import com.github.pkovacs.util.data.Direction;

public class Day06 extends AbstractDay {

    public static void main(String[] args) {
        var lines = readLines(getInputPath());

        var table = new CharTable(lines);
        var start = table.find('^');

        var parentMap = patrol(table, start);
        long ans1 = parentMap.size();
        long ans2 = parentMap.keySet().stream()
                .filter(p -> p != start && checkLoop(table, p, parentMap.get(p))).count();

        System.out.println("Part 1: " + ans1);
        System.out.println("Part 2: " + ans2);
    }

    private static Map<Cell, State> patrol(CharTable table, Cell start) {
        var parentMap = new HashMap<Cell, State>();
        patrol(table, new State(start, Direction.NORTH), parentMap::putIfAbsent);
        return parentMap;
    }

    private static boolean checkLoop(CharTable table, Cell block, State start) {
        table = new CharTable(table);
        table.set(block, '#');
        return patrol(table, start, null);
    }

    /**
     * Simulates the patrol traversal for part 1 and part 2.
     * <p>
     * For part 1, the visitor is used to collect each visited position along with the previous state (position and
     * direction) that corresponds to its first visit. This information is used for making part 2 more efficient
     * by starting the traversal from the state where the new obstruction causes a difference the first time.
     *
     * @return true if a loop was found
     */
    private static boolean patrol(CharTable table, State start, BiConsumer<Cell, State> visitor) {
        var pos = start.pos;
        var dir = start.dir;
        State state = null;
        var turns = new HashSet<State>();
        while (table.containsCell(pos)) {
            if (visitor != null) {
                visitor.accept(pos, state);
                state = new State(pos, dir);
            }
            var next = pos.neighbor(dir);
            if (table.containsCell(next) && table.get(next) == '#') {
                if (!turns.add(new State(pos, dir))) {
                    return true; // we have this turn before, so we entered a loop
                }
                dir = dir.rotateRight();
            } else {
                pos = next;
            }
        }
        return false;
    }

    private record State(Cell pos, Direction dir) {}

}
