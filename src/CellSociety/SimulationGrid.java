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
public class SimulationGrid<E extends Abstract_Cell> {
    public static int LEFT = 0;
    public static int RIGHT = 2;
    public static int TOP = 0;
    public static int BOTTOM = 2;
    public static int CENTER = 1;
    private Abstract_Cell[][] cells;
    private Class<E> cellType;
    private double screenWidth;
    private double screenHeight;

    private SimulationGrid(E[][] array, Class<E> type) {
        cells = array;
        cellType = type;
    }

    public SimulationGrid(String[][][] paramsArray, Class<E> type) {
        cellType = type;
        cells = new Abstract_Cell[paramsArray.length][paramsArray[0].length];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                try {
                    String[] temp = Arrays.copyOf(paramsArray[i][j], paramsArray[i][j].length);
                    set(i, j, cellType.getConstructor(int.class, int.class, String[].class).newInstance(i, j, temp));
                    cells[i][j].setParentGrid(this);
                } catch (Exception e) {
                    e.printStackTrace();
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
     * Returns up to 4 adjacent neighbors of the cell at x,y in a 3 by 3 grid; the center of the cell is null
     *
     * @param x
     * @param y
     * @return 3 by 3 Grid
     */
    public SimulationGrid<E> getAdjNeighbors(int x, int y) {
        SimulationGrid<E> neighbors = getNeighbors(x, y);
        neighbors.set(TOP, LEFT, null);
        neighbors.set(TOP, RIGHT, null);
        neighbors.set(BOTTOM, LEFT, null);
        neighbors.set(BOTTOM, RIGHT, null);
        return neighbors;
    }

    /**
     * Get the cell at coordinates x, y on grid
     *
     * @param x
     * @param y
     * @return AbstractCell
     */
    public E get(int x, int y) {
        return (x >= 0 && x < cells.length && y >= 0 && y < cells[x].length) ? (E) cells[x][y] : null;
    }

    private void set(int x, int y, E cell) {
        cells[x][y] = cell;
        if (Objects.nonNull(cells[x][y])) {
            cells[x][y].setParentGrid(this);
        }
    }

    /**
     * Returns this group as a collection
     *
     * @return Collection of Cells
     */
    public Collection<E> asCollection() {
        return Arrays.stream(cells).flatMap(Arrays::stream).filter(Objects::nonNull).map(e -> (E) e).collect(Collectors.toSet());
    }

    /**
     * Apply a method to each cell in the grid
     *
     * @param method
     */
    public void forEach(Consumer<E> method) {
        Arrays.stream(cells).flatMap(Arrays::stream).filter(Objects::nonNull).map(e -> (E) e).forEach(method);
    }

    /**
     * Get width of grid (x-dimension)
     *
     * @return width
     */
    public int getWidth() {
        return cells.length;
    }

    /**
     * Get height of grid (y-dimension)
     *
     * @return height
     */
    public int getHeight() {
        return cells[1].length;
    }

    /**
     * @return width of the simulation in pixels
     */
    public double getScreenWidth() {
        return screenWidth;
    }

    /**
     * @return height of the simulation in pixels
     */
    public double getScreenHeight() {
        return screenHeight;
    }

    /**
     * @param width  Width of simulation in pixels
     * @param height Height of simulation in pixels
     */
    public void setWindowDimensions(double width, double height) {
        screenWidth = width;
        screenHeight = height;
    }
}
