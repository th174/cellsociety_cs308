package Fire;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Created by th174 on 1/29/2017.
 */
public final class CellState extends CellSociety.CellState {
    public static final CellState BURNING = new CellState(FireState.BURNING);
    public static final CellState EMPTY = new CellState(FireState.EMPTY);
    public static final CellState TREE = new CellState(FireState.TREE);

    private enum FireState {
        EMPTY, TREE, BURNING
    }

    private FireState myState;

    private CellState(FireState state) {
        myState = state;
    }

    public CellState(String s) {
        myState = FireState.valueOf(s);
    }

    @Override
    public int compareTo(CellSociety.CellState state) {
        if (state instanceof Fire.CellState) {
            return getState() == ((Fire.CellState) state).getState() ? 0 : 1;
        } else {
            throw new Error("invalid state:\tExpected: " + getClass().getName() + "\tActual: " + state.getClass().getName());
        }
    }

    private FireState getState() {
        return myState;
    }

    @Override
    public Paint getFill() {
        return getState().equals(FireState.EMPTY) ? Color.YELLOW : getState().equals(FireState.TREE) ? Color.GREEN : Color.RED;
    }
}
