package CellSociety.Grids;

import CellSociety.AbstractDiscrete_CellState;
import CellSociety.Abstract_Cell;
import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Implementation of SimulationGrid for Cell Society.
 * <p>
 * Created by th174 on 2/7/2017.
 *
 * @see SimulationGrid
 */
public class SimulationGridImpl<E extends Abstract_Cell<E, T>, T extends AbstractDiscrete_CellState<T, ? extends Enum<?>>> implements SimulationGrid<E, T>, Iterable<E> {
    public static final int LEFT = 0;
    public static final int RIGHT = 2;
    public static final int TOP = 0;
    public static final int BOTTOM = 2;
    public static final int CENTER = 1;
    private final Map<Pair<Integer, Integer>, E> cells;
    private Class<E> cellType;
    private int columns;
    private int rows;
    private BoundsHandler<SimulationGrid<E, T>> boundsMode;
    private NeighborsGetter<SimulationGrid<E, T>> shapeMode;

    private SimulationGridImpl(E[][] array) {
        columns = array.length;
        rows = array[0].length;
        cells = new HashMap<>();
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                cells.put(new Pair<>(i, j), array[i][j]);
            }
        }
    }

    /**
     * Constructs SimulationGrid with Finite Bounds and Square Shapes
     *
     * @param paramsArray array of String XML parameters to instantiate cells
     * @param type        type of cells
     * @throws CellInstantiationException if one or more cells fail to instantiate through Reflection
     */
    public SimulationGridImpl(String[][][] paramsArray, Class<E> type) throws CellInstantiationException {
        this(paramsArray, type, null, null);
        setShapeType(new SquaresGrid());
        setBoundsType(new FiniteBounds());
    }

    /**
     * Constructs SimulationGrid with Finite Bounds and given shape
     *
     * @param paramsArray array of String XML parameters to instantiate cells
     * @param type        type of cells
     * @param shape       NeighborsGetter to handle for given shape
     * @throws CellInstantiationException if one or more cells fail to instantiate through Reflection
     */
    public SimulationGridImpl(String[][][] paramsArray, Class<E> type, NeighborsGetter<SimulationGrid<E, T>> shape) throws CellInstantiationException {
        this(paramsArray, type, null, shape);
        setShapeType(new SquaresGrid());
    }

    public SimulationGridImpl(String[][][] paramsArray, Class<E> type, BoundsHandler<SimulationGrid<E, T>> bounds) throws CellInstantiationException {
        this(paramsArray, type, bounds, null);
        setBoundsType(new FiniteBounds());
    }

    public SimulationGridImpl(String[][][] paramsArray, Class<E> type, BoundsHandler<SimulationGrid<E, T>> bounds, NeighborsGetter<SimulationGrid<E, T>> shape) throws CellInstantiationException {
        cellType = type;
        boundsMode = bounds;
        shapeMode = shape;
        cells = new HashMap<>();
        columns = paramsArray.length;
        rows = paramsArray[0].length;
        for (int i = 0; i < paramsArray.length; i++) {
            for (int j = 0; j < paramsArray[0].length; j++) {
                instantiateCell(i, j, paramsArray[i][j]);
            }
        }
    }

    private static int actualMod(int dividend, int divisor) {
        return dividend % divisor + (dividend % divisor < 0 ? divisor : 0);
    }

    /**
     * updates the grid and displays the new cell configurations
     *
     * @see SimulationGrid#update()
     */
    @Override
    public void update() {
        forEach(Abstract_Cell::updateState);
        forEach(Abstract_Cell::interact);
    }

    /**
     * Gets the nearby cells of the given cell, returned as an array
     *
     * @see SimulationGrid#getNearbyCellsAsArray(int, int, int, int)
     */
    @Override
    public E[][] getNearbyCellsAsArray(int x, int y, int distanceX, int distanceY) {
        E[][] neighbors = (E[][]) Array.newInstance(cellType, distanceX * 2 + 1, distanceY * 2 + 1);
        for (int i = 0; i < neighbors.length; i++) {
            for (int j = 0; j < neighbors[i].length; j++) {
                neighbors[i][j] = get(x + i - distanceX, y + j - distanceY);
            }
        }
        neighbors[CENTER][CENTER] = null;
        return neighbors;
    }

    /**
     * Gets the neighbors of the given cell coordinates
     *
     * @see SimulationGrid#getNeighbors(int, int)
     */
    @Override
    public SimulationGrid<E, T> getNeighbors(int x, int y) {
        return shapeMode.getNeighbors(x, y, this);
    }

    /**
     * counts Total number of cells of the given state
     *
     * @see SimulationGrid#countTotalOfState(Object)
     */
    @Override
    public int countTotalOfState(Object state) {
        return (int) parallelStream().filter(e -> e.getCurrentState().equals(state)).count();
    }

    /**
     * @return current timeline index
     * @see SimulationGrid#getCurrentTimelineIndex()
     */
    @Override
    public int getCurrentTimelineIndex() {
        return stream().findAny().get().getCurrentIndex();
    }

    /**
     * @return max timeline index
     * @see SimulationGrid#getMaxTimelineIndex()
     */
    @Override
    public int getMaxTimelineIndex() {
        return stream().findAny().get().getMaxIndex();
    }

    /**
     * Get the cell at coordinates x, y on grid
     *
     * @see SimulationGrid#get(int, int)
     */
    @Override
    public E get(int x, int y) {
        return cells.get(boundsMode.handleBounds(x, y, this));
    }

    /**
     * Sets the cell at the given position to the given cell
     *
     * @see SimulationGrid#set(int, int, Abstract_Cell)
     */
    @Override
    public void set(int x, int y, E cell) {
        cells.put(new Pair<>(x, y), cell);
        if (Objects.nonNull(cell)) {
            cell.setParentGrid(this);
        }
    }

    /**
     * @return stream of the cells
     * @see SimulationGrid#stream()
     */
    @Override
    public Stream<E> stream() {
        return parallelStream().sequential();
    }

    /**
     * @return parallel stream of the cells
     * @see SimulationGrid#parallelStream()
     */
    @Override
    public Stream<E> parallelStream() {
        return cells.values().parallelStream().unordered().filter(Objects::nonNull);
    }

    /**
     * Apply a method to each cell in the grid
     *
     * @see SimulationGrid#forEach(Consumer)
     */
    @Override
    public void forEach(Consumer<? super E> method) {
        stream().forEach(method);
    }

    /**
     * Get width of grid (x-dimension)
     *
     * @see SimulationGrid#getColumns()
     */
    @Override
    public int getColumns() {
        return columns;
    }

    /**
     * Get height of grid (y-dimension)
     *
     * @see SimulationGrid#getRows()
     */
    @Override
    public int getRows() {
        return rows;
    }

    /**
     * Sets the bounds Type of the simulation grid
     *
     * @see SimulationGrid#setBoundsType(BoundsHandler)
     * @see NeighborsGetter
     */
    @Override
    public SimulationGrid<E, T> setBoundsType(BoundsHandler<SimulationGrid<E, T>> mode) {
        boundsMode = mode;
        return this;
    }

    /**
     * Sets shape of the simuluation grid to the given shape
     *
     * @see SimulationGrid#setShapeType(NeighborsGetter)
     * @see NeighborsGetter
     */
    @Override
    public SimulationGrid<E, T> setShapeType(NeighborsGetter<SimulationGrid<E, T>> shape) {
        shapeMode = shape;
        return this;
    }

    /**
     * @return size of the grid
     * @see SimulationGrid#size()
     */
    @Override
    public int size() {
        return (int) parallelStream().count();
    }

    /**
     * @return an iterator of the cells in the grid
     * @see SimulationGrid#iterator()
     */
    @Override
    public Iterator<E> iterator() {
        return stream().iterator();
    }

    /**
     * @return set containing the distinct cellstates
     * @see SimulationGrid#getDistinctCellStates()
     */
    @Override
    public Set<? extends Enum> getDistinctCellStates() {
        return getSingleCell().getCurrentState().getDistinctCellStates();
    }

    /**
     * @return Returns any single cell from the grid
     * @see SimulationGrid#getSingleCell()
     */
    @Override
    public E getSingleCell() {
        return parallelStream().findAny().get();
    }

    private void instantiateCell(int x, int y, String... cellStateInitializer) throws CellInstantiationException {
        try {
            set(x, y, cellType.getConstructor(int.class, int.class, cellStateInitializer.getClass()).newInstance(x, y, cellStateInitializer));
        } catch (Exception e) {
            e.printStackTrace();
            throw new CellInstantiationException(x, y, cellType, Arrays.toString(cellStateInitializer));
        }
    }

    /**
     * FiniteBounds, bounded by the initial size. Cells on the edges have fewer neighbors.
     *
     * @see BoundsHandler
     */
    public final class FiniteBounds implements BoundsHandler<SimulationGrid<E, T>> {
        /**
         * @return Unmodified Pair of coordinates
         * @see BoundsHandler#handleBounds(int, int, CellSociety.Grids.SimulationGrid)
         */
        @Override
        public Pair<Integer, Integer> handleBounds(int x, int y, SimulationGrid<E, T> grid) {
            return new Pair<>(x, y);
        }
    }

    /**
     * Wrapped Bounds where cells on one edge of the grid interact with cells on the other side
     *
     * @see BoundsHandler
     */
    public final class WrappedBounds implements BoundsHandler<SimulationGrid<E, T>> {
        /**
         * @return Pair of coordinates, wrapped to the other side if out of bounds
         * @see CellSociety.Grids.BoundsHandler#handleBounds(int, int, CellSociety.Grids.SimulationGrid)
         */
        @Override
        public Pair<Integer, Integer> handleBounds(int x, int y, SimulationGrid<E, T> grid) {
            return new Pair<>(actualMod(x, grid.getColumns()), actualMod(y, grid.getRows()));
        }
    }

    /**
     * Grid of squares, the classic simple implementation
     *
     * @see NeighborsGetter
     */
    public final class SquaresGrid implements NeighborsGetter<SimulationGrid<E, T>> {
        /**
         * @return SimulationGrid of up to 8 possible neighbors
         * @see NeighborsGetter#getNeighbors(int, int, SimulationGrid)
         */
        @Override
        public SimulationGrid<E, T> getNeighbors(int x, int y, SimulationGrid<E, T> grid) {
            return new SimulationGridImpl<>(grid.getNearbyCellsAsArray(x, y, 1, 1));
        }
    }

    /**
     * Grid similar to SquaresGrid but here only adjacent neighbors are considered
     *
     * @see NeighborsGetter
     */
    public final class AdjacentSquaresGrid implements NeighborsGetter<SimulationGrid<E, T>> {
        /**
         * @return SimulationGrid of up to 4 Square Adjacent Neighbors
         * @see NeighborsGetter#getNeighbors(int, int, SimulationGrid)
         */
        @Override
        public SimulationGrid<E, T> getNeighbors(int x, int y, SimulationGrid<E, T> grid) {
            E[][] neighbors = grid.getNearbyCellsAsArray(x, y, 1, 1);
            neighbors[LEFT][TOP] = null;
            neighbors[LEFT][BOTTOM] = null;
            neighbors[RIGHT][TOP] = null;
            neighbors[RIGHT][BOTTOM] = null;
            return new SimulationGridImpl<>(neighbors);
        }
    }

    /**
     * Again a variation of SquaresGrid where now only the corner neighbors are considered
     *
     * @see NeighborsGetter
     */
    public final class CornersSquaresGrid implements NeighborsGetter<SimulationGrid<E, T>> {
        /**
         * @return SimulationGrid of 4 Square Corner Neighbors
         * @see NeighborsGetter#getNeighbors(int, int, SimulationGrid)
         */
        @Override
        public SimulationGrid<E, T> getNeighbors(int x, int y, SimulationGrid<E, T> grid) {
            E[][] neighbors = grid.getNearbyCellsAsArray(x, y, 1, 1);
            neighbors[CENTER][TOP] = null;
            neighbors[CENTER][BOTTOM] = null;
            neighbors[RIGHT][CENTER] = null;
            neighbors[LEFT][CENTER] = null;
            return new SimulationGridImpl<>(neighbors);
        }
    }

    /**
     * A grid consisting of hexagons as opposed to squares. Note the number of neighbors are now different
     *
     * @see NeighborsGetter
     */
    public final class HexagonsGrid implements NeighborsGetter<SimulationGrid<E, T>> {
        /**
         * @return SimulationGrid of up to 6 Hexagonal Neighbors
         * @see NeighborsGetter#getNeighbors(int, int, SimulationGrid)
         */
        @Override
        public SimulationGrid<E, T> getNeighbors(int x, int y, SimulationGrid<E, T> grid) {
            E[][] neighbors = grid.getNearbyCellsAsArray(x, y, 1, 1);
            if (x % 2 == 0) {
                neighbors[RIGHT][TOP] = null;
                neighbors[RIGHT][BOTTOM] = null;
            } else {
                neighbors[LEFT][TOP] = null;
                neighbors[LEFT][BOTTOM] = null;
            }
            return new SimulationGridImpl<>(neighbors);
        }
    }

    /**
     * Grid consisting of Hexagons where only adjacent neighbors are considered
     *
     * @see NeighborsGetter
     */
    public final class AdjacentHexagonsGrid implements NeighborsGetter<SimulationGrid<E, T>> {
        /**
         * Note: This is identical to HexagonsGrid.getNeighbors()
         *
         * @return SimulationGrid of up to 6 Hexagonal Adjacent Neighbors
         * @see NeighborsGetter#getNeighbors(int, int, SimulationGrid)
         */
        @Override
        public SimulationGrid<E, T> getNeighbors(int x, int y, SimulationGrid<E, T> grid) {
            return new HexagonsGrid().getNeighbors(x, y, grid);
        }
    }

    /**
     * Grid where the cells are represented as Triangles
     *
     * @see NeighborsGetter
     */
    public final class TrianglesGrid implements NeighborsGetter<SimulationGrid<E, T>> {
        /**
         * @return SimulationGrid of up to 12 Triangular Neighbors
         * @see NeighborsGetter#getNeighbors(int, int, SimulationGrid)
         */
        @Override
        public SimulationGrid<E, T> getNeighbors(int x, int y, SimulationGrid<E, T> grid) {
            E[][] neighbors = getNearbyCellsAsArray(x, y, 1, 2);
            if ((x + y) % 2 == 0) {
                neighbors[RIGHT][TOP + TOP] = null;
                neighbors[RIGHT][BOTTOM + BOTTOM] = null;
            } else {
                neighbors[LEFT][TOP + TOP] = null;
                neighbors[LEFT][BOTTOM + BOTTOM] = null;
            }
            return new SimulationGridImpl<>(neighbors);
        }
    }

    /**
     * Grid where the cells are represented as Triangles but only adjacent neighbors are considered
     *
     * @see NeighborsGetter
     */
    public final class AdjacentTrianglesGrid implements NeighborsGetter<SimulationGrid<E, T>> {
        /**
         * @return SimulationGrid of up to 3 Adjacent Triangular Neighbors
         * @see NeighborsGetter#getNeighbors(int, int, SimulationGrid)
         */
        @Override
        public SimulationGrid<E, T> getNeighbors(int x, int y, SimulationGrid<E, T> grid) {
            E[][] neighbors = getNearbyCellsAsArray(x, y, 1, 1);
            neighbors[RIGHT][TOP] = null;
            neighbors[LEFT][TOP] = null;
            neighbors[RIGHT][BOTTOM] = null;
            neighbors[LEFT][BOTTOM] = null;
            if ((x + y) % 2 == 0) {
                neighbors[RIGHT][CENTER] = null;
            } else {
                neighbors[LEFT][CENTER] = null;
            }
            return new SimulationGridImpl<>(neighbors);
        }
    }

    /**
     * Grid where the cells are represented as Triangles. Only corner neighbors are considered
     *
     * @see NeighborsGetter
     */
    public final class CornersTrianglesGrid implements NeighborsGetter<SimulationGrid<E, T>> {
        /**
         * @return SimulationGrid of up to 9 Corner Triangular Neighbors
         * @see NeighborsGetter#getNeighbors(int, int, SimulationGrid)
         */
        @Override
        public SimulationGrid<E, T> getNeighbors(int x, int y, SimulationGrid<E, T> grid) {
            E[][] neighbors = getNearbyCellsAsArray(x, y, 1, 2);
            neighbors[CENTER][1] = null;
            neighbors[CENTER][3] = null;
            if ((x + y) % 2 == 0) {
                neighbors[LEFT][2] = null;
            } else {
                neighbors[RIGHT][2] = null;
            }
            return new SimulationGridImpl<>(neighbors);
        }
    }
}
