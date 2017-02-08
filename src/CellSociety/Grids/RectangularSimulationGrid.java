package CellSociety.Grids;

import CellSociety.Abstract_Cell;

/**
 * Created by th174 on 1/29/2017.
 */
public class RectangularSimulationGrid<E extends Abstract_Cell> extends AbstractSimulationGrid<E> {

    private RectangularSimulationGrid(E[][] array) {
        super(array);
    }

    public RectangularSimulationGrid(String[][][] paramsArray, Class<E> type) throws Exception {
        super(paramsArray, type);
        this.forEach(e -> e.setParentGrid(this));
    }

    @Override
    public RectangularSimulationGrid<E> getNeighbors(int x, int y) {
        return new RectangularSimulationGrid<>(getNeighborsAsArray(x, y));
    }

    /**
     * Returns up to 4 adjacent neighbors of the cell at x,y in a 3 by 3 grid; the center of the cell is null
     *
     * @param x
     * @param y
     * @return 3 by 3 Grid
     */
    public RectangularSimulationGrid<E> getAdjNeighbors(int x, int y) {
        RectangularSimulationGrid<E> neighbors = getNeighbors(x, y);
        neighbors.set(LEFT, TOP, null);
        neighbors.set(LEFT, BOTTOM, null);
        neighbors.set(RIGHT, TOP, null);
        neighbors.set(RIGHT, BOTTOM, null);
        return neighbors;
    }
}
