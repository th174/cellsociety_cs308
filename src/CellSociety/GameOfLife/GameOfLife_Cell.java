package CellSociety.GameOfLife;

import CellSociety.Abstract_Cell;

/**
 * Created by th174 on 1/29/2017.
 */
public class GameOfLife_Cell extends Abstract_Cell<GameOfLife_CellState> {
    //Gotta pull out them magic numbers
    public static final int TWO = 2;
    public static final int THREE = 3;

    public GameOfLife_Cell(int x, int y, String[] params) {
        this(x, y, new GameOfLife_CellState(params[0]));
    }

    public GameOfLife_Cell(int x, int y, GameOfLife_CellState state) {
        super(x, y, state);
    }

    @Override
    public void interact() {
        int numAlive = (int) getNeighbors().asCollection(GameOfLife_Cell.class).parallelStream()
                .filter(e -> e.getCurrentState().equals(GameOfLife_CellState.ALIVE))
                .count();
        if (numAlive < TWO && getCurrentState().equals(GameOfLife_CellState.ALIVE)) {
            setNextState(GameOfLife_CellState.DEAD);
        } else if (numAlive > THREE && getCurrentState().equals(GameOfLife_CellState.ALIVE)) {
            setNextState(GameOfLife_CellState.DEAD);
        } else if (numAlive == THREE && getCurrentState().equals(GameOfLife_CellState.DEAD)) {
            setNextState(GameOfLife_CellState.ALIVE);
        }
    }
}
