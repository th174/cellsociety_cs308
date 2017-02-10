package CellSociety.Grids;

import CellSociety.Abstract_Cell;

/**
 * Created by th174 on 2/9/2017.
 */
public interface NeighborsGetter<E extends SimulationGrid<? extends Abstract_Cell>> {
    public abstract E getNeighbors(int x, int y, E grid);
}
