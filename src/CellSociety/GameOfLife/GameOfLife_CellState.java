package CellSociety.GameOfLife;


import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;

/**
 * Created by th174 on 1/29/2017.
 */
public final class GameOfLife_CellState extends AbstractDiscrete_CellState {
    public static final GameOfLife_CellState DEAD = new GameOfLife_CellState(GameOfLifeState.DEAD);
    public static final GameOfLife_CellState ALIVE = new GameOfLife_CellState(GameOfLifeState.ALIVE);
    private GameOfLifeState myState;

    private GameOfLife_CellState(GameOfLifeState state) {
        myState = state;
    }

    public GameOfLife_CellState(String s) {
        myState = s.equals("rand") ? randomState(GameOfLifeState.class) : GameOfLifeState.valueOf(s);
    }

    @Override
    public Color getFill() {
        return getState().equals(GameOfLifeState.ALIVE) ? Color.GREEN : Color.WHITE;
    }

    protected GameOfLifeState getState() {
        return myState;
    }
    @Override
    public GameOfLife_CellState getSuccessorState() {
        return new GameOfLife_CellState(getState());
    }

    @Override
    public String toString() {
        return myState.toString();
    }

    private enum GameOfLifeState {
        ALIVE, DEAD
    }
}
