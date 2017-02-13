package CellSociety.SugarScape;

import CellSociety.Abstract_CellState;
import javafx.scene.paint.Color;

import java.util.Set;


public final class SugarScape_CellState extends Abstract_CellState<SugarScape_CellState, Double> {
    private double sugarGrowBackRate;
    private double maxCapacity;
    private double agentMetabolism;
    private double agentCurrentSugar;
    private boolean hasAgent;


    public SugarScape_CellState(String... params) {
        super(Double.parseDouble(params[0]));
    }

    public SugarScape_CellState(Double state, double maxSugar, double sugarGrowRate, double metabolism, double agentSugar) {
        super(state);
        maxCapacity = maxSugar;
        agentMetabolism = metabolism;
        agentCurrentSugar = agentSugar;
        sugarGrowBackRate = sugarGrowRate;
    }

    public SugarScape_CellState(SugarScape_CellState parent) {
        this(Math.min(parent.getState() + parent.sugarGrowBackRate / 2, parent.maxCapacity), parent.maxCapacity, parent.sugarGrowBackRate, parent.agentMetabolism, parent.agentCurrentSugar - parent.agentMetabolism);
    }

    @Override
    public int compareTo(SugarScape_CellState otherCellState) {
        return getState().compareTo(otherCellState.getState());
    }

    @Override
    public Color getFill() {
        return Color.ORANGE.deriveColor(0, 1, 1, getState() / maxCapacity);
    }

    @Override
    public SugarScape_CellState getSuccessorState() {
        return new SugarScape_CellState(this);
    }

    public boolean isEmpty() {
        return myAgent == null;
    }

    public boolean canReproduceHere() {
        return isEmpty() && nextAgent == null;
    }

    public void setNextAgent(Agent a) {
        nextAgent = a;
    }

    @Override
    public Set getDistinctCellStates() {
        // TODO Auto-generated method stub
        return null;
    }
}
