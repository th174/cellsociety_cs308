package CellSociety.Segregation;

import CellSociety.Abstract_Cell;

import java.util.Collection;
import java.util.stream.Collectors;


/**
 * Created by th174 on 1/29/2017.
 */
public class Segregation_Cell extends Abstract_Cell<Segregation_Cell, Segregation_CellState> {

    public Segregation_Cell(int x, int y, String... params) {
        this(x, y, new Segregation_CellState(params));
    }

    public Segregation_Cell(int x, int y, Segregation_CellState state) {
        super(x, y, state);
    }

    @Override
    public void interact() {
        if (!getCurrentState().equals(Segregation_CellState.EMPTY)) {
            int sameStateNeighbors = (int) getNeighbors().parallelStream().filter(e -> e.getCurrentState().equals(getCurrentState())).count();
            int totalStateNeighbors = (int) getNeighbors().parallelStream().filter(e -> !e.getCurrentState().equals(Segregation_CellState.EMPTY)).count();
            if (!getCurrentState().isSatisfiedByNeighbors(sameStateNeighbors, totalStateNeighbors)) {
                Collection<Segregation_Cell> emptyCells = getParentGrid().stream().filter(e -> e.getNextState().equals(Segregation_CellState.EMPTY)).collect(Collectors.toSet());
                emptyCells.stream().skip((long) (emptyCells.size() * Math.random())).findAny().ifPresent(this::move);
            }
        }
    }

    private void move(Segregation_Cell cell) {
        cell.setNextState(getCurrentState());
        setNextState(Segregation_CellState.EMPTY);
    }
}