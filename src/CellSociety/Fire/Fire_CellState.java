package CellSociety.Fire;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;

import java.util.Set;

/**
 * Created by th174 on 1/29/2017.
 */
public final class Fire_CellState extends AbstractDiscrete_CellState<Fire_CellState, Fire_CellState.FireState> {
    private static final double DEFAULT_FLAMMABILITY = 0.5;
    public static final Fire_CellState BURNING = new Fire_CellState(FireState.BURNING, DEFAULT_FLAMMABILITY);
    public static final Fire_CellState EMPTY = new Fire_CellState(FireState.EMPTY, DEFAULT_FLAMMABILITY);
    public static final Fire_CellState TREE = new Fire_CellState(FireState.TREE, DEFAULT_FLAMMABILITY);
    private double flammability;

    private Fire_CellState(FireState state, double probCatch) {
        super(state);
        flammability = probCatch;
    }

    /**
     * initiates state with the given parameters
     * @param fireState of the cell
     * @param probCatch probability of the cell catching fire
     */
    public Fire_CellState(Fire_CellState fireState, double probCatch) {
        this(fireState.getState(), probCatch);
    }

    /**
     * initializes according to an unknown size of paramaters
     * @param params
     */
    public Fire_CellState(String... params) {
        super(params[0].toLowerCase().equals("rand") ? randomState(FireState.class) : FireState.valueOf(params[0].toUpperCase()));
        flammability = params.length > 1 ? Double.parseDouble(params[1]) : DEFAULT_FLAMMABILITY;
    }

    /**
     * @return value of flammability of the cell
     */
    public double getFlammability() {
        return equals(TREE) ? flammability : 0;
    }

    /**
     * @return Graphical representation of this CellState according to tree, empty, and fire states.
     */
    @Override
    public Color getFill() {
        return getState().equals(FireState.EMPTY) ? Color.YELLOW : getState().equals(FireState.TREE) ? Color.GREEN : Color.RED;
    }

    /**
     * @see CellSociety.Abstract_CellState#getSuccessorState()
     */
    @Override
    public Fire_CellState getSuccessorState() {
        return new Fire_CellState(this, flammability);
    }

    /**
     * @see CellSociety.Abstract_CellState#toString()
     */
    @Override
    public String toString() {
        return super.toString() + String.format("\n\t\t<Flammability>%f</Flammability", flammability);
    }

    enum FireState {
        EMPTY, TREE, BURNING
    }
}
