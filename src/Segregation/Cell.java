package Segregation;

import CellSociety.AbstractCell;
import CellSociety.SimulationGrid;

/**
 * Created by th174 on 1/29/2017.
 */
public class Cell extends AbstractCell {
    private double threshold;

    public Cell(int x, int y, CellSociety.CellState state, String satisfactionThresholdString) {
        super(x, y, state);
        threshold = Double.parseDouble(satisfactionThresholdString);
    }

    @Override
    public void interact(SimulationGrid<AbstractCell> grid) {
        //similar neighbors/total populated neighbors
        double sameStateNeighbors = getNeighbors().asCollection().stream().filter(e -> getState().equals(e.getState())).count();
        double totalStateNeighbors = getNeighbors().asCollection().stream().filter(e -> !getState().equals(CellState.EMPTY)).count();
        if (sameStateNeighbors / totalStateNeighbors < threshold) {
            moveToEmpty(grid);
        }
    }

    private void moveToEmpty(SimulationGrid<AbstractCell> grid) {
        grid.asCollection().stream().filter(e -> e instanceof Cell && ((Cell) e).nextStateEmpty()).findAny().ifPresent(e -> {
            e.setState(this.getState());
            setState(CellState.EMPTY);
        });
    }

    private boolean nextStateEmpty() {
        return this.getNextState().equals(CellState.EMPTY);
    }
}