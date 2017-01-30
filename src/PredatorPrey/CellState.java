package PredatorPrey;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Created by th174 on 1/29/2017.
 */
public final class CellState extends CellSociety.CellState {
    public static final CellState PREDATOR = new CellState(PredatorPreyState.PREDATOR);
    public static final CellState EMPTY = new CellState(PredatorPreyState.EMPTY);
    public static final CellState PREY = new CellState(PredatorPreyState.PREY);

    private enum PredatorPreyState {
        EMPTY, PREDATOR, PREY
    }

    private PredatorPreyState myState;

    private CellState(PredatorPreyState state) {
        myState = state;
    }

    public CellState(String s) {
        myState = PredatorPreyState.valueOf(s);
    }

    @Override
    public int compareTo(CellSociety.CellState state) {
        if (state instanceof PredatorPrey.CellState) {
            return getState() == ((PredatorPrey.CellState) state).getState() ? 0 : 1;
        } else {
            throw new Error("invalid state:\tExpected: " + getClass().getName() + "\tActual: " + state.getClass().getName());
        }
    }

    private PredatorPreyState getState() {
        return myState;
    }

    @Override
    public Paint getFill() {
        return getState().equals(PredatorPreyState.EMPTY) ? Color.BLUE : getState().equals(PredatorPreyState.PREDATOR) ? Color.YELLOW : Color.GREEN;
    }
}
