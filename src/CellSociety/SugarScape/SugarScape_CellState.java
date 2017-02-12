package CellSociety.SugarScape;

import CellSociety.Abstract_CellState;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public final class SugarScape_CellState extends Abstract_CellState<SugarScape_CellState, SugarScape_CellState.SugarScapeState> {
    private int growBackTicks = 1;
    private int sugarGrowBackUnits = 1;
    private int maxCapacity;
    private int ticks;
    private int sugar;
    private double sugarFillCutoff = maxCapacity / 5.0;
    private Agent myAgent;
    private Agent nextAgent;

    public SugarScape_CellState(SugarScapeState state) {
        this(state, 0, 0, null);
    }

    public SugarScape_CellState(SugarScapeState state, int startingSugar, int life, Agent nextA) {
        super(state);
        sugar = startingSugar;
        ticks = life;
        maxCapacity = 20;
        myAgent = nextA;
        nextAgent=null;
    }

    @Override
    public int compareTo(SugarScape_CellState state) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Color getFill() {
        if (sugar < sugarFillCutoff) return Color.WHITE;
        if (sugar < 2 * sugarFillCutoff) return Color.LIGHTPINK;
        if (sugar < 3 * sugarFillCutoff) return Color.ORANGE;
        if (sugar < 4 * sugarFillCutoff) return Color.ORANGERED;
        return Color.CRIMSON;
    }

    @Override
    public SugarScape_CellState getSuccessorState() {
        return new SugarScape_CellState(getState(), sugar, ticks, nextAgent);
    }

    @Override
    public SugarScape_CellState getInactiveState() {
        return null;
    }

    public void growSugar() {
        ticks++;
        if (ticks == growBackTicks) {
            ticks = 0;
            sugar = Math.min(sugar + sugarGrowBackUnits, maxCapacity);
        }
    }

    public void removeAgent(Agent a) {
        myAgent=null;
    }

    public Agent getAgent() {
        return myAgent;
    }

    public int getSugar() {
        return sugar;
    }

    public void removeSugar() {
        sugar = 0;
    }

    public enum SugarScapeState {
        DEAD, ALIVE
    }
    public boolean isEmpty(){
    	return myAgent==null;
    }
    public boolean canReproduceHere(){
    	return isEmpty() && nextAgent==null;
    }
    public void setNextAgent(Agent a){
    	nextAgent=a;
    }

	@Override
	public Set<SugarScapeState> getDistinctCellStates() {
		// TODO Auto-generated method stub
		return null;
	}
}
