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

    public E get(int x, int y) {
        return (x >= 0 && x < cells.length && y >= 0 && y < cells[x].length) ? cells[x][y] : null;
    }

    public Collection<E> asCollection() {
        return Arrays.stream(cells).flatMap(Arrays::stream).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public void forEach(Consumer<E> method) {
        Arrays.stream(cells).flatMap(Arrays::stream).forEach(method);
    }

    public int getWidth() {
        return cells.length;
    }

    public int getHeight() {
        return cells[1].length;
    }
}
