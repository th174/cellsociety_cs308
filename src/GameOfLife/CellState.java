package GameOfLife;


import javafx.scene.paint.Color;

/**
 * Created by th174 on 1/29/2017.
 */
public final class CellState extends CellSociety.CellState {
    public static final CellState DEAD = new CellState(GameOfLifeState.DEAD);
    public static final CellState ALIVE = new CellState(GameOfLifeState.ALIVE);

    private enum GameOfLifeState {
        ALIVE, DEAD
    }

    private GameOfLifeState myState;

    private CellState(GameOfLifeState state) {
        myState = state;
    }

    public CellState(String s) {
        myState = GameOfLifeState.valueOf(s);
    }

    @Override
    public int compareTo(CellSociety.CellState state) {
        if (state instanceof GameOfLife.CellState) {
            return getState() == ((GameOfLife.CellState) state).getState() ? 0 : 1;
        } else {
            throw new Error("invalid state:\tExpected: " + getClass().getName() + "\tActual: " + state.getClass().getName());
        }
    }

    @Override
    public Color getFill() {
        return getState().equals(GameOfLifeState.ALIVE) ? Color.GREEN : Color.WHITE;
    }

    private GameOfLifeState getState() {
        return myState;
    }

    @Override
    public String toString() {
        return myState.toString();
    }
}
