package CellSociety.SugarScape;

import CellSociety.Abstract_CellState;
import javafx.scene.paint.Color;

import java.util.Set;


public final class SugarScape_CellState extends Abstract_CellState<SugarScape_CellState, Double> {
    private double sugarGrowBackRate;
    private double maxCapacity;
    private double agentMetabolism;
    private double agentCurrentSugar;
    private double agentSugarCapacity;
    private boolean hasAgent;
    private int maxReproduceTimer;
    private int reproduceTimer;


    public SugarScape_CellState(String... params) {
        super(Double.parseDouble(params[0]));
        maxCapacity = params.length > 1 ? Double.parseDouble(params[1]) : 100;
        sugarGrowBackRate = params.length > 2 ? Double.parseDouble(params[2]) : 20;
        agentMetabolism = params.length > 3 ? Double.parseDouble(params[3]) : 30;
        agentSugarCapacity = params.length > 4 ? Double.parseDouble(params[4]) : 0;
        agentCurrentSugar = agentSugarCapacity;
        maxReproduceTimer = params.length > 5 ? Integer.parseInt(params[5]) : Integer.MAX_VALUE;
        reproduceTimer = maxReproduceTimer;
        hasAgent = params.length > 3;
    }

    public SugarScape_CellState(double sugar, double maxSugar, double sugarGrowRate, boolean agent, double metabolism, double agentSugar, double agentMaxSugar, int reproductionTime, int maxReproductionTime) {
        super(sugar);
        hasAgent = agent;
        maxCapacity = maxSugar;
        agentMetabolism = metabolism;
        agentCurrentSugar = agentSugar;
        agentSugarCapacity = agentMaxSugar;
        sugarGrowBackRate = sugarGrowRate;
        maxReproduceTimer = maxReproductionTime;
        reproduceTimer = reproductionTime;
    }

    public SugarScape_CellState(SugarScape_CellState parent) {
        this(Math.min(parent.getState() + parent.sugarGrowBackRate, parent.maxCapacity), parent.maxCapacity, parent.sugarGrowBackRate, parent.hasAgent, parent.agentMetabolism, parent.agentCurrentSugar - parent.agentMetabolism, parent.agentSugarCapacity, parent.reproduceTimer - 1, parent.maxReproduceTimer);
    }

    public SugarScape_CellState(SugarScape_CellState parent, boolean agent, double agentSugar) {
        this(agent ? 0 : parent.getState(), parent.maxCapacity, parent.sugarGrowBackRate, agent, agent ? parent.agentMetabolism : 0, Math.min(agentSugar, parent.agentSugarCapacity) - parent.agentMetabolism, parent.agentSugarCapacity, parent.reproduceTimer - 1, parent.maxReproduceTimer);
    }

    public SugarScape_CellState(SugarScape_CellState parent, boolean agent) {
        this(agent ? 0 : parent.getState(), parent.maxCapacity, parent.sugarGrowBackRate, agent, agent ? parent.agentMetabolism : 0, parent.agentSugarCapacity, parent.agentSugarCapacity, parent.maxReproduceTimer, parent.maxReproduceTimer);
    }

    public double getAgentSugar() {
        return agentCurrentSugar;
    }

    @Override
    public int compareTo(SugarScape_CellState otherCellState) {
        return getState().compareTo(otherCellState.getState());
    }

    @Override
    public Color getFill() {
        return hasAgent() ? Color.RED : Color.ORANGE.deriveColor(0, 1, 1, getState() / maxCapacity);
    }

    public double getSugar() {
        return getState();
    }

    @Override
    public SugarScape_CellState getSuccessorState() {
        return new SugarScape_CellState(this);
    }

    public boolean hasAgent() {
        return hasAgent;
    }

    public boolean canReproduce() {
        return reproduceTimer <= 0;
    }

    @Override
    public Set getDistinctCellStates() {
        // TODO Auto-generated method stub
        return null;
    }
}
