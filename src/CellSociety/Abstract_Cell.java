package CellSociety;

import javafx.scene.shape.Rectangle;

import java.util.Objects;

/**
 * Created by th174 on 1/29/2017.
 */
public abstract class Abstract_Cell<T extends Abstract_CellState> {
    public static final double BORDER_OFFSET = 1;
    private final int xPos;
    private final int yPos;
    private final Rectangle myRectangle;
    private SimulationGrid<? extends Abstract_Cell<T>> parentGrid;
    private CellStateTimeline<T> myTimeline;

    protected Abstract_Cell(String[] args, T state) {
        this(Integer.parseInt(args[0]), Integer.parseInt(args[1]), state);
    }

    protected Abstract_Cell(int x, int y, T state) {
        xPos = x;
        yPos = y;
        myTimeline = new CellStateTimeline<>(state);
        myRectangle = new Rectangle();
    }

    /**
     * Advances currentState to nextState
     * This is the only way currentState can be modified
     */
    public void updateState() {
        myTimeline.advance();
        setState(getState());
        myRectangle.setFill(getState().getFill());
        myRectangle.setWidth(parentGrid.getScreenWidth() / parentGrid.getColumns() - BORDER_OFFSET * 2);
        myRectangle.setHeight(parentGrid.getScreenHeight() / parentGrid.getRows() - BORDER_OFFSET * 2);
        myRectangle.setX(parentGrid.getScreenWidth() * xPos / parentGrid.getColumns() + BORDER_OFFSET);
        myRectangle.setY(parentGrid.getScreenHeight() * yPos / parentGrid.getRows() + BORDER_OFFSET);
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
        return "\nxPos = " + xPos + "\t\tyPos = " + yPos + "\t\tCurrentState = " + getState() + " \t\tNextState = " + getNextState() + "\t\tFill: + " + myRectangle.getFill();
    }

    /**
     * Graphical representation of the cell in JavaFX
     *
     * @return javafx Rectangle
     */
    public Rectangle getRectangle() {
        return myRectangle;
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
}
