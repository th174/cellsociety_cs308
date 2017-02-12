package CellSociety.SugarScape;

import java.util.Comparator;
import java.util.List;

import CellSociety.Abstract_CellState;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


public final class SugarScape_CellState extends Abstract_CellState<SugarScape_CellState.SugarScapeState> {
	private int growBackTicks=1;
	private int sugarGrowBackUnits=1;
	private int maxCapacity;
	private int sugar;
	private double sugarFillCutoff=maxCapacity/5.0;
	private List<Agent> myAgents;
	
	public enum SugarScapeState {
		DEAD,ALIVE
	}

	public SugarScape_CellState(SugarScapeState state) {
		super(state);
		sugar=20;
	}
	public SugarScape_CellState(SugarScapeState state, int startingSugar) {
		super(state);
		sugar=startingSugar;
	}

	@Override
	public int compareTo(Abstract_CellState<SugarScapeState> state) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Paint getFill() {
		if(sugar < sugarFillCutoff) return Color.WHITE;
		if(sugar < 2*sugarFillCutoff) return Color.LIGHTPINK;
		if(sugar < 3*sugarFillCutoff) return Color.ORANGE;
		if(sugar <4*sugarFillCutoff) return Color.ORANGERED;
		return Color.CRIMSON;
	}

	@Override
	public Abstract_CellState<SugarScapeState> getSuccessorState() {
		// TODO Auto-generated method stub
		return new SugarScape_CellState(getState(),sugar);
	}
	public void removeAgent(Agent a){
		myAgents.remove(a);
	}
	public List<Agent> getAgents(){
		return myAgents;
	}
	public int getSugar(){
		return sugar;
	}
	public void removeSugar(){
		sugar=0;
	}
	

}
