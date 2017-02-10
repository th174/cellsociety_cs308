package CellSociety.PredatorPrey;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Created by th174 on 1/29/2017.
 */
public final class PredatorPrey_CellState extends AbstractDiscrete_CellState<PredatorPrey_CellState.PredatorPreyState> {
    public static final PredatorPrey_CellState PREDATOR = new PredatorPrey_CellState(PredatorPreyState.PREDATOR);
    public static final PredatorPrey_CellState EMPTY = new PredatorPrey_CellState(PredatorPreyState.EMPTY);
    public static final PredatorPrey_CellState PREY = new PredatorPrey_CellState(PredatorPreyState.PREY);
    private int maxReproductionTimer;
    private int maxStarvationTimer;
    private int starvationTimer;
    private int reproductionTimer;

    private PredatorPrey_CellState(PredatorPreyState state) {
        super(state);
    }

    public PredatorPrey_CellState(PredatorPrey_CellState parent, int reproduceTime, int starveTime) {
        this(parent.getState());
        maxReproductionTimer = parent.maxReproductionTimer;
        maxStarvationTimer = parent.maxStarvationTimer;
        reproductionTimer = reproduceTime == -1 ? parent.reproductionTimer + reproduceTime : reproduceTime;
        starvationTimer = starveTime == -1 ? parent.starvationTimer + starveTime : starveTime;
    }

    public PredatorPrey_CellState(String... params) {
        super(params[0].toLowerCase().equals("rand") ? randomState(PredatorPreyState.class) : PredatorPreyState.valueOf(params[0].toUpperCase()));
        if (params.length > 1) {
            maxReproductionTimer = Integer.parseInt(params[1]) * 2;
        }
        if (params.length > 2) {
            maxStarvationTimer = Integer.parseInt(params[2]) * 2;
        }
        reproductionTimer = maxReproductionTimer;
        starvationTimer = maxStarvationTimer;
    }

    @Override
    public PredatorPrey_CellState getSuccessorState() {
        return new PredatorPrey_CellState(this, reproductionTimer - 1, starvationTimer - 1);
    }

    boolean canReproduce() {
        return reproductionTimer <= 0;
    }

    int getMaxReproductionTimer() {
        return maxReproductionTimer;
    }

    int getMaxStarvationTimer() {
        return maxStarvationTimer;
    }

    @Override
    public Paint getFill() {
        return getState().equals(PredatorPreyState.EMPTY) ? Color.BLUE : getState().equals(PredatorPreyState.PREDATOR) ? Color.YELLOW : Color.GREEN;
    }

    boolean willStarve() {
        return starvationTimer <= 0;
    }

    enum PredatorPreyState {
        EMPTY, PREDATOR, PREY
    }
}
