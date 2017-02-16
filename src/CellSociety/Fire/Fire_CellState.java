package CellSociety.Fire;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;

/**
 * This class models a single CellState of a single Cell in the Fire simulation.
 * <p>
 * Note: This class is immutable. All fields MUST be declared final.
 * <p>
 * Created by th174 on 1/29/2017.
 *
 * @see CellSociety.AbstractDiscrete_CellState
 */
public final class Fire_CellState extends AbstractDiscrete_CellState<Fire_CellState, Fire_CellState.FireState> {
    private static final double DEFAULT_FLAMMABILITY = 0.5;
    public static final Fire_CellState BURNING = new Fire_CellState(FireState.BURNING, DEFAULT_FLAMMABILITY);
    public static final Fire_CellState EMPTY = new Fire_CellState(FireState.EMPTY, DEFAULT_FLAMMABILITY);
    public static final Fire_CellState TREE = new Fire_CellState(FireState.TREE, DEFAULT_FLAMMABILITY);
    private final double flammability;

    private Fire_CellState(FireState state, double probCatch) {
        super(state);
        flammability = probCatch;
    }

    /**
     * Constructs new Fire_CellState with String properties read from XML file
     *
     * @param params String parameters read from XML file
     * @see #Fire_CellState(FireState, double)
     */
    public Fire_CellState(String... params) {
        this(params[0].toLowerCase().equals("rand") ? randomState(FireState.class) : FireState.valueOf(params[0].toUpperCase()),
                params.length > 1 ? Double.parseDouble(params[1]) : DEFAULT_FLAMMABILITY);
    }

    /**
     * Constructs Fire_CellState from a parent Fire_CellState
     *
     * @param parent    parent Fire_CellState
     * @param probCatch probability of the new cell catching fire
     */
    public Fire_CellState(Fire_CellState parent, double probCatch) {
        this(parent.getState(), probCatch);
    }

    /**
     * Get the probability that this CellState catches fire. Returns 0 if not a tree
     *
     * @return probability of catching fire
     */
    public double getFlammability() {
        return equals(TREE) ? flammability : 0;
    }

    /**
     * @return Graphical representation of this CellState according to tree, empty, and fire states.
     * @see AbstractDiscrete_CellState#getFill()
     */
    @Override
    public Color getFill() {
        return getState().equals(FireState.EMPTY) ? Color.YELLOW : getState().equals(FireState.TREE) ? Color.GREEN : Color.RED;
    }

    /**
     * @see CellSociety.AbstractDiscrete_CellState#getSuccessorState()
     */
    @Override
    public Fire_CellState getSuccessorState() {
        return new Fire_CellState(this, flammability);
    }

    /**
     * @see CellSociety.AbstractDiscrete_CellState#toString()
     */
    @Override
    public String toString() {
        return super.toString() + String.format("\n\t\t<Flammability>%f</Flammability", flammability);
    }

    enum FireState {
        EMPTY, TREE, BURNING
    }
}
