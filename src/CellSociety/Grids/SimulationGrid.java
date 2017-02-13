package CellSociety.Grids;

import CellSociety.AbstractDiscrete_CellState;
import CellSociety.Abstract_Cell;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by th174 on 2/12/2017.
 */
public interface SimulationGrid<E extends Abstract_Cell<E, T>, T extends AbstractDiscrete_CellState<T, ? extends Enum<?>>> {
    /**
     * updates the grid and displays the new cell configurations
     */
    void update();

    /**
     * Gets the nearby cells of the given cell, returned as an array
     *
     * @param x
     * @param y
     * @param distanceX
     * @param distanceY
     * @return array of the nearby cells
     */
    E[][] getNearbyCellsAsArray(int x, int y, int distanceX, int distanceY);

    /**
     * Gets the neighbors of the given cell coordinates
     *
     * @param x
     * @param y
     * @return grid of the neighbors
     */
    SimulationGrid<E, T> getNeighbors(int x, int y);

    /**
     * counts Total number of cells of the given state
     *
     * @param state
     * @return number of cells
     */
    int countTotalOfState(Object state);

    /**
     * @return current index
     */
    int getCurrentIndex();

    /**
     * @return max index
     */
    int getMaxIndex();

    /**
     * @param x position of the cell
     * @param y position of the cell
     * @return cell at the given coordinates
     */
    E get(int x, int y);

    /**
     * Sets the cell at the given position to the given cell
     *
     * @param x
     * @param y
     * @param cell
     */
    void set(int x, int y, E cell);

    /**
     * @return stream of the cells
     */
    Stream<E> stream();

    /**
     * @return parallel stream of the cells
     */
    Stream<E> parallelStream();

    /**
     * Apply a method to each cell in the grid
     *
     * @param method
     */
    void forEach(Consumer<? super E> method);

    /**
     * Get width of grid (x-dimension)
     *
     * @return width
     */
    int getColumns();

    /**
     * Get height of grid (y-dimension)
     *
     * @return height
     */
    int getRows();

    /**
     * @return size of the grid
     */
    int size();

    /**
     * @return an iterator of the cells in the grid
     */
    Iterator<E> iterator();

    /**
     * @return set containing the distinct cellstates
     */
    Set<? extends Enum> getDistinctCellStates();

    /**
     * Sets the bounds Type of the simulation grid
     *
     * @param mode
     * @return simulationGrid with the bounds type
     */
    SimulationGrid<E, T> setBoundsType(BoundsHandler<SimulationGrid<E, T>> mode);

    /**
     * Sets shape of the simuluation grid to the given shape
     *
     * @param shape
     * @return simulation grid of the given shape
     */
    SimulationGrid<E, T> setShapeType(NeighborsGetter<SimulationGrid<E, T>> shape);

    E getSingleCell();

    class CellInstantiationException extends Exception {
        public CellInstantiationException(int x, int y, Class<? extends Abstract_Cell> cellType, String initializer) {
            super("\nInstantiation of " + cellType.getSimpleName() + " at location (x,y) = (" + x + "," + y + ") failed:\nInitializer:\t" + initializer);
        }
    }
}