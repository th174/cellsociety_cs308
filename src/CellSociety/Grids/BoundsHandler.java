package CellSociety.Grids;

import CellSociety.Abstract_Cell;
import javafx.util.Pair;

/**
 * Created by th174 on 2/9/2017.
 */
public interface BoundsHandler<E extends SimulationGrid> {
    Pair<Integer, Integer> handleBounds(int x, int y, E grid);
}
