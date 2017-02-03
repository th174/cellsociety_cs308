package CellSociety.Segregation;

import CellSociety.Abstract_Cell;

import java.util.Collection;
import java.util.stream.Collectors;


/**
 * Created by th174 on 1/29/2017.
 */
public class Segregation_Cell extends Abstract_Cell<SegregationCell_State> {
    public static final double DEFAULT_SATISFACTORY_THRESHOLD = 0.5;
    private double satisfactionThreshold;

    public Segregation_Cell(int x, int y, String[] params) {
        this(x, y, new SegregationCell_State(params[0]), DEFAULT_SATISFACTORY_THRESHOLD);
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
        if (!getState().equals(SegregationCell_State.EMPTY)) {
            double sameStateNeighbors = getNeighbors().asCollection().parallelStream().filter(e -> e.getState().equals(getState())).count();
            double totalStateNeighbors = getNeighbors().asCollection().parallelStream().filter(e -> !e.getState().equals(SegregationCell_State.EMPTY)).count();
            if (sameStateNeighbors / totalStateNeighbors < satisfactionThreshold) {
                Collection<Abstract_Cell<SegregationCell_State>> EmptyCells = getParentGrid().asCollection().parallelStream().filter(e -> e instanceof Segregation_Cell && ((Segregation_Cell) e).nextStateEmpty()).collect(Collectors.toSet());
                EmptyCells.stream().skip((long) (EmptyCells.size() * Math.random())).findAny().ifPresent(e -> move(e, SegregationCell_State.EMPTY));
            }
        }
    }

    private boolean nextStateEmpty() {
        return getNextState().equals(SegregationCell_State.EMPTY);
    }
}