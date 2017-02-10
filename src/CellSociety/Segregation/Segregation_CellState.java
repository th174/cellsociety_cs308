package CellSociety.Segregation;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Created by th174 on 1/29/2017.
 */
public class Segregation_CellState extends AbstractDiscrete_CellState<Segregation_CellState.SegregationState> {
    public static final Segregation_CellState X = new Segregation_CellState(SegregationState.X);
    public static final Segregation_CellState O = new Segregation_CellState(SegregationState.O);
    public static final Segregation_CellState EMPTY = new Segregation_CellState(SegregationState.EMPTY);

    private Segregation_CellState(SegregationState state) {
        super(state);
    }

    public Segregation_CellState(String s) {
        super(s.toLowerCase().equals("rand") ? randomState(SegregationState.class) : SegregationState.valueOf(s.toUpperCase()));
    }

    @Override
    public Paint getFill() {
        return getState().equals(SegregationState.X) ? Color.BLUE : (getState().equals(SegregationState.O) ? Color.RED : Color.WHITE);
    }

    @Override
    public Segregation_CellState getSuccessorState() {
        return new Segregation_CellState(getState());
    }

    protected enum SegregationState {
        X, O, EMPTY
    }
}
