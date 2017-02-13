package CellSociety.Segregation;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;

/**
 * Created by th174 on 1/29/2017.
 */
public class Segregation_CellState extends AbstractDiscrete_CellState<Segregation_CellState, Segregation_CellState.SegregationState> {
    private static final double DEFAULT_SATISFACTORY_THRESHOLD = 0.5;
    public static final Segregation_CellState X = new Segregation_CellState(SegregationState.X, DEFAULT_SATISFACTORY_THRESHOLD);
    public static final Segregation_CellState O = new Segregation_CellState(SegregationState.O, DEFAULT_SATISFACTORY_THRESHOLD);
    public static final Segregation_CellState EMPTY = new Segregation_CellState(SegregationState.EMPTY, DEFAULT_SATISFACTORY_THRESHOLD);
    private double satisfactionThreshold;

    private Segregation_CellState(SegregationState state, double satisfaction) {
        super(state);
        satisfactionThreshold = satisfaction;
    }

    public Segregation_CellState(String... params) {
        super(params[0].toLowerCase().equals("rand") ? randomState(SegregationState.class) : SegregationState.valueOf(params[0].toUpperCase()));
        satisfactionThreshold = params.length > 1 ? Double.parseDouble(params[1]) : DEFAULT_SATISFACTORY_THRESHOLD;
    }

    public boolean isSatisfiedByNeighbors(int sameStateNeighbors, int nonEmptyNeighbors) {
        return 1.0 * sameStateNeighbors / nonEmptyNeighbors > satisfactionThreshold;
    }

    @Override
    public Color getFill() {
        return getState().equals(SegregationState.X) ? Color.BLUE : (getState().equals(SegregationState.O) ? Color.RED : Color.WHITE);
    }

    @Override
    public Segregation_CellState getSuccessorState() {
        return new Segregation_CellState(getState(), satisfactionThreshold);
    }

    @Override
    public Segregation_CellState getInactiveState() {
        return new Segregation_CellState(SegregationState.EMPTY, satisfactionThreshold);
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format("\n\t\t<Threshold>%f</Threshold>", satisfactionThreshold);
    }

    enum SegregationState {
        X, O, EMPTY
    }
}
