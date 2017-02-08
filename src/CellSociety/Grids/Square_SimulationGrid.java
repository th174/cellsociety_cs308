package CellSociety.Grids;

import CellSociety.Abstract_Cell;

/**
 * Created by th174 on 1/29/2017.
 */
public class Square_SimulationGrid<E extends Abstract_Cell> extends Abstract_SimulationGrid<E> {

    private Square_SimulationGrid(E[][] array) {
        super(array);
    }

    public Square_SimulationGrid(String[][][] paramsArray, Class<E> type) throws Exception {
        super(paramsArray, type);
        this.forEach(e -> e.setParentGrid(this));
    }

    @Override
    public Square_SimulationGrid<E> getNeighbors(int x, int y) {
        return new Square_SimulationGrid<>(getNeighborsAsArray(x, y));
    }

    /**
     * Returns up to 4 adjacent neighbors of the cell at x,y in a 3 by 3 grid; the center of the cell is null
     *
     * @param x
     * @param y
     * @return 3 by 3 Grid
     */
    public Square_SimulationGrid<E> getAdjNeighbors(int x, int y) {
        Square_SimulationGrid<E> neighbors = getNeighbors(x, y);
        neighbors.set(LEFT, TOP, null);
        neighbors.set(LEFT, BOTTOM, null);
        neighbors.set(RIGHT, TOP, null);
        neighbors.set(RIGHT, BOTTOM, null);
        return neighbors;
    }
}
