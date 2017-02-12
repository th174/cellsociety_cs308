package CellSociety.Grids;

/**
 * Created by th174 on 2/9/2017.
 */
public interface NeighborsGetter<E extends SimulationGrid> {
    E getNeighbors(int x, int y, E grid);
}
