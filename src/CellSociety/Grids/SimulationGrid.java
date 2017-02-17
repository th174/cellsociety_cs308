package CellSociety.Grids;

import CellSociety.AbstractDiscrete_CellState;
import CellSociety.Abstract_Cell;
import CellSociety.CellStateTimeline;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Interface provides basic API for a Grid of Abstract_Cells with Abstract_CellStates to run the simulation on
 *
 * @param <E> Type of Abstract_Cell contained by the grid
 * @param <T> Type of AbstractDiscrete_CellState contained by the Cells of the grid
 */
public interface SimulationGrid<E extends Abstract_Cell<E, T>, T extends AbstractDiscrete_CellState<T, ? extends Enum<?>>> {
    /**
     * updates the grid and displays the new cell configurations
     */
    void update();

    /**
     * Gets all cells neighboring a given location, returned as an array
     *
     * @param x         x-coordinate
     * @param y         y-coordinate
     * @param distanceX distance in the X direction
     * @param distanceY distance in the Y direction
     * @return array of the nearby cells
     */
    E[][] getNearbyCellsAsArray(int x, int y, int distanceX, int distanceY);

    /**
     * Gets all cells neighboring a given location, returned as a SimulationGrid
     *
     * @param x x-coordinate
     * @param y y-coodinate
     * @return grid of the neighbors
     */
    SimulationGrid<E, T> getNeighbors(int x, int y);

    /**
     * counts Total number of cells of the given state
     *
     * @param state CellState to be compared with all CellStates on the grid
     * @return number of cells
     */
    int countTotalOfState(Object state);

    /**
     * @return current timeline index
     * @see CellStateTimeline#getCurrentIndex()
     */
    int getCurrentTimelineIndex();

    /**
     * @return max timeline index
     * @see CellStateTimeline#getMaxIndex()
     */
    int getMaxTimelineIndex();

    /**
     * @param x x-coordinate of the cell
     * @param y y-coordinate of the cell
     * @return cell at the given coordinates
     */
    E get(int x, int y);

    /**
     * Sets the cell at the given position to the given cell
     *
     * @param x    x-coordinate
     * @param y    y-coordinate
     * @param cell cell to be placed into grid
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
     * @param method Consumer method
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
     * @see Iterable#iterator()
     */
    Iterator<E> iterator();

    /**
     * @return set containing all possible distinct CellStates in this grid
     */
    Set<? extends Enum> getDistinctCellStates();

    /**
     * Sets the bounds Type of the simulation grid
     *
     * @param mode algorithm to handle bounds
     * @return simulationGrid with the bounds type
     * @see BoundsHandler
     */
    SimulationGrid<E, T> setBoundsType(BoundsHandler<SimulationGrid<E, T>> mode);

    /**
     * Sets shape of the simuluation grid to the given shape
     *
     * @param shape algorithm to get all Neighbors of a cell
     * @return simulation grid of the given shape
     * @see NeighborsGetter
     */
    SimulationGrid<E, T> setShapeType(NeighborsGetter<SimulationGrid<E, T>> shape);

    /**
     * @return A random single cell from this SimulationGrid
     */
    E getSingleCell();

    /**
     * Thrown when this SimulationGrid could not instantiate a new cell through Reflection
     */
    class CellInstantiationException extends Exception {
        public CellInstantiationException(int x, int y, Class<? extends Abstract_Cell> cellType, String initializer) {
            super("\nInstantiation of " + cellType.getSimpleName() + " at location (x,y) = (" + x + "," + y + ") failed:\nInitializer:\t" + initializer);
        }
    }
}