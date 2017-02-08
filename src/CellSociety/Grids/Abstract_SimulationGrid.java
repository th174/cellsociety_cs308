package CellSociety.Grids;

import CellSociety.Abstract_Cell;
import CellSociety.Abstract_CellState;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by th174 on 2/7/2017.
 */
public abstract class Abstract_SimulationGrid<E extends Abstract_Cell> {
    public static int LEFT = 0;
    public static int RIGHT = 2;
    public static int TOP = 0;
    public static int BOTTOM = 2;
    public static int CENTER = 1;
    private final List<List<E>> cells;
    private Class<E> cellType;
    private int columns;
    private int rows;

    protected Abstract_SimulationGrid(E[][] array) {
        cells = new ArrayList<>();
        columns = array.length;
        rows = array[0].length;
        for (E[] row : array) {
            cells.add(Arrays.asList(row));
        }
    }

    public Abstract_SimulationGrid(String[][][] paramsArray, Class<E> type) throws Exception {
        cellType = type;
        cells = new ArrayList<>();
        columns = paramsArray.length;
        rows = paramsArray[0].length;
        for (int i = 0; i < paramsArray.length; i++) {
            for (int j = 0; j < paramsArray[0].length; j++) {
                cells.add(new ArrayList<>());
                String[] temp = Arrays.copyOf(paramsArray[i][j], paramsArray[i][j].length);
                cells.get(i).add(cellType.getConstructor(int.class, int.class, String[].class).newInstance(i, j, temp));
            }
        }
    }

    public void update() {
        forEach(Abstract_Cell::updateState);
        forEach(Abstract_Cell::interact);
    }

    protected E[][] getNeighborsAsArray(int x, int y) {
        E[][] neighbors = (E[][]) Array.newInstance(cellType, 3, 3);
        for (int i = 0; i < neighbors.length; i++) {
            for (int j = 0; j < neighbors[i].length; j++) {
                neighbors[i][j] = get(x + i - 1, y + j - 1);
            }
        }
        neighbors[CENTER][CENTER] = null;
        return neighbors;
    }

    public abstract Abstract_SimulationGrid<E> getNeighbors(int x, int y);

    public long countTotalOfState(Abstract_CellState state) {
        return asCollection(cellType).parallelStream().filter(e -> e.getCurrentState().equals(state)).count();
    }

    /**
     * Get the cell at coordinates x, y on grid
     *
     * @param x
     * @param y
     * @return AbstractCell
     */
    private E get(int x, int y) {
        return (x >= 0 && x < cells.size() && y >= 0 && y < cells.get(x).size()) ? cells.get(x).get(y) : null;
    }

    protected void set(int x, int y, E cell) {
        cells.get(x).set(y, cell);
        if (Objects.nonNull(cells.get(x).get(y))) {
            cells.get(x).get(y).setParentGrid(this);
        }
    }

    /**
     * Returns this group as a collection
     *
     * @return Collection of Cells
     */
    public <U> Collection<U> asCollection(Class<U> cell) {
        return cells.stream().flatMap(List::stream).parallel().filter(Objects::nonNull).filter(cell::isInstance).map(cell::cast).collect(Collectors.toSet());
    }

    /**
     * Apply a method to each cell in the grid
     *
     * @param method
     */
    public void forEach(Consumer<E> method) {
        cells.stream().flatMap(List::stream).filter(Objects::nonNull).forEach(method);
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

    public String getSimulationType() {
        return cellType.getSimpleName().split("_")[0];
    }

    public String getGridType() {
        return this.getClass().getSimpleName().split("_")[0];
    }
}