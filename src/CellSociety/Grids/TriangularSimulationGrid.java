package CellSociety.Grids;

import CellSociety.Abstract_Cell;

/**
 * Created by th174 on 2/7/2017.
 */
public class TriangularSimulationGrid<E extends Abstract_Cell> extends AbstractSimulationGrid<E> {

    private TriangularSimulationGrid(E[][] array) {
        super(array);
    }

    public TriangularSimulationGrid(String[][][] paramsArray, Class<E> type) throws Exception {
        super(paramsArray, type);
        this.forEach(e -> e.setParentGrid(this));
    }

    /**
     * Returns up to 4 adjacent neighbors of the cell at x,y in a 3 by 3 grid; the center of the cell is null
     *
     * @param x
     * @param y
     * @return 3 by 3 Grid
     */
    @Override
    public TriangularSimulationGrid<E> getNeighbors(int x, int y) {
        E[][] neighbors = getNeighborsAsArray(x, y);
        neighbors[LEFT][TOP] = null;
        neighbors[LEFT][BOTTOM] = null;
        neighbors[RIGHT][TOP] = null;
        neighbors[RIGHT][BOTTOM] = null;
        if ((x + y) % 2 == 0) {
            neighbors[CENTER][BOTTOM] = null;
        } else {
            neighbors[CENTER][TOP] = null;
        }
        return new TriangularSimulationGrid<>(neighbors);
    }
}
