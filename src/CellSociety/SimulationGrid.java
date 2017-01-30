package CellSociety;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by th174 on 1/29/2017.
 */
public class SimulationGrid<E extends Cell> {
    public static int LEFT = 0;
    public static int RIGHT = 2;
    public static int TOP = 0;
    public static int BOTTOM = 2;
    public static int CENTER = 1;
    private E[][] cells;
    private Class<E> cellType;


    private SimulationGrid(E[][] array, Class<E> type) {
        cells = array;
        cellType = type;
    }

    /**
     * Create a grid from a 2 dimensional array of cells. All null values in grid will be initialized to a default Class and default State
     * @param array 2 dimensional array of cells
     * @param type default type of cell to populate grid
     * @param baseState default state to populate grid
     */
    public SimulationGrid(E[][] array, Class<E> type, CellState baseState) {
        cells = array;
        cellType = type;
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                if (cells[i][j] == null) {
                    try {
                        cells[i][j] = cellType.getDeclaredConstructor(int.class, int.class, CellState.class, SimulationGrid.class).newInstance(i, j, baseState, this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    cells[i][j].setParentGrid(this);
                }
            }
        }
    }

    /**
     * Returns up to 8 neighbors of the cell at x,y in a 3 by 3 grid; the center of the cell is null
     *
     * @param x
     * @param y
     * @return 3 by 3 Grid
     */
    public SimulationGrid<E> getNeighbors(int x, int y) {
        E[][] neighbors = (E[][]) Array.newInstance(cellType, 3, 3);
        for (int i = 0; i < neighbors.length; i++) {
            for (int j = 0; j < neighbors[i].length; j++) {
                neighbors[i][j] = get(x + i - 1, y + j - 1);
            }
        }
        neighbors[CENTER][CENTER] = null;
        return new SimulationGrid<>(neighbors, cellType);
    }

    /**
     * Get the cell at coordinates x, y on grid
     *
     * @param x
     * @param y
     * @return Cell
     */
    public E get(int x, int y) {
        return (x >= 0 && x < cells.length && y >= 0 && y < cells[x].length) ? cells[x][y] : null;
    }

    /**
     * Returns this group as a collection
     *
     * @return Collection of Cells
     */
    public Collection<E> asCollection() {
        return Arrays.stream(cells).flatMap(Arrays::stream).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    /**
     * Apply a method to each cell in the grid
     *
     * @param method
     */
    public void forEach(Consumer<E> method) {
        Arrays.stream(cells).flatMap(Arrays::stream).filter(Objects::nonNull).forEach(method);
    }

    /**
     * Get width of grid (x-dimension)
     * @return width
     */
    public int getWidth() {
        return cells.length;
    }

    /**
     * Get height of grid (y-dimension)
     * @return height
     */
    public int getHeight() {
        return cells[1].length;
    }
}
