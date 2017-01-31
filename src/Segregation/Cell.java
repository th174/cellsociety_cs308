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
    //TODO: Implement this;
    public void interact(SimulationGrid<AbstractCell> grid) {
    	//similar neighbors/unsatisfied neighbors
    	double satisfactionPercent =  (getNeighbors().asCollection().stream()
        .filter(e -> e.getState().equals(this.getState())).count())/
    			(getNeighbors().asCollection().stream()
    			        .filter(e -> !e.getState().equals(this.getState())).count());
    	if(satisfactionPercent<threshold){
    		//move
    		
    		
    	}
    	
    	
        return;
    }
    private void moveToEmpty(){
    	
    }
}