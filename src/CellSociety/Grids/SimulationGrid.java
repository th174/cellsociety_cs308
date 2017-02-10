package CellSociety.Grids;

import CellSociety.Abstract_Cell;
import CellSociety.Abstract_CellState;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by th174 on 2/7/2017.
 */
public class SimulationGrid<E extends Abstract_Cell<E, ? extends Abstract_CellState>> implements Iterable<E> {
    public static final int LEFT = 0;
    public static final int RIGHT = 2;
    public static final int TOP = 0;
    public static final int BOTTOM = 2;
    public static final int CENTER = 1;
    private final List<List<E>> cells;
    private Class<E> cellType;
    private int columns;
    private int rows;
    private BoundsHandler<SimulationGrid<E>> boundsMode;
    private NeighborsGetter<SimulationGrid<E>> shapeMode;

    private SimulationGrid(E[][] array) {
        cells = new ArrayList<>();
        columns = array.length;
        rows = array[0].length;
        for (E[] row : array) {
            cells.add(Arrays.asList(row));
        }
    }

    public SimulationGrid(String[][][] paramsArray, Class<E> type) throws Exception {
        this(paramsArray, type, null, null);
        setShapeType(new SquaresGrid());
        setBoundsType(new FiniteBounds());
    }

    public SimulationGrid(String[][][] paramsArray, Class<E> type, NeighborsGetter<SimulationGrid<E>> shape) throws Exception {
        this(paramsArray, type, null, shape);
        setShapeType(new SquaresGrid());
    }

    public SimulationGrid(String[][][] paramsArray, Class<E> type, BoundsHandler<SimulationGrid<E>> bounds) throws Exception {
        this(paramsArray, type, bounds, null);
        setBoundsType(new FiniteBounds());
    }

    public SimulationGrid(String[][][] paramsArray, Class<E> type, BoundsHandler<SimulationGrid<E>> bounds, NeighborsGetter<SimulationGrid<E>> shape) throws Exception {
        cellType = type;
        boundsMode = bounds;
        shapeMode = shape;
        cells = new ArrayList<>();
        columns = paramsArray.length;
        rows = paramsArray[0].length;
        for (int i = 0; i < paramsArray.length; i++) {
            for (int j = 0; j < paramsArray[0].length; j++) {
                cells.add(new ArrayList<>());
                String[] temp = Arrays.copyOf(paramsArray[i][j], paramsArray[i][j].length);
                cells.get(i).add(cellType.getConstructor(int.class, int.class, String[].class).newInstance(i, j, temp));
                cells.get(i).get(j).setParentGrid(this);
            }
        }
    }

    public void update() {
        forEach(Abstract_Cell::updateState);
        forEach(Abstract_Cell::interact);
    }

    private E[][] getNearbyCellsAsArray(int x, int y, int distanceX, int distanceY) {
        E[][] neighbors = (E[][]) Array.newInstance(cellType, distanceX * 2 + 1, distanceY * 2 + 1);
        for (int i = 0; i < neighbors.length; i++) {
            for (int j = 0; j < neighbors[i].length; j++) {
                neighbors[i][j] = get(x + i - distanceX, y + j - distanceY);
            }
        }
        neighbors[CENTER][CENTER] = null;
        return neighbors;
    }

    public SimulationGrid<E> getNeighbors(int x, int y) {
        return shapeMode.getNeighbors(x, y, this);
    }

    public long countTotalOfState(Abstract_CellState state) {
        return parallelStream().filter(e -> e.getCurrentState().equals(state)).count();
    }

    /**
     * Get the cell at coordinates x, y on grid
     *
     * @param x
     * @param y
     * @return AbstractCell
     */
    private E get(int x, int y) {
        int[] loc = boundsMode.handleBounds(x, y, this);
        return (loc[0] >= 0 && loc[0] < cells.size() && loc[1] >= 0 && loc[1] < cells.get(x).size()) ? cells.get(x).get(y) : null;
    }

    private void set(int x, int y, E cell) {
        cells.get(x).set(y, cell);
        if (Objects.nonNull(cell)) {
            cell.setParentGrid(this);
        }
    }

