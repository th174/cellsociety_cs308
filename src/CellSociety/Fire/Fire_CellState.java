package CellSociety.Fire;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Created by th174 on 1/29/2017.
 */
public final class Fire_CellState extends AbstractDiscrete_CellState {
    public static final Fire_CellState BURNING = new Fire_CellState(FireState.BURNING);
    public static final Fire_CellState EMPTY = new Fire_CellState(FireState.EMPTY);
    public static final Fire_CellState TREE = new Fire_CellState(FireState.TREE);
    private FireState myState;

    private Fire_CellState(FireState state) {
        myState = state;
    }

    public Fire_CellState(String s) {
        myState = s.equals("rand") ? randomState(myState) : FireState.valueOf(s);
    }

    protected FireState getState() {
        return myState;
    }

    @Override
    public Paint getFill() {
        return getState().equals(FireState.EMPTY) ? Color.YELLOW : getState().equals(FireState.TREE) ? Color.GREEN : Color.RED;
    }

    private enum FireState {
        EMPTY, TREE, BURNING
    }
}
