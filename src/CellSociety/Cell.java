package CellSociety;

import javafx.scene.shape.Rectangle;

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
     * advances cell to nextState
     */
    public void updateState() {
        currentState = nextState;
        myRectangle.setFill(currentState.getFill());
    }

    public void interact(SimulationGrid<Cell> grid) {
        myRectangle.setWidth(WindowProperties.getWidth() / grid.getWidth() - BORDER_OFFSET * 2);
        myRectangle.setHeight(WindowProperties.getHeight() / grid.getHeight() - BORDER_OFFSET * 2);
        myRectangle.setX(WindowProperties.getWidth() * xPos / grid.getWidth() + BORDER_OFFSET);
        myRectangle.setY(WindowProperties.getHeight() * yPos / grid.getHeight() + BORDER_OFFSET);
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
        return "\nxPos = " + xPos + "\t\tyPos = " + yPos + "\t\tCurrentState = " + currentState + " \t\tNextState = " + nextState + "\t\tFill: + " + myRectangle.getFill();
    }
    public Rectangle getRectangle(){
    	return myRectangle;
    }

    public void setParentGrid(SimulationGrid grid) {
        parentGrid = grid;
    }
}
