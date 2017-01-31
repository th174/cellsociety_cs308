package GameOfLife;

import CellSociety.AbstractCell;
import CellSociety.SimulationGrid;

/**
 * Created by th174 on 1/29/2017.
 */
public class Cell extends AbstractCell {
    //Gotta pull out them magic numbers
    public static final int TWO = 2;
    public static final int THREE = 3;

    public Cell(int x, int y, CellSociety.CellState state) {
        super(x, y, state);
    }

    @Override
    public void interact(SimulationGrid<AbstractCell> grid) {
        int numAlive = (int) getNeighbors().asCollection().stream()
                .filter(e -> e.getState().equals(CellState.ALIVE))
                .count();
        if (numAlive < TWO && getState().equals(CellState.ALIVE)) {
            setState(CellState.DEAD);
        } else if (numAlive <= THREE && getState().equals(CellState.ALIVE)) {
            setState(CellState.ALIVE);
        } else if (numAlive > THREE && getState().equals(CellState.ALIVE)) {
            setState(CellState.DEAD);
        } else if (numAlive == THREE && getState().equals(CellState.DEAD)) {
            setState(CellState.ALIVE);
        } else {
            setState(CellState.DEAD);
        }
    }
}
