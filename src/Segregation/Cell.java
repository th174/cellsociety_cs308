package Segregation;

import CellSociety.AbstractCell;
import CellSociety.SimulationGrid;

/**
 * Created by th174 on 1/29/2017.
 */
public class Cell extends AbstractCell {
	public static double threshold=0.50;
    public Cell(int x, int y, CellSociety.CellState state) {
        super(x, y, state);
    }
    //make method for next state
    
    @Override
    public void interact(SimulationGrid<AbstractCell> grid) {
    	//similar neighbors/unsatisfied neighbors
    	double satisfactionPercent =  (getNeighbors().asCollection().stream()
        .filter(e -> e.getState().equals(this.getState())).count())/
    			(getNeighbors().asCollection().stream()
    			        .filter(e -> !e.getState().equals(this.getState())).count());
    	if(satisfactionPercent<threshold){
    		moveToEmpty(grid);		
    	}
        return;
    }
    private void moveToEmpty(SimulationGrid<AbstractCell> grid){
    	for(AbstractCell c: grid.asCollection()){
    		if(c instanceof Segregation.Cell){
    			if(((Segregation.Cell) c).nextStateEmpty()){
    				//move to this cell
    				c.setState(this.getState());
    				setState(CellState.EMPTY);
    				return;
    			}
    		}
    	}
    }
    private boolean nextStateEmpty(){
    	return this.getNextState().equals(CellState.EMPTY);
    }
}