package CellSociety;

import javafx.scene.shape.Rectangle;

/**
 * Created by th174 on 1/29/2017.
 */
public abstract class Cell extends Rectangle {
    public static final double BORDER_OFFSET = 1;
    private CellState currentState;
    private CellState nextState;
    private SimulationGrid<Cell> parentGrid;
    private int xPos;
    private int yPos;

    public Cell(int x, int y, CellState state, SimulationGrid grid) {
        parentGrid = grid;
        currentState = state;
        nextState = state;
        xPos = x;
        yPos = y;
    }

    /**
     * advances cell to nextState
     */
    public void updateState() {
        currentState = nextState;
        setFill(currentState.getFill());
    }

    public void interact(SimulationGrid<Cell> grid) {
        setWidth(WindowProperties.getWidth() / grid.getWidth() - BORDER_OFFSET * 2);
        setHeight(WindowProperties.getHeight() / grid.getHeight() - BORDER_OFFSET * 2);
        setX(WindowProperties.getWidth() * xPos / grid.getWidth() + BORDER_OFFSET);
        setY(WindowProperties.getHeight() * yPos / grid.getHeight() + BORDER_OFFSET);
    }

    public SimulationGrid<Cell> getNeighbors() {
        return parentGrid.getNeighbors(xPos, yPos);
    }

    public void setState(CellState state) {
        nextState = state;
    }

    public CellState getState() {
        return currentState;
    }

    public String toString() {
        return "\nxPos = " + xPos + "\t\tyPos = " + yPos + "\t\tCurrentState = " + currentState + " \t\tNextState = " + nextState + "\t\tFill: + " + getFill();
    }

    public void setParentGrid(SimulationGrid grid) {
        parentGrid = grid;
    }
}
