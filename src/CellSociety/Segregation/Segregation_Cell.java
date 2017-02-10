package CellSociety.Segregation;

import CellSociety.Abstract_Cell;

import java.util.Collection;
import java.util.stream.Collectors;


/**
 * Created by th174 on 1/29/2017.
 */
public class Segregation_Cell extends Abstract_Cell<Segregation_Cell,Segregation_CellState> {
    private static final double DEFAULT_SATISFACTORY_THRESHOLD = 0.5;
    private double satisfactionThreshold;

    public Segregation_Cell(int x, int y, String... params) {
        this(x, y, new Segregation_CellState(params[0]), DEFAULT_SATISFACTORY_THRESHOLD);
        if (params.length > 1) {
            satisfactionThreshold = Double.parseDouble(params[1]);
        }
    }

    public Segregation_Cell(int x, int y, Segregation_CellState state, double threshold) {
        super(x, y, state);
        satisfactionThreshold = threshold;
    }

    @Override
    public void interact() {
        if (!getCurrentState().equals(Segregation_CellState.EMPTY)) {
            double sameStateNeighbors = getNeighbors().parallelStream().filter(e -> e.getCurrentState().equals(getCurrentState())).count();
            double totalStateNeighbors = getNeighbors().parallelStream().filter(e -> !e.getCurrentState().equals(Segregation_CellState.EMPTY)).count();
            if (sameStateNeighbors / totalStateNeighbors < satisfactionThreshold) {
                Collection<Segregation_Cell> emptyCells = getParentGrid().stream().filter(e -> e.getNextState().equals(Segregation_CellState.EMPTY)).collect(Collectors.toSet());
                emptyCells.stream().skip((long) (emptyCells.size() * Math.random())).findAny().ifPresent(this::move);
            }
        }
    }

    protected void move(Segregation_Cell cell) {
        cell.setNextState(getCurrentState());
        setNextState(Segregation_CellState.EMPTY);
    }
}