package CellSociety.ForagingAnts;

import java.util.ArrayList;
import java.util.Collection;

import CellSociety.AbstractDiscrete_CellState;
import CellSociety.Abstract_CellState;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class ForagingAnts_CellState extends Abstract_CellState<ForagingAnts_CellState.ForagingAntsState> {
	
	private int ants;
	private double foodPheromone;
	private double homePheromone;
	private Collection<Ant> myAnts;
	protected ForagingAnts_CellState(ForagingAntsState state) {
		super(state);
		myAnts= new ArrayList<Ant>();
		ants=0;
		foodPheromone=0;
		homePheromone=0;
	}
	
	enum ForagingAntsState {
        HOME, SOURCE, EMPTY, OBSTACLE
    }

	@Override
	public Paint getFill() {
		return getState().equals(ForagingAntsState.EMPTY) ? Color.BLUE : getState().equals(ForagingAntsState.SOURCE) ?
				Color.DARKGRAY : getState().equals(ForagingAntsState.OBSTACLE) ?  Color.WHITESMOKE : Color.DARKOLIVEGREEN;	    
	}
	public Collection<Ant> getAnts(){
		return myAnts;
	}

	@Override
	public Abstract_CellState<ForagingAntsState> getSuccessorState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int compareTo(Abstract_CellState<ForagingAntsState> state) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
