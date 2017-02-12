package CellSociety.SugarScape;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.List;

import CellSociety.Abstract_Cell;
import CellSociety.ForagingAnts.ForagingAnts_Cell;
import CellSociety.ForagingAnts.ForagingAnts_CellState;

public class SugarScape_Cell extends Abstract_Cell<SugarScape_Cell,SugarScape_CellState>{

	public SugarScape_Cell(int x, int y, SugarScape_CellState state) {
		super(x, y, state);
	}

	@Override
	public void interact() {
		List<SugarScape_Cell> adjNeighbors =  getAdjNeighbors().stream().collect(Collectors.toList());
		//use Agent's vision as well 
		Collections.sort(adjNeighbors, new sugarComparator());
		for(Agent a: getCurrentState().getAgents()){
			a.grabSugar(adjNeighbors.get(0));
			a.metabolize();
			if(a.isDead()) getNextState().removeAgent(a);
			Collections.sort(adjNeighbors, new sugarComparator()); //update so we always grab the best cell
		}
		
	}
	private class sugarComparator implements Comparator<SugarScape_Cell>{

		@Override
		public int compare(SugarScape_Cell o1, SugarScape_Cell o2) {		
			if(o2.getNextState().getSugar() != o1.getNextState().getSugar()) 
				return o2.getNextState().getSugar() - o1.getNextState().getSugar();
			else return 0; //return closest
		}
		
	}

}
