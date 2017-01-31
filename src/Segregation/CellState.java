package Segregation;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Created by th174 on 1/29/2017.
 */
public class CellState extends CellSociety.CellState {
    public static final CellState X = new CellState(SegregationState.X);
    public static final CellState O = new CellState(SegregationState.O);
    public static final CellState EMPTY = new CellState(SegregationState.EMPTY);
    
    private enum SegregationState {
        X, O, EMPTY
    }

    private SegregationState myState;

    private CellState(SegregationState state) {
        myState = state;
    }

    public CellState(String s) {
        myState = SegregationState.valueOf(s);
    }

    @Override
    public int compareTo(CellSociety.CellState state) {
        if (state instanceof Segregation.CellState) {
            return getState() == ((Segregation.CellState) state).getState() ? 0 : 1;
        } else {
            throw new Error("invalid state:\tExpected: " + getClass().getName() + "\tActual: " + state.getClass().getName());
        }
    }

    private SegregationState getState() {
        return myState;
    }


    @Override
    public Paint getFill() {
        return getState().equals(SegregationState.X) ? Color.BLUE : getState().equals(SegregationState.O) ? Color.RED : Color.WHITE;
    }
}
