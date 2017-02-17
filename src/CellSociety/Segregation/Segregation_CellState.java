package CellSociety.Segregation;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;

/**
 * This class models a single CellState of a single Cell in the Segregation simulation.
 * <p>
 * Note: This class is immutable. All fields MUST be declared final.
 * <p>
 * Created by th174 on 1/29/2017.
 *
 * @see CellSociety.AbstractDiscrete_CellState
 */
public class Segregation_CellState extends AbstractDiscrete_CellState<Segregation_CellState, Segregation_CellState.SegregationState> {
    private static final double DEFAULT_SATISFACTORY_THRESHOLD = 0.5;
    public static final Segregation_CellState X = new Segregation_CellState(SegregationState.X, DEFAULT_SATISFACTORY_THRESHOLD);
    public static final Segregation_CellState O = new Segregation_CellState(SegregationState.O, DEFAULT_SATISFACTORY_THRESHOLD);
    public static final Segregation_CellState EMPTY = new Segregation_CellState(SegregationState.EMPTY, DEFAULT_SATISFACTORY_THRESHOLD);
    private final double satisfactionThreshold;

    private Segregation_CellState(SegregationState state, double satisfaction) {
        super(state);
        satisfactionThreshold = satisfaction;
    }

    /**
     * Constructs new Segregation_CellState with String properties read from XML file
     *
     * @param params String parameters read from XML file
     * @see #Segregation_CellState(SegregationState, double)
     */
    public Segregation_CellState(String... params) {
        this(params[0].toLowerCase().equals("rand") ? randomState(SegregationState.class) : SegregationState.valueOf(params[0].toUpperCase()),
                params.length > 1 ? Double.parseDouble(params[1]) : DEFAULT_SATISFACTORY_THRESHOLD);
    }

    /**
     * @param sameStateNeighbors Number of neighbors that share the same state
     * @param nonEmptyNeighbors  Total number of non-Empty neighbors
     * @return true if the ratio of same/all neighbors is greater than the satisfaction threshold
     */
    public boolean isSatisfiedByNeighbors(int sameStateNeighbors, int nonEmptyNeighbors) {
        return 1.0 * sameStateNeighbors / nonEmptyNeighbors > satisfactionThreshold;
    }

    /**
     * @return Color.BLUE if cell is X, Color.RED if O, and Color.WHITE if empty
     * @see CellSociety.Abstract_CellState#getFill()
     */
    @Override
    public Color getFill() {
        return getState().equals(SegregationState.X) ? Color.BLUE : (getState().equals(SegregationState.O) ? Color.RED : Color.WHITE);
    }

    /**
     * @return successor state that inherits the same satisfaction threshold as its parent
     * @see CellSociety.Abstract_CellState#getSuccessorState()
     */
    @Override
    public Segregation_CellState getSuccessorState() {
        return new Segregation_CellState(getState(), satisfactionThreshold);
    }


    /**
     * @return XML string representation of this CellState
     * @see CellSociety.AbstractDiscrete_CellState#toString()
     */
    @Override
    public String toString() {
        return super.toString() + String.format("\n\t\t<Threshold>%f</Threshold>", satisfactionThreshold);
    }

    enum SegregationState {
        X, O, EMPTY
    }
}
