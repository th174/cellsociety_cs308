package CellSociety.Fire;

import CellSociety.AbstractDiscrete_CellState;
import CellSociety.Abstract_Cell;

/**
 * This class models a single cell in the Fire simulation.
 *
 * @see CellSociety.Abstract_Cell
 * Created by th174 on 1/29/2017.
 */
public class Fire_Cell extends Abstract_Cell<Fire_Cell, Fire_CellState> {

    /**
     * Constructs new Fire_Cell from XML Properties
     *
     * @param x      x-position
     * @param y      y-position
     * @param params String paramters from xml input
     * @see #Fire_Cell(int, int, Fire_CellState)
     */
    public Fire_Cell(int x, int y, String... params) {
        this(x, y, new Fire_CellState(params));
    }

    /**
     * Constructs new Fire_Cell with initial CellState
     *
     * @param x     x-position
     * @param y     y-position
     * @param state initial CellState of this Cell
     * @see Abstract_Cell#Abstract_Cell(int, int, AbstractDiscrete_CellState)
     */
    public Fire_Cell(int x, int y, Fire_CellState state) {
        super(x, y, state);
    }

    /**
     * Determines how the cells interact according to its neighboring cells
     * If any neighboring cells are burning, set next state to BURNING with probability determined by current CellState's flammability
     * If any cell is currently burning, set next state to EMPTY
     *
     * @see Abstract_Cell#interact()
     */
    @Override
    public void interact() {
        if (Math.random() < getCurrentState().getFlammability() && getNeighbors().parallelStream().anyMatch(e -> e.getCurrentState().equals(Fire_CellState.BURNING))) {
            setNextState(new Fire_CellState(Fire_CellState.BURNING, getCurrentState().getFlammability()));
        } else if (getCurrentState().equals(Fire_CellState.BURNING)) {
            setNextState(new Fire_CellState(Fire_CellState.EMPTY, getCurrentState().getFlammability()));
        }
    }
}

