package CellSociety.GameOfLife;


import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;

/**
 * Created by th174 on 1/29/2017.
 */
public final class GameOfLifeCell_State extends AbstractDiscrete_CellState {
    public static final GameOfLifeCell_State DEAD = new GameOfLifeCell_State(GameOfLifeState.DEAD);
    public static final GameOfLifeCell_State ALIVE = new GameOfLifeCell_State(GameOfLifeState.ALIVE);
    private GameOfLifeState myState;

    private GameOfLifeCell_State(GameOfLifeState state) {
        myState = state;
    }

    public GameOfLifeCell_State(String s) {
        myState = s.equals("rand") ? randomState(myState) : GameOfLifeState.valueOf(s);
    }

    @Override
    public Color getFill() {
        return getState().equals(GameOfLifeState.ALIVE) ? Color.GREEN : Color.WHITE;
    }

    protected GameOfLifeState getState() {
        return myState;
    }

    @Override
    public String toString() {
        return myState.toString();
    }

    private enum GameOfLifeState {
        ALIVE, DEAD
    }
}
