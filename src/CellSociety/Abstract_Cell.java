package CellSociety;

import javafx.scene.shape.Rectangle;

import java.util.Objects;

/**
 * Created by th174 on 1/29/2017.
 */
public abstract class Abstract_Cell<T extends Abstract_CellState> {
    public static final double BORDER_OFFSET = 1;
    private SimulationGrid<Abstract_Cell<T>> parentGrid;
    private CellStateTimeline<T> myTimeline;
    private final int xPos;
    private final int yPos;
    private final Rectangle myRectangle;

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
        myRectangle.setFill(getState().getFill());
        myRectangle.setWidth(parentGrid.getScreenWidth() / parentGrid.getWidth() - BORDER_OFFSET * 2);
        myRectangle.setHeight(parentGrid.getScreenHeight() / parentGrid.getHeight() - BORDER_OFFSET * 2);
        myRectangle.setX(parentGrid.getScreenWidth() * xPos / parentGrid.getWidth() + BORDER_OFFSET);
        myRectangle.setY(parentGrid.getScreenHeight() * yPos / parentGrid.getHeight() + BORDER_OFFSET);
    }

    public abstract void interact();

    /**
     * @return Grid of neighboring cells. See SimulationGrid::getNeighbors
     */
    public SimulationGrid<Abstract_Cell<T>> getNeighbors() {
        if (Objects.nonNull(parentGrid)) {
            return parentGrid.getNeighbors(xPos, yPos);
        } else {
            return null;
        }
    }

    public SimulationGrid<Abstract_Cell<T>> getAdjNeighbors() {
        if (Objects.nonNull(parentGrid)) {
            return parentGrid.getAdjNeighbors(xPos, yPos);
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
     * Sets the state that this cell will change into the next time updateState is called
     *
     * @param state CellState on next update
     */
    public void setState(T state) {
        myTimeline.append(state);
    }

    /**
     * @return currentState of cell
     */
    public T getState() {
        return myTimeline.getCurrentState();
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

    /**
     * Set the SimulationGrid this cell is a part of
     *
     * @param grid parent SimulationGrid
     */
    public void setParentGrid(SimulationGrid<Abstract_Cell<T>> grid) {
        parentGrid = grid;
    }

    protected SimulationGrid<Abstract_Cell<T>> getParentGrid() {
        return parentGrid;
    }

    protected T getNextState() {
        return myTimeline.getNextState();
    }
}
