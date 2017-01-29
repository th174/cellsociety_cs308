package GameOfLife;

import CellSociety.SimulationGrid;
import javafx.scene.paint.Color;

/**
 * Created by th174 on 1/29/2017.
 */
public class Cell extends CellSociety.Cell {
    //IDK wtf this is, but magic numbers man
    public static final int TWO = 2;
    public static final int THREE = 3;

    public Cell(CellSociety.CellState state, SimulationGrid grid) {
        super(state, grid);
    }

    public Cell(int x, int y, CellSociety.CellState state, SimulationGrid grid) {
        super(x, y, state, grid);
    }

    @Override
    public void interact(SimulationGrid<CellSociety.Cell> grid) {
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
        super.interact(grid);
        setFill(getState().equals(CellState.ALIVE) ? Color.GREEN : Color.RED);
    }
}
