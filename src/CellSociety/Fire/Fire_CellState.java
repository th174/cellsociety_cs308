package CellSociety.Fire;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Created by th174 on 1/29/2017.
 */
public final class Fire_CellState extends AbstractDiscrete_CellState<Fire_CellState, Fire_CellState.FireState> {
    private static final double DEFAULT_FLAMMABILITY = 0.5;
    public static final Fire_CellState BURNING = new Fire_CellState(FireState.BURNING, DEFAULT_FLAMMABILITY);
    public static final Fire_CellState EMPTY = new Fire_CellState(FireState.EMPTY, DEFAULT_FLAMMABILITY);
    public static final Fire_CellState TREE = new Fire_CellState(FireState.TREE, DEFAULT_FLAMMABILITY);
    private double flammability;

    private Fire_CellState(FireState state, double flammability) {
        super(state);
    }

    public Fire_CellState(String... params) {
        super(params[0].toLowerCase().equals("rand") ? randomState(FireState.class) : FireState.valueOf(params[0].toUpperCase()));
        flammability = params.length > 1 ? Double.parseDouble(params[1]) : DEFAULT_FLAMMABILITY;
    }

    public double getFlammability() {
        return getState().equals(FireState.TREE) ? flammability : 0;
    }

    @Override
    public Color getFill() {
        return getState().equals(FireState.EMPTY) ? Color.YELLOW : getState().equals(FireState.TREE) ? Color.GREEN : Color.RED;
    }

    @Override
    public Fire_CellState getSuccessorState() {
        return new Fire_CellState(getState(), flammability);
    }

    @Override
    public Fire_CellState getInactiveState() {
        return new Fire_CellState(FireState.EMPTY, flammability);
    }

    enum FireState {
        EMPTY, TREE, BURNING
    }
}
