package CellSociety;

import CellSociety.Grids.Abstract_SimulationGrid;
import CellSociety.Grids.Square_SimulationGrid;

import java.util.Objects;

/**
 * Created by th174 on 1/29/2017.
 */
public abstract class Abstract_Cell<T extends Abstract_CellState> {
    private final int xPos;
    private final int yPos;
    private Abstract_SimulationGrid<? extends Abstract_Cell<T>> parentGrid;
    private CellStateTimeline<T> myTimeline;

    protected Abstract_Cell(String[] args, T state) {
        this(Integer.parseInt(args[0]), Integer.parseInt(args[1]), state);
    }

    protected Abstract_Cell(int x, int y, T state) {
        xPos = x;
        yPos = y;
        myTimeline = new CellStateTimeline<>(state);
    }

    /**
     * Advances currentState to nextState
     * This is the only way currentState can be modified
     */
    public void updateState() {
        myTimeline.advance();
        setNextState((T) getCurrentState().getSuccessorState());
    }

    public abstract void interact();

    /**
     * @return Grid of neighboring cells. See SimulationGrid::getNeighbors
     */
    public Abstract_SimulationGrid<? extends Abstract_Cell<T>> getNeighbors() {
        if (Objects.nonNull(parentGrid)) {
            return parentGrid.getNeighbors(xPos, yPos);
        } else {
            return null;
        }
    }

    public Abstract_SimulationGrid<? extends Abstract_Cell<T>> getAdjNeighbors() {
        if (Objects.nonNull(parentGrid) && parentGrid instanceof Square_SimulationGrid) {
            return ((Square_SimulationGrid<? extends Abstract_Cell<T>>) parentGrid).getAdjNeighbors(xPos, yPos);
        } else {
            return null;
        }
    }

    public void seek(int index) {
        myTimeline.seek(index);
    }

    public void reverse() {
        myTimeline.reverse();
    }

    protected Abstract_SimulationGrid<? extends Abstract_Cell<T>> getParentGrid() {
        return parentGrid;
    }

    /**
     * Set the SimulationGrid this cell is a part of
     *
     * @param grid parent SimulationGrid
     */
    public <U extends Abstract_Cell<T>> void setParentGrid(Abstract_SimulationGrid<U> grid) {
        parentGrid = grid;
    }

    /**
     * @return currentState of cell
     */
    public T getCurrentState() {
        return myTimeline.getCurrentState();
    }

    /**
     * Sets the state that this cell will change into the next time updateState is called
     *
     * @param state CellState on next update
     */
    public void setNextState(T state) {
        myTimeline.setNextState(state);
    }

    public int getTimelineIndex() {
        return myTimeline.getIndex();
    }

    protected T getNextState() {
        return myTimeline.getNextState();
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    /**
     * @return Debug String representation
     */
    public String toString() {
        return "\nxPos = " + xPos + "\t\tyPos = " + yPos + "\t\tCurrentState = " + getCurrentState() + " \t\tNextState = " + getNextState();
    }
}
