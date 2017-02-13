package CellSociety;

import CellSociety.Grids.SimulationGrid;

import java.util.Objects;

/**
 * Created by th174 on 1/29/2017.
 */
public abstract class Abstract_Cell<E extends Abstract_Cell<E, T>, T extends AbstractDiscrete_CellState<T, ? extends Enum<?>>> {
    private final int xPos;
    private final int yPos;
    private SimulationGrid<E, T> parentGrid;
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
        setNextState(getCurrentState().getSuccessorState());
    }

    /**
     * Determines how the cells interact
     */
    public abstract void interact();

    /**
     * @return Grid of neighboring cells. See SimulationGrid::getNeighbors
     */
    protected SimulationGrid<E, T> getNeighbors() {
        if (Objects.nonNull(parentGrid)) {
            return parentGrid.getNeighbors(xPos, yPos);
        } else {
            return null;
        }
    }

    /**
     * Goes to the index in the timeline. Allows us to jump.
     *
     * @param index
     */
    public void seek(int index) {
        myTimeline.seek(index);
    }

    /**
     * Reverses the animation.
     */
    public void reverse() {
        myTimeline.reverse();
    }

    /**
     * @return current index of the Timeline
     */
    public int getCurrentIndex() {
        return myTimeline.getCurrentIndex();
    }

    /**
     * @return max index of the Timeline
     */
    public int getMaxIndex() {
        return myTimeline.getMaxIndex();
    }

    protected SimulationGrid<E, T> getParentGrid() {
        return parentGrid;
    }

    /**
     * Set the SimulationGrid this cell is a part of
     *
     * @param grid parent SimulationGrid
     */
    public void setParentGrid(SimulationGrid<E, T> grid) {
        parentGrid = grid;
    }

    /**
     * @return currentState of cell
     */
    public T getCurrentState() {
        return myTimeline.getCurrentState();
    }

    protected int getTimelineIndex() {
        return myTimeline.getIndex();
    }

    /**
     * @return nextState of cell
     */
    public T getNextState() {
        return myTimeline.getNextState();
    }

    /**
     * Sets the state that this cell will change into the next time updateState is called
     *
     * @param state CellState on next update
     */
    public void setNextState(T state) {
        myTimeline.setNextState(state);
    }


    public int getX() {
        return xPos;
    }

    /**
     * @return y position of the cell
     */
    public int getY() {
        return yPos;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("\n\t<Cell xPos=\"%d\" yPos=\"%d\">%s\n\t</Cell>", getX(), getY(), getCurrentState());
    }
}
