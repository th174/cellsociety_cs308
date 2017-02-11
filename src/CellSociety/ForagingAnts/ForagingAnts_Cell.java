package CellSociety.ForagingAnts;

import java.util.Collection;
import java.util.stream.Collectors;

import CellSociety.Abstract_Cell;
import CellSociety.ForagingAnts.ForagingAnts_CellState.ForagingAntsState;


public class ForagingAnts_Cell extends Abstract_Cell<ForagingAnts_Cell,ForagingAnts_CellState> {

	protected ForagingAnts_Cell(int x, int y, ForagingAnts_CellState state) {
		super(x, y, state);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void interact() {
		if(!getCurrentState().equals(ForagingAntsState.OBSTACLE)){
			Collection<ForagingAnts_Cell> neighbors = getNeighbors().stream().collect(Collectors.toSet());
			for(Ant a: getCurrentState().getAnts()){
				if(a.hasFood()) returnToNest(neighbors,a);
				else findFoodSource(neighbors, a);
			}
		}
		
	}
	private void returnToNest(Collection<ForagingAnts_Cell> neighbors, Ant a){
		
		
		
	}
	private void findFoodSource(Collection<ForagingAnts_Cell> neighbors, Ant a){
		
	}

}
