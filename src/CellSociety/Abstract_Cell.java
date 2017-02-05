package CellSociety;

import java.util.Objects;

/**
 * Created by th174 on 1/29/2017.
 */
public abstract class Abstract_Cell<T extends Abstract_CellState> {
    private final int xPos;
    private final int yPos;
    private SimulationGrid<? extends Abstract_Cell<T>> parentGrid;
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
        setState(getState());
    }

    public abstract void interact();

    /**
     * @return Grid of neighboring cells. See SimulationGrid::getNeighbors
     */
    public <U extends Abstract_Cell<T>> SimulationGrid<U> getNeighbors() {
        if (Objects.nonNull(parentGrid)) {
            return (SimulationGrid<U>) parentGrid.getNeighbors(xPos, yPos);
        } else {
            return null;
        }
    }

    public <U extends Abstract_Cell<T>> SimulationGrid<U> getAdjNeighbors() {
        if (Objects.nonNull(parentGrid)) {
            return (SimulationGrid<U>) parentGrid.getAdjNeighbors(xPos, yPos);
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

    /**
     * @return currentState of cell
     */
    public T getState() {
        return myTimeline.getCurrentState();
    }

    /**
     * Sets the state that this cell will change into the next time updateState is called
     *
     * @param state CellState on next update
     */
    public void setState(T state) {
        myTimeline.setNextState(state);
    }

    /**
     * @return Debug String representation
     */
    public String toString() {
        return "\nxPos = " + xPos + "\t\tyPos = " + yPos + "\t\tCurrentState = " + getState() + " \t\tNextState = " + getNextState();
    }

    protected SimulationGrid<? extends Abstract_Cell<T>> getParentGrid() {
        return parentGrid;
    }

    /**
     * Set the SimulationGrid this cell is a part of
     *
     * @param grid parent SimulationGrid
     */
    public <U extends Abstract_Cell<T>> void setParentGrid(SimulationGrid<U> grid) {
        parentGrid = grid;
    }

    protected T getNextState() {
        return myTimeline.getNextState();
    }

    protected void move(Abstract_Cell<T> cell, T state) {
        cell.setState(getState());
        setState(state);
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }
}
