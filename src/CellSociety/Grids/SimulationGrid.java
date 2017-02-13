package CellSociety.Grids;

import CellSociety.Abstract_Cell;
import CellSociety.Abstract_CellState;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by th174 on 2/12/2017.
 */
public interface SimulationGrid<E extends Abstract_Cell<E, T>, T extends Abstract_CellState<T, ?>> {
    void update();

    E[][] getNearbyCellsAsArray(int x, int y, int distanceX, int distanceY);

    SimulationGrid<E, T> getNeighbors(int x, int y);

    int countTotalOfState(T state);

    int getCurrentIndex();

    int getMaxIndex();

    E get(int x, int y);

    void set(int x, int y, E cell);

    Stream<E> stream();

    Stream<E> parallelStream();

    void forEach(Consumer<? super E> method);

    int getColumns();

    int getRows();

    int size();

    Iterator<E> iterator();

    Set getDistinctCellStates();

    SimulationGrid<E, T> setBoundsType(BoundsHandler<SimulationGrid<E, T>> mode);

    SimulationGrid<E, T> setShapeType(NeighborsGetter<SimulationGrid<E, T>> shape);

    class CellInstantiationException extends Exception {
        public CellInstantiationException(int x, int y, Class<? extends Abstract_Cell> cellType, String initializer) {
            super("\nInstantiation of " + cellType.getSimpleName() + " at location (x,y) = (" + x + "," + y + ") failed:\nInitializer:\t" + initializer);
        }
    }
}