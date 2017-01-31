package CellSociety.PredatorPrey;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Created by th174 on 1/29/2017.
 */
public final class PredatorPreyCell_State extends AbstractDiscrete_CellState {
    public static final PredatorPreyCell_State PREDATOR = new PredatorPreyCell_State(PredatorPreyState.PREDATOR);
    public static final PredatorPreyCell_State EMPTY = new PredatorPreyCell_State(PredatorPreyState.EMPTY);
    public static final PredatorPreyCell_State PREY = new PredatorPreyCell_State(PredatorPreyState.PREY);
    private PredatorPreyState myState;

    private PredatorPreyCell_State(PredatorPreyState state) {
        myState = state;
    }

    public PredatorPreyCell_State(String s) {
        myState = s.equals("rand") ? randomState(myState) : PredatorPreyState.valueOf(s);
    }

    protected PredatorPreyState getState() {
        return myState;
    }

    @Override
    public Paint getFill() {
        return getState().equals(PredatorPreyState.EMPTY) ? Color.BLUE : getState().equals(PredatorPreyState.PREDATOR) ? Color.YELLOW : Color.GREEN;
    }

    private enum PredatorPreyState {
        EMPTY, PREDATOR, PREY
    }
}
