package CellSociety.Segregation;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Created by th174 on 1/29/2017.
 */
public class SegregationCell_State extends AbstractDiscrete_CellState {
    public static final SegregationCell_State X = new SegregationCell_State(SegregationState.X);
    public static final SegregationCell_State O = new SegregationCell_State(SegregationState.O);
    public static final SegregationCell_State EMPTY = new SegregationCell_State(SegregationState.EMPTY);
    private SegregationState myState;

    private SegregationCell_State(SegregationState state) {
        myState = state;
    }

    public SegregationCell_State(String s) {
        myState = s.equals("rand") ? randomState(myState) : SegregationState.valueOf(s);
    }

    protected SegregationState getState() {
        return myState;
    }

    @Override
    public Paint getFill() {
        return getState().equals(SegregationState.X) ? Color.BLUE : getState().equals(SegregationState.O) ? Color.RED : Color.WHITE;
    }

    private enum SegregationState {
        X, O, EMPTY
    }
}
