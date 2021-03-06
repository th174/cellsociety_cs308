package CellSociety.LangtonsLoop;

import CellSociety.Abstract_Cell;
import CellSociety.Grids.SimulationGrid;
import CellSociety.LangtonsLoop.LangtonsLoop_CellState.LangtonsLoopState;

import java.util.List;
import java.util.stream.Collectors;

public class LangtonsLoop_Cell extends Abstract_Cell<LangtonsLoop_Cell, LangtonsLoop_CellState> {

    private int xDirection;
    private int yDirection;

    /**
     * Constructs new cell
     * @param x
     * @param y
     * @param state
     */
    public LangtonsLoop_Cell(int x, int y, LangtonsLoop_CellState state) {
        super(x, y, state);
        xDirection = 0;
        yDirection = 1;
    }

    /** 
     * beginning implementation of how the cells interact. Cells propogate forward but 
     * according to the rules of each cell, they may or may not change the layout of the entire loop
     * @see CellSociety.Abstract_Cell#interact()
     */
    @Override
    public void interact() {
        if (shouldMove()) {
        }
        SimulationGrid<LangtonsLoop_Cell, LangtonsLoop_CellState> neighborsGrid = getNeighbors();
        List<LangtonsLoop_Cell> adjNeighbors = getNeighbors().stream().collect(Collectors.toList());
        LangtonsLoop_Cell nextCell = neighborsGrid.get(xDirection, yDirection);
        if (needToExpandSheath(adjNeighbors)) {
            expandSheath(adjNeighbors);
        }
        if (isAtEnd(nextCell)) {
            doAction(nextCell);
        } else {
            if (!nextCell.getNextState().equals(LangtonsLoopState.SHEATH)) nextCell.setNextState(getCurrentState());
        }
    }

    private boolean shouldMove() {
        return false;
    }

    /**
     * @param nextCell
     * @return true if the next cell is a sheath cell, i.e. the end of the loop
     */
    public boolean isAtEnd(LangtonsLoop_Cell nextCell) {
        return nextCell.getCurrentState().equals(LangtonsLoopState.SHEATH);
    }

    /**
     * cells act according to their properties
     * @param nextCell
     */
    public void doAction(LangtonsLoop_Cell nextCell) {
        if (getCurrentState().equals(LangtonsLoopState.GROWTH)) {
            nextCell.setNextState(new LangtonsLoop_CellState(LangtonsLoopState.CORE));
        }
        if (getCurrentState().equals(LangtonsLoopState.TURN)) {
            //set left to core, which will rebuild int he next turn
        }
    }

    private boolean needToExpandSheath(List<LangtonsLoop_Cell> adjNeighbors) {
        return adjNeighbors.stream().filter(e -> e.getCurrentState().equals(LangtonsLoopState.EMPTY)).count() >= 3;
    }

    /**
     * expands sheath to account of the rules
     * @param adjNeighbors
     */
    public void expandSheath(List<LangtonsLoop_Cell> adjNeighbors) {
        //need to change the empty cells to sheath
        for (LangtonsLoop_Cell cell : adjNeighbors) {
            if (cell.getCurrentState().equals(LangtonsLoopState.EMPTY)) {
                cell.setNextState(new LangtonsLoop_CellState(LangtonsLoopState.SHEATH));
            }
        }
    }

}
