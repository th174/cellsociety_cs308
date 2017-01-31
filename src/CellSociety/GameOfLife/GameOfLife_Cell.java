package CellSociety.GameOfLife;

import CellSociety.Abstract_Cell;

/**
 * Created by th174 on 1/29/2017.
 */
public class GameOfLife_Cell extends Abstract_Cell<GameOfLifeCell_State> {
    //Gotta pull out them magic numbers
    public static final int TWO = 2;
    public static final int THREE = 3;

    public GameOfLife_Cell(int x, int y, String[] params) {
        this(x, y, new GameOfLifeCell_State(params[0]));
    }

    public GameOfLife_Cell(int x, int y, GameOfLifeCell_State state) {
        super(x, y, state);
    }

    @Override
    public void interact() {
        int numAlive = (int) getNeighbors().asCollection().stream()
                .filter(e -> e.getState().equals(GameOfLifeCell_State.ALIVE))
                .count();
        if (numAlive < TWO && getState().equals(GameOfLifeCell_State.ALIVE)) {
            setState(GameOfLifeCell_State.DEAD);
        } else if (numAlive <= THREE && getState().equals(GameOfLifeCell_State.ALIVE)) {
            setState(GameOfLifeCell_State.ALIVE);
        } else if (numAlive > THREE && getState().equals(GameOfLifeCell_State.ALIVE)) {
            setState(GameOfLifeCell_State.DEAD);
        } else if (numAlive == THREE && getState().equals(GameOfLifeCell_State.DEAD)) {
            setState(GameOfLifeCell_State.ALIVE);
        } else {
            setState(GameOfLifeCell_State.DEAD);
        }
    }
}
