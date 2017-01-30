package PredatorPrey;

import java.util.ArrayList;
import java.util.Collection;

import CellSociety.SimulationGrid;

/**
 * Created by th174 on 1/29/2017.
 */
public class Cell extends CellSociety.Cell {
    private int movesSinceReproduction=0;
	private final int preyReproductionTime = 5;
	private final int predReproductionTime = 5;
	
	
    public Cell(int x, int y, CellSociety.CellState state, SimulationGrid grid) {
        super(x, y, state, grid);
    }

    @Override
    //TODO: Implement this;
    /*
     * Every turn of the simulation a fish will move to a random adjacent cell 
     * unless all four are occupied. If the fish has survived the number of turns 
     * necessary to breed it produces a new fish if there is an empty adjacent cell..
     * 
     * Each turn if there is a fish adjacent to a shark the shark eats it. If there are 
     * multiple adjacent fish the shark eats one at random. If there are no adjacent fish 
     * the shark moves in the same manner as fish. After eating or moving if the shark has 
     * survived the number of turns necessary to breed it produces a new shark if there is 
     * an empty adjacent cell.
     * @see CellSociety.Cell#interact(CellSociety.SimulationGrid)
     */
    public void interact(SimulationGrid<CellSociety.Cell> grid) {
    	ArrayList<CellSociety.Cell> adjNeighbors = new ArrayList<CellSociety.Cell>(getAdjNeighbors().asCollection());
   	 	
    	if(getState().equals(CellState.PREDATOR)){
    		
    	}
    	if(getState().equals(CellState.PREY)){
    		 //check adjacent cells. move to a random one of the empty ones
    		 if(adjNeighbors.size()>0){
    			int indexOfNextFish = (int) (Math.random()*(adjNeighbors.size()));
    			 System.out.println("we have " + adjNeighbors.size() + 
    					 " empty neighbors " +adjNeighbors + "       and we CHOSE     " + indexOfNextFish);
    			adjNeighbors.get(indexOfNextFish).setState(CellState.PREY);
    		}
    		setState(CellState.EMPTY);
    				
    	}
    	
    	
    	
        return;
    }
    public boolean canReproduce(){
    	if(getState().equals(CellState.PREDATOR)&&movesSinceReproduction>=predReproductionTime||
    			getState().equals(CellState.PREY)&&movesSinceReproduction>=preyReproductionTime){
    		movesSinceReproduction=0;
    		return true;
    	}
    	
    	return false;
    }


}