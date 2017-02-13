package CellSociety.PredatorPrey;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;

/**
 * Created by th174 on 1/29/2017.
 */
public final class PredatorPrey_CellState extends AbstractDiscrete_CellState<PredatorPrey_CellState, PredatorPrey_CellState.PredatorPreyState> {
    public static final PredatorPrey_CellState PREDATOR = new PredatorPrey_CellState(PredatorPreyState.PREDATOR, Integer.MAX_VALUE, Integer.MAX_VALUE);
    public static final PredatorPrey_CellState EMPTY = new PredatorPrey_CellState(PredatorPreyState.EMPTY, Integer.MAX_VALUE, Integer.MAX_VALUE);
    public static final PredatorPrey_CellState PREY = new PredatorPrey_CellState(PredatorPreyState.PREY, Integer.MAX_VALUE, Integer.MAX_VALUE);
    private int maxReproductionTimer;
    private int maxStarvationTimer;
    private int starvationTimer;
    private int reproductionTimer;

    private PredatorPrey_CellState(PredatorPreyState state, int maxReproduceTime, int maxStarvationTime) {
        super(state);
        maxReproductionTimer = maxReproduceTime;
        maxStarvationTimer = maxStarvationTime;
    }

    /**
     * Initializes new PredatorPrey_CellState of the given parameters. Inherits attirbutes from parent.
     * @param parent
     * @param reproduceTime
     * @param starveTime
     */
    public PredatorPrey_CellState(PredatorPrey_CellState parent, int reproduceTime, int starveTime) {
        this(parent.getState(), parent.maxReproductionTimer, parent.maxStarvationTimer);
        reproductionTimer = reproduceTime == -1 ? parent.reproductionTimer + reproduceTime : reproduceTime;
        starvationTimer = starveTime == -1 ? parent.starvationTimer + starveTime : starveTime;
    }

    /**
     * Initializes new PredatorPrey_CellState with the given params of unknown length.
     * Default values will be used if params does not include values for the timers
     * maxReproductionTimer, maxStartvationTimer, reproductionTimer, starvationTimer
     * @param params
     */
    public PredatorPrey_CellState(String... params) {
        super(params[0].toLowerCase().equals("rand") ? randomState(PredatorPreyState.class) : PredatorPreyState.valueOf(params[0].toUpperCase()));
        maxReproductionTimer = params.length > 1 ? Integer.parseInt(params[1]) * 2 : Integer.MAX_VALUE;
        maxStarvationTimer = params.length > 2 ? Integer.parseInt(params[2]) * 2 : Integer.MAX_VALUE;
        reproductionTimer = maxReproductionTimer;
        starvationTimer = maxStarvationTimer;
    }

    /** 
     * Generates the successor state with the same attributes as the current state but with the timers decremented.
     * @return sucessorState
     */
    @Override
    public PredatorPrey_CellState getSuccessorState() {
        return new PredatorPrey_CellState(this, reproductionTimer - 1, starvationTimer - 1);
    }


    /**
     * @return true if the reproduction timer is less than 0
     */
    boolean canReproduce() {
        return reproductionTimer <= 0;
    }

    /**
     * @return maxReproductionTimer of the cell
     */
    int getMaxReproductionTimer() {
        return maxReproductionTimer;
    }

    /**
     * @return maxStarvationTimer of the cell
     */
    int getMaxStarvationTimer() {
        return maxStarvationTimer;
    }

    /** 
     * Returns color of the cell; blue if empty, yellow if predator, green if prey
     * @see CellSociety.Abstract_CellState#getFill()
     */
    @Override
    public Color getFill() {
        return getState().equals(PredatorPreyState.EMPTY) ? Color.BLUE : getState().equals(PredatorPreyState.PREDATOR) ? Color.YELLOW : Color.GREEN;
    }

    /**
     * @return true if the starvationTimer is <= 0
     */
    boolean willStarve() {
        return starvationTimer <= 0;
    }

    /* (non-Javadoc)
     * @see CellSociety.Abstract_CellState#toString()
     */
    @Override
    public String toString() {
        return super.toString() + String.format("\n\t\t<ReproductionTimer>%d</ReproductionTimer>\n\t\t<StarvationTimer>%d</StarvationTimer>", maxReproductionTimer, maxStarvationTimer);
    }

    enum PredatorPreyState {
        EMPTY, PREDATOR, PREY
    }
}
