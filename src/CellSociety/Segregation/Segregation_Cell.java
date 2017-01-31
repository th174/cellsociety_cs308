package CellSociety.Segregation;

import CellSociety.Abstract_Cell;
import CellSociety.SimulationGrid;

/**
 * Created by th174 on 1/29/2017.
 */
public class Segregation_Cell extends Abstract_Cell<SegregationCell_State> {
    public static final double DEFAULT_SATISFACTORY_THRESHOLD = 0.5;
    private double satisfactionThreshold;

    public Segregation_Cell(int x, int y, String[] params) {
        this(x,y, new SegregationCell_State(params[0]), DEFAULT_SATISFACTORY_THRESHOLD);
        if (params.length > 1) {
            satisfactionThreshold = Double.parseDouble(params[1]);
        }
    }

    public Segregation_Cell(int x, int y, SegregationCell_State state, double threshold) {
        super(x, y, state);
        satisfactionThreshold = threshold;
    }

    @Override
    public void interact() {
        //similar neighbors/total populated neighbors
        double sameStateNeighbors = getNeighbors().asCollection().stream().filter(e -> getState().equals(e.getState())).count();
        double totalStateNeighbors = getNeighbors().asCollection().stream().filter(e -> !getState().equals(SegregationCell_State.EMPTY)).count();
        if (sameStateNeighbors / totalStateNeighbors < satisfactionThreshold) {
            moveToEmpty(getParentGrid());
        }
    }

    private void moveToEmpty(SimulationGrid<Abstract_Cell<SegregationCell_State>> grid) {
        grid.asCollection().stream().filter(e -> e instanceof Segregation_Cell && ((Segregation_Cell) e).nextStateEmpty()).findAny().ifPresent(e -> {
            e.setState(this.getState());
            setState(SegregationCell_State.EMPTY);
        });
    }

    private boolean nextStateEmpty() {
        return this.getNextState().equals(SegregationCell_State.EMPTY);
    }
}