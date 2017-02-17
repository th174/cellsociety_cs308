package CellSociety.Grids;

/**
 * Functional Interface that handles algorithm to retrieve all neighboring cells to specified location.
 * <p>
 * Created by th174 on 2/9/2017.
 *
 * @param <E> Type of SimulationGrid
 */
public interface NeighborsGetter<E extends SimulationGrid> {
    /**
     * Retrieves all cells neighboring a SimulationGrid location as another SimulationGrid
     *
     * @param x    x-coordinate
     * @param y    y-coordinate
     * @param grid SimulationGrid from which neighbors will be retrieved
     * @return SimulationGrid of neighboring cells
     */
    E getNeighbors(int x, int y, E grid);
}
