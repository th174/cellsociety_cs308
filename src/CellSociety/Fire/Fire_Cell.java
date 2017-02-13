package CellSociety.Fire;

import CellSociety.Abstract_Cell;

/**
 * Created by th174 on 1/29/2017.
 */
public class Fire_Cell extends Abstract_Cell<Fire_Cell, Fire_CellState> {

    /**
     * initializes new FireCell
     * @param x coordinate
     * @param y coordinate
     * @param params possible list of parameters
     */
    public Fire_Cell(int x, int y, String... params) {
        this(x, y, new Fire_CellState(params));
    }

    /**
     * @param x coordinate
     * @param y coordinate
     * @param state of this cell
     */
    public Fire_Cell(int x, int y, Fire_CellState state) {
        super(x, y, state);
    }

    /**
     * Determines how the cells interact according to its neighboring cells
     */
    @Override
    public void interact() {
        if (getNeighbors().parallelStream().anyMatch(e -> e.getCurrentState().equals(Fire_CellState.BURNING)) && Math.random() < getCurrentState().getFlammability()) {
            setNextState(new Fire_CellState(Fire_CellState.BURNING, getCurrentState().getFlammability()));
        } else if (getCurrentState().equals(Fire_CellState.BURNING)) {
            setNextState(new Fire_CellState(Fire_CellState.EMPTY, getCurrentState().getFlammability()));
        }
    }
}

