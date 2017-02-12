package CellSociety.SlimeMold;

import CellSociety.Abstract_Cell;
import CellSociety.Grids.SimulationGrid;


public class SlimeMold_Cell extends Abstract_Cell<SlimeMold_Cell, SlimeMold_CellState>{
	
	public SlimeMold_Cell(int x, int y, SlimeMold_CellState state) {
		super(x,y,state);
	}

	@Override
	public void interact() {
		SimulationGrid<SlimeMold_Cell, SlimeMold_CellState> neighborsGrid = getNeighbors();
		boolean turtleMoved;
		if(getCurrentState().hasTurtle()){
			Turtle thisTurtle = getCurrentState().getTurtle();
			thisTurtle.depositChemical(this);
			if(thisTurtle.followGradient(getCurrentState())){
				turtleMoved = thisTurtle.moveToCell(neighborsGrid);
			}else{
				turtleMoved =thisTurtle.moveSameDirection(neighborsGrid);
			}
			if(turtleMoved &&getCurrentState().getNextTurtle()!=null) 
				getCurrentState().setNextTurtle(null);
		}
		
		diffuseAndEvaporate(neighborsGrid);
		
	}
	public void diffuseAndEvaporate(SimulationGrid<SlimeMold_Cell, SlimeMold_CellState> neighborsGrid){
		getCurrentState().evaporate();
		int numberNeighbors = (int) neighborsGrid.stream().filter(e-> e!=null).count();
		
		double diffusionAmount = getCurrentState().getDiffusionAmount();
		double diffusionPerNeighbor = diffusionAmount/numberNeighbors;
		while(neighborsGrid.iterator().hasNext()){
			SlimeMold_Cell nextCell = neighborsGrid.iterator().next();
			if(nextCell!=null){
				nextCell.getCurrentState().addNextChemical(diffusionPerNeighbor);
			}
		}
			
	}
	

}
