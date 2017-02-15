package CellSociety.GameOfLife;

import CellSociety.AbstractDiscrete_CellState;
import CellSociety.Abstract_Cell;

/**
 * This class models a single cell in the GameOfLife simulation.
 *
 * @see CellSociety.Abstract_Cell
 * Created by th174 on 1/29/2017.
 */
public class GameOfLife_Cell extends Abstract_Cell<GameOfLife_Cell, GameOfLife_CellState> {
    //Gotta pull out them magic numbers
    private static final int TWO = 2;
    private static final int THREE = 3;

    /**
     * Constructs new GameOfLife_Cell from XML Properties
     *
     * @param x      x-position
     * @param y      y-position
     * @param params String paramters from xml input
     * @see #GameOfLife_Cell(int, int, GameOfLife_CellState)
     */
    public GameOfLife_Cell(int x, int y, String... params) {
        this(x, y, new GameOfLife_CellState(params[0]));
    }

    /**
     * Constructs new GameOfLife_Cell with initial CellState
     *
     * @param x     x-position
     * @param y     y-position
     * @param state initial CellState of this Cell
     * @see Abstract_Cell#Abstract_Cell(int, int, AbstractDiscrete_CellState)
     */
    public GameOfLife_Cell(int x, int y, GameOfLife_CellState state) {
        super(x, y, state);
    }

    /**
     * This GameOfLife_Cell interacts with all other cells on its parentGrid in accordance with rules described in the GameOfLife Simulation
     *
     * @see Abstract_Cell#interact()
     */
    @Override
    public void interact() {
        int numAlive = (int) getNeighbors().parallelStream()
                .filter(e -> e.getCurrentState().equals(GameOfLife_CellState.ALIVE))
                .count();
        if (numAlive < TWO && getCurrentState().equals(GameOfLife_CellState.ALIVE)) {
            setNextState(new GameOfLife_CellState(GameOfLife_CellState.DEAD));
        } else if (numAlive > THREE && getCurrentState().equals(GameOfLife_CellState.ALIVE)) {
            setNextState(new GameOfLife_CellState(GameOfLife_CellState.DEAD));
        } else if (numAlive == THREE && getCurrentState().equals(GameOfLife_CellState.DEAD)) {
            setNextState(new GameOfLife_CellState(GameOfLife_CellState.ALIVE));
        }
    }
}
