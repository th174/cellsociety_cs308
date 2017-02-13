package CellSociety.Grids;

/**
 * Created by th174 on 2/9/2017.
 */
public interface NeighborsGetter<E extends SimulationGrid> {
    /**
     * Determines which neighbors to get according to the grid and x,y coordinates
     * @param x
     * @param y
     * @param grid of the simulation
     * @return
     */
    E getNeighbors(int x, int y, E grid);
}
