package CellSociety.Grids;

import CellSociety.Abstract_Cell;
import CellSociety.Abstract_CellState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by th174 on 2/7/2017.
 */
public class SimulationGridImpl<E extends Abstract_Cell<E, T>, T extends Abstract_CellState<T, ?>> implements SimulationGrid<E, T>, Iterable<E> {
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

    public SimulationGridImpl(String[][][] paramsArray, Class<E> type) throws CellInstantiationException {
        this(paramsArray, type, null, null);
        setShapeType(new SquaresGrid());
        setBoundsType(new FiniteBounds());
    }

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

    public void update() {
        forEach(Abstract_Cell::updateState);
        forEach(Abstract_Cell::interact);
    }

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

    public SimulationGrid<E, T> getNeighbors(int x, int y) {
        return shapeMode.getNeighbors(x, y, this);
    }

    public int countTotalOfState(T state) {
        return (int) parallelStream().filter(e -> e.getCurrentState().equals(state)).count();
    }

    public int getCurrentIndex() {
        return stream().findAny().get().getCurrentIndex();
    }

    public int getMaxIndex() {
        return stream().findAny().get().getMaxIndex();
    }

    /**
     * Get the cell at coordinates x, y on grid
     *
     * @param x
     * @param y
     * @return AbstractCell
     */
    public E get(int x, int y) {
        return cells.get(boundsMode.handleBounds(x, y, this));
    }

    public void set(int x, int y, E cell) {
        cells.put(new Pair<>(x, y), cell);
        if (Objects.nonNull(cell)) {
            cell.setParentGrid(this);
        }
    }

    public Stream<E> stream() {
        return parallelStream().sequential();
    }

    public Stream<E> parallelStream() {
        return cells.values().parallelStream().unordered().filter(Objects::nonNull);
    }

    /**
     * Apply a method to each cell in the grid
     *
     * @param method
     */
    @Override
    public void forEach(Consumer<? super E> method) {
        stream().forEach(method);
    }

    /**
     * Get width of grid (x-dimension)
     *
     * @return width
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Get height of grid (y-dimension)
     *
     * @return height
     */
    public int getRows() {
        return rows;
    }

    public SimulationGrid<E, T> setBoundsType(BoundsHandler<SimulationGrid<E, T>> mode) {
        boundsMode = mode;
        return this;
    }

    public SimulationGrid<E, T> setShapeType(NeighborsGetter<SimulationGrid<E, T>> shape) {
        shapeMode = shape;
        return this;
    }
    
    public int size() {
        return (int) parallelStream().count();
    }

    public ObservableMap<String, Double> getCellConcentrations() {
        Map<String, Double> cellMap = new HashMap<String, Double>();
        ObservableMap<String, Double> cellConcentrations = FXCollections.observableMap(cellMap);
        double totalCells = columns * rows;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                String aState = get(i, j).getCurrentState().toString();
                if (!cellConcentrations.containsKey(aState)) {
                    cellConcentrations.put(aState, 0.0);
                }
                cellConcentrations.put(aState, (cellConcentrations.get(aState) * totalCells + 1.0) / totalCells);
            }
        }
        //forEach(Abstract_Cell a,stateCount::updateMapCount);

        return cellConcentrations;
    }

    @Override
    public Iterator<E> iterator() {
        return stream().iterator();
    }

    @Override
    public Set getDistinctCellStates() {
        return getSingleCell().getCurrentState().getDistinctCellStates();
    }

    private E getSingleCell() {
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

    public final class FiniteBounds implements BoundsHandler<SimulationGrid<E, T>> {
        @Override
        public Pair<Integer, Integer> handleBounds(int x, int y, SimulationGrid<E, T> grid) {
            return new Pair<>(x, y);
        }
    }

    public final class WrappedBounds implements BoundsHandler<SimulationGrid<E, T>> {
        @Override
        public Pair<Integer, Integer> handleBounds(int x, int y, SimulationGrid<E, T> grid) {
            return new Pair<>(actualMod(x, grid.getColumns()), actualMod(y, grid.getRows()));
        }
    }

    public final class SquaresGrid implements NeighborsGetter<SimulationGrid<E, T>> {
        @Override
        public SimulationGrid<E, T> getNeighbors(int x, int y, SimulationGrid<E, T> grid) {
            return new SimulationGridImpl<>(grid.getNearbyCellsAsArray(x, y, 1, 1));
        }
    }

    public final class AdjacentSquaresGrid implements NeighborsGetter<SimulationGrid<E, T>> {
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

    public final class CornersSquaresGrid implements NeighborsGetter<SimulationGrid<E, T>> {
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

    public final class HexagonsGrid implements NeighborsGetter<SimulationGrid<E, T>> {
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

    public final class AdjacentHexagonsGrid implements NeighborsGetter<SimulationGrid<E, T>> {
        @Override
        public SimulationGrid<E, T> getNeighbors(int x, int y, SimulationGrid<E, T> grid) {
            return new HexagonsGrid().getNeighbors(x, y, grid);
        }
    }

    public final class TrianglesGrid implements NeighborsGetter<SimulationGrid<E, T>> {
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

    public final class AdjacentTrianglesGrid implements NeighborsGetter<SimulationGrid<E, T>> {
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

    public final class CornersTrianglesGrid implements NeighborsGetter<SimulationGrid<E, T>> {
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
