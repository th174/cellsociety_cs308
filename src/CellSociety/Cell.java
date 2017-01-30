package CellSociety;

import javafx.scene.shape.Rectangle;

import java.util.Collection;
import java.util.Objects;

/**
 * Created by th174 on 1/29/2017.
 */
public abstract class Cell {
    /*
     * to do
     */
    public static final double BORDER_OFFSET = 1;
    private CellState currentState;
    private CellState nextState;
    private SimulationGrid<Cell> parentGrid;
    private int xPos;
    private int yPos;
    private Rectangle myRectangle;

    public Cell(int x, int y, CellState state, SimulationGrid grid) {
        parentGrid = grid;
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
        myRectangle.setWidth(WindowProperties.getWidth() / parentGrid.getWidth() - BORDER_OFFSET * 2);
        myRectangle.setHeight(WindowProperties.getHeight() / parentGrid.getHeight() - BORDER_OFFSET * 2);
        myRectangle.setX(WindowProperties.getWidth() * xPos / parentGrid.getWidth() + BORDER_OFFSET);
        myRectangle.setY(WindowProperties.getHeight() * yPos / parentGrid.getHeight() + BORDER_OFFSET);
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
     * @param grid parent SimulationGrid
     */
    public void setParentGrid(SimulationGrid grid) {
        parentGrid = grid;
    }
}
