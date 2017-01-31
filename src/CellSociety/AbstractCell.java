package CellSociety;

import javafx.scene.shape.Rectangle;

import java.util.Objects;

/**
 * Created by th174 on 1/29/2017.
 */
public abstract class AbstractCell {
    /*
     * to do
     */
    public static final double BORDER_OFFSET = 1;
    private CellState currentState;
    private CellState nextState;
    private SimulationGrid<Cell> parentGrid;
    private final int xPos;
    private final int yPos;
    private final Rectangle myRectangle;

    public AbstractCell(int x, int y, CellState state) {
        currentState = state;
        nextState = state;
        xPos = x;
        yPos = y;
        myRectangle = new Rectangle();
    }

    /**
     * Advances currentState to nextState
     * This is the only way currentState can be modified
     */
    public void updateState() {
        currentState = nextState;
        myRectangle.setFill(currentState.getFill());
        myRectangle.setWidth(parentGrid.getScreenWidth() / parentGrid.getWidth() - BORDER_OFFSET * 2);
        myRectangle.setHeight(parentGrid.getScreenHeight() / parentGrid.getHeight() - BORDER_OFFSET * 2);
        myRectangle.setX(parentGrid.getScreenWidth() * xPos / parentGrid.getWidth() + BORDER_OFFSET);
        myRectangle.setY(parentGrid.getScreenHeight() * yPos / parentGrid.getHeight() + BORDER_OFFSET);
    }

    /**
     * @param grid Grid of simulation
     */
    public abstract void interact(SimulationGrid<Cell> grid);

    /**
     * @return Grid of neighboring cells. See SimulationGrid::getNeighbors
     */
    public SimulationGrid<Cell> getNeighbors() {
        if (Objects.nonNull(parentGrid)) {
            return parentGrid.getNeighbors(xPos, yPos);
        } else {
            return null;
        }
    }

    public SimulationGrid<Cell> getAdjNeighbors() {
        if (Objects.nonNull(parentGrid)) {
            return parentGrid.getAdjNeighbors(xPos, yPos);
        } else {
            return null;
        }
    }

    /**
     * Sets the state that this cell will change into the next time updateState is called
     *
     * @param state CellState on next update
     */
    public void setState(CellState state) {
        nextState = state;
    }

    /**
     * @return currentState of cell
     */
    public CellState getState() {
        return currentState;
    }

    /**
     * @return Debug String representation
     */
    public String toString() {
        return "\nxPos = " + xPos + "\t\tyPos = " + yPos + "\t\tCurrentState = " + currentState + " \t\tNextState = " + nextState + "\t\tFill: + " + myRectangle.getFill();
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
    public void setParentGrid(SimulationGrid grid) {
        parentGrid = grid;
    }

    protected CellState getNextState() {
        return nextState;
    }
}