    private static int actualMod(int dividend, int divisor) {
        return dividend % divisor + (dividend % divisor < 0 ? divisor : 0);
    }

    public Stream<E> stream() {
        return parallelStream().sequential();
    }

    public Stream<E> parallelStream() {
        return cells.parallelStream().flatMap(List::stream).unordered().filter(Objects::nonNull);
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

    public SimulationGrid<E> setBoundsType(BoundsHandler<SimulationGrid<E>> mode) {
        boundsMode = mode;
        return this;
    }

    public SimulationGrid<E> setShapeType(NeighborsGetter<SimulationGrid<E>> shape) {
        shapeMode = shape;
        return this;
    }

    @Override
    public Iterator<E> iterator() {
        return stream().iterator();
    }

    public final class FiniteBounds implements BoundsHandler<SimulationGrid<E>> {
        @Override
        public int[] handleBounds(int x, int y, SimulationGrid<E> grid) {
            return new int[]{x, y};
        }
    }

    public final class WrappedBounds implements BoundsHandler<SimulationGrid<E>> {
        @Override
        public int[] handleBounds(int x, int y, SimulationGrid<E> grid) {
            return new int[]{actualMod(x, grid.columns), actualMod(y, grid.rows)};
        }
    }

    public final class InfiniteBounds implements BoundsHandler<SimulationGrid<E>> {
        @Override
        public int[] handleBounds(int x, int y, SimulationGrid<E> grid) {
            //TODO: this
            return new int[0];
        }
    }

    public final class SquaresGrid implements NeighborsGetter<SimulationGrid<E>> {
        public SimulationGrid<E> getNeighbors(int x, int y, SimulationGrid<E> grid) {
            return new SimulationGrid<>(grid.getNearbyCellsAsArray(x, y, 1, 1));
        }
    }

    public final class AdjacentSquaresGrid implements NeighborsGetter<SimulationGrid<E>> {
        @Override
        public SimulationGrid<E> getNeighbors(int x, int y, SimulationGrid<E> grid) {
            E[][] neighbors = grid.getNearbyCellsAsArray(x, y, 1, 1);
            neighbors[LEFT][TOP] = null;
            neighbors[LEFT][BOTTOM] = null;
            neighbors[RIGHT][TOP] = null;
            neighbors[RIGHT][BOTTOM] = null;
            return new SimulationGrid<>(neighbors);
        }
    }

    public final class HexagonsGrid implements NeighborsGetter<SimulationGrid<E>> {
        @Override
        public SimulationGrid<E> getNeighbors(int x, int y, SimulationGrid<E> grid) {
            E[][] neighbors = grid.getNearbyCellsAsArray(x, y, 1, 1);
            if (x % 2 == 0) {
                neighbors[RIGHT][TOP] = null;
                neighbors[RIGHT][BOTTOM] = null;
            } else {
                neighbors[LEFT][TOP] = null;
                neighbors[LEFT][BOTTOM] = null;
            }
            return new SimulationGrid<>(neighbors);
        }
    }

    public final class AdjacentHexagonsGrid implements NeighborsGetter<SimulationGrid<E>> {
        @Override
        public SimulationGrid<E> getNeighbors(int x, int y, SimulationGrid<E> grid) {
            return new HexagonsGrid().getNeighbors(x, y, grid);
        }
    }

    public final class TrianglesGrid implements NeighborsGetter<SimulationGrid<E>> {
        public SimulationGrid<E> getNeighbors(int x, int y, SimulationGrid<E> grid) {
            E[][] neighbors = getNearbyCellsAsArray(x, y, 1, 2);
            if ((x + y) % 2 == 0) {
                neighbors[RIGHT][TOP + TOP] = null;
                neighbors[RIGHT][BOTTOM + BOTTOM] = null;
            } else {
                neighbors[LEFT][TOP + TOP] = null;
                neighbors[LEFT][BOTTOM + BOTTOM] = null;
            }
            return new SimulationGrid<>(neighbors);
        }
    }

    public final class AdjacentTrianglesGrid implements NeighborsGetter<SimulationGrid<E>> {
        public SimulationGrid<E> getNeighbors(int x, int y, SimulationGrid<E> grid) {
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
            return new SimulationGrid<>(neighbors);
        }
    }
}