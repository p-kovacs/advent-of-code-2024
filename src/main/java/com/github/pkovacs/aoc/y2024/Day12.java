package com.github.pkovacs.aoc.y2024;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.pkovacs.util.alg.Bfs;
import com.github.pkovacs.util.data.Cell;
import com.github.pkovacs.util.data.CharTable;
import com.github.pkovacs.util.data.Direction;

public class Day12 extends AbstractDay {

    public static void main(String[] args) {
        var table = new CharTable(readLines(getInputPath()));

        long ans1 = 0;
        long ans2 = 0;
        var reached = new HashSet<Cell>();
        for (var current : table.cells().toList()) {
            if (reached.contains(current)) {
                continue;
            }

            var region = Bfs.run(current,
                    p -> table.neighbors(p).filter(q -> table.get(q) == table.get(p)).toList()).keySet();
            var sideSections = SideSection.collect(region);

            long perimeter = sideSections.size();
            long cornerCount = sideSections.stream().mapToLong(s -> s.cornerCount(sideSections)).sum() / 2;

            ans1 += region.size() * perimeter;
            ans2 += region.size() * cornerCount;
            reached.addAll(region);
        }

        System.out.println("Part 1: " + ans1);
        System.out.println("Part 2: " + ans2);
    }

    /**
     * Represents a section of a side of a region. For part 2, we sum the corner count of each side section
     * (0, 1, or 2), and divide the sum by two to get the total corner count of the region, which equals to the
     * number of sides.
     * <p>
     * This method has quadratic time complexity in terms of the number of side sections for each region. A faster
     * algorithm could be achieved if the side sections and sorted (and grouped) appropriately so that the relevant
     * neighbors get next to each other.
     */
    private record SideSection(Cell inner, Cell outer) {

        static List<SideSection> collect(Set<Cell> region) {
            return region.stream().flatMap(p -> p.neighbors().filter(q -> !region.contains(q))
                    .map(q -> new SideSection(p, q))).toList();
        }

        long cornerCount(Collection<SideSection> sideSections) {
            boolean horizontalSide = inner.x() == outer.x();
            return Arrays.stream(Direction.values())
                    .filter(dir -> dir.isHorizontal() == horizontalSide)
                    .map(this::neighbor)
                    .filter(n -> !sideSections.contains(n))
                    .count();
        }

        SideSection neighbor(Direction dir) {
            return new SideSection(inner.neighbor(dir), outer.neighbor(dir));
        }

    }

}
