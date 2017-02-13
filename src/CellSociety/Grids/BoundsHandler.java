package CellSociety.Grids;

import javafx.util.Pair;

/**
 * Created by th174 on 2/9/2017.
 */
public interface BoundsHandler<E extends SimulationGrid> {
    /**
     * handles the bounds of the simulation. Processed the coordinates based on current grid and bound setting
     * @param x
     * @param y
     * @param grid of the simulation
     * @return new coordinates after accounting for bounds processing
     */
    Pair<Integer, Integer> handleBounds(int x, int y, E grid);
}
