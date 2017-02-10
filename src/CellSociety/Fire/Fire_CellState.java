package CellSociety.Fire;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Created by th174 on 1/29/2017.
 */
public final class Fire_CellState extends AbstractDiscrete_CellState<Fire_CellState.FireState> {
    public static final Fire_CellState BURNING = new Fire_CellState(FireState.BURNING);
    public static final Fire_CellState EMPTY = new Fire_CellState(FireState.EMPTY);
    public static final Fire_CellState TREE = new Fire_CellState(FireState.TREE);

    private Fire_CellState(FireState state) {
        super(state);
    }

    public Fire_CellState(String s) {
        super(s.toLowerCase().equals("rand") ? randomState(FireState.class) : FireState.valueOf(s.toUpperCase()));
    }

    @Override
    public Paint getFill() {
        return getState().equals(FireState.EMPTY) ? Color.YELLOW : getState().equals(FireState.TREE) ? Color.GREEN : Color.RED;
    }

    @Override
    public Fire_CellState getSuccessorState() {
        return new Fire_CellState(getState());
    }

    enum FireState {
        EMPTY, TREE, BURNING
    }
}
