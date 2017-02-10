package CellSociety.Grids;

import CellSociety.Abstract_Cell;

/**
 * Created by th174 on 2/9/2017.
 */
public interface BoundsHandler<E extends SimulationGrid<? extends Abstract_Cell>> {
    public abstract int[] handleBounds(int x, int y, E grid);
}
