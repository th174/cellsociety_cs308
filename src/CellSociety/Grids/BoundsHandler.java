package CellSociety.Grids;

import javafx.util.Pair;

/**
 * Functional interface that handles SimulationGrid coordinate bounds.
 * <p>
 * Created by th174 on 2/9/2017.
 *
 * @param <E> Type of SimulationGrid
 */
public interface BoundsHandler<E extends SimulationGrid> {
    /**
     * Checks whether coordinates are in defined bounds and processes them before passing to SimulationGrid as a Pair of Integers
     *
     * @param x    x-coordinate
     * @param y    y-coordinate
     * @param grid SimulationGrid
     * @return Pair of new coordinates after accounting for bounds processing
     */
    Pair<Integer, Integer> handleBounds(int x, int y, E grid);
}
