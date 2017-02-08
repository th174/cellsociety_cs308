package CellSociety.Grids;

import CellSociety.Abstract_Cell;

/**
 * Created by th174 on 2/7/2017.
 */
public class HexagonalSimulationGrid<E extends Abstract_Cell> extends AbstractSimulationGrid<E> {

    private HexagonalSimulationGrid(E[][] array) {
        super(array);
    }

    public HexagonalSimulationGrid(String[][][] paramsArray, Class<E> type) throws Exception {
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
    public HexagonalSimulationGrid<E> getNeighbors(int x, int y) {
        E[][] neighbors = super.getNeighborsAsArray(x, y);
        if (x % 2 == 0) {
            neighbors[LEFT][BOTTOM] = null;
            neighbors[RIGHT][BOTTOM] = null;
        } else {
            neighbors[LEFT][TOP] = null;
            neighbors[RIGHT][TOP] = null;
        }
        return new HexagonalSimulationGrid<>(neighbors);
    }
}
