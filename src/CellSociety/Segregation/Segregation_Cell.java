package CellSociety.Segregation;

import CellSociety.Abstract_Cell;

import java.util.Collection;
import java.util.stream.Collectors;


/**
 * Created by th174 on 1/29/2017.
 */
public class Segregation_Cell extends Abstract_Cell<Segregation_Cell, Segregation_CellState> {

    /**
     * Creates new segregation cell according to the x,y coordinates and values given in params
     * @param x
     * @param y
     * @param params
     */
    public Segregation_Cell(int x, int y, String... params) {
        this(x, y, new Segregation_CellState(params));
    }

    /**
     * Creates new segregation cell according to the x,y, coordinates and the given state
     * @param x
     * @param y
     * @param state
     */
    public Segregation_Cell(int x, int y, Segregation_CellState state) {
        super(x, y, state);
    }

    /** 
     * A given cell will either move to a random cell or stay where it is based on its current values.
     * Namely, is the proportion of neighbors of the same type are above its threshold, it will not move.
     * @see CellSociety.Abstract_Cell#interact()
     */
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