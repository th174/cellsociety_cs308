package CellSociety.PredatorPrey;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;

/**
 * This class models a single CellState of a single Cell in the PredatorPrey simulation.
 * Note: This class is immutable. All fields MUST be declared final.
 *
 * @see CellSociety.AbstractDiscrete_CellState
 */
public final class PredatorPrey_CellState extends AbstractDiscrete_CellState<PredatorPrey_CellState, PredatorPrey_CellState.PredatorPreyState> {
    public static final PredatorPrey_CellState PREDATOR = new PredatorPrey_CellState(PredatorPreyState.PREDATOR, Integer.MAX_VALUE, Integer.MAX_VALUE);
    public static final PredatorPrey_CellState EMPTY = new PredatorPrey_CellState(PredatorPreyState.EMPTY, Integer.MAX_VALUE, Integer.MAX_VALUE);
    public static final PredatorPrey_CellState PREY = new PredatorPrey_CellState(PredatorPreyState.PREY, Integer.MAX_VALUE, Integer.MAX_VALUE);
    private static final int DEFAULT_MAX_REPRODUCTION_TIMER=5;
    private static final int DEFAULT_MAX_STARVATION_TIMER=10;

    private final int maxReproductionTimer;
    private final int maxStarvationTimer;
    private final int starvationTimer;
    private final int reproductionTimer;

    private PredatorPrey_CellState(PredatorPreyState state, int maxReproduceTime, int maxStarvationTime) {
        super(state);
        maxReproductionTimer = maxReproduceTime;
        maxStarvationTimer = maxStarvationTime;
        reproductionTimer = maxReproductionTimer;
        starvationTimer = maxStarvationTimer;
    }

    /**
     * Constructs new PredatorPrey_CellState from a parent PredatorPrey_CellState. The new PredatorPrey_CellState inherits all attributes from the parent except for reproduceTime and starveTime
     *
     * @param parent        Parent Cell State
     * @param reproduceTime Turns until reproduction, or negative one for one less than parent value
     * @param starveTime    Turns until starvation, or negative one for one less than parent value
     */
    public PredatorPrey_CellState(PredatorPrey_CellState parent, int reproduceTime, int starveTime) {
        this(parent.getState(), reproduceTime == -1 ? parent.reproductionTimer + reproduceTime : reproduceTime, starveTime == -1 ? parent.starvationTimer + starveTime : starveTime);
    }

    /**
     * Constructs new PredatorPrey_CellState with String properties read from XML file.
     *
     * @param params String parameters from XML file
     * @see #PredatorPrey_CellState(PredatorPreyState, int, int)
     */
    public PredatorPrey_CellState(String... params) {
        this(params[0].toLowerCase().equals("rand") ? randomState(PredatorPreyState.class) : PredatorPreyState.valueOf(params[0].toUpperCase()),
                params.length > 1 ? Integer.parseInt(params[1]) * 2 : Integer.MAX_VALUE,
                params.length > 2 ? Integer.parseInt(params[2]) * 2 : Integer.MAX_VALUE);
    }

    /**
     * Generates the successor state with the same attributes as the current state but with the timers decremented.
     *
     * @return sucessorState
     */
    @Override
    public PredatorPrey_CellState getSuccessorState() {
        return new PredatorPrey_CellState(this, -1, -1);
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
     *
     * @see CellSociety.Abstract_CellState#getFill()
     */
    @Override
    public Color getFill() {
        return getState().equals(PredatorPreyState.EMPTY) ? Color.BLUE : 
        	getState().equals(PredatorPreyState.PREDATOR) ? Color.YELLOW : Color.GREEN;
    }

    /**
     * @return true if the starvationTimer is <= 0
     */
    boolean willStarve() {
        return starvationTimer <= 0;
    }

    /**
     * @see CellSociety.AbstractDiscrete_CellState#toString()
     */
    @Override
    public String toString() {
        return super.toString() + String.format("\n\t\t<ReproductionTimer>%d</ReproductionTimer>\n\t\t<StarvationTimer>%d</StarvationTimer>", maxReproductionTimer, maxStarvationTimer);
    }

    enum PredatorPreyState {
        EMPTY, PREDATOR, PREY
    }
}
