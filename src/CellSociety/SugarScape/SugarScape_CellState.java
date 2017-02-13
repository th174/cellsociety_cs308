package CellSociety.SugarScape;

import CellSociety.Abstract_CellState;
import javafx.scene.paint.Color;

import java.util.HashSet;
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

    /**
     * Constructor from xml
     *
     * @param params xml parameters
     */
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

    private SugarScape_CellState(double sugar, double maxSugar, double sugarGrowRate, boolean agent, double metabolism, double agentSugar, double agentMaxSugar, int reproductionTime, int maxReproductionTime) {
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

    private SugarScape_CellState(SugarScape_CellState parent) {
        this(Math.min(parent.getState() + parent.sugarGrowBackRate, parent.maxCapacity), parent.maxCapacity, parent.sugarGrowBackRate, parent.hasAgent, parent.agentMetabolism, parent.agentCurrentSugar - parent.agentMetabolism, parent.agentSugarCapacity, parent.reproduceTimer - 1, parent.maxReproduceTimer);
    }

    /**
     * Constructor for agent moving
     *
     * @param parent     previous agent location
     * @param agent      agent exists
     * @param agentSugar agent sugar amount
     */
    public SugarScape_CellState(SugarScape_CellState parent, boolean agent, double agentSugar) {
        this(agent ? 0 : parent.getState(), parent.maxCapacity, parent.sugarGrowBackRate, agent, agent ? parent.agentMetabolism : 0, Math.min(agentSugar, parent.agentSugarCapacity) - parent.agentMetabolism, parent.agentSugarCapacity, parent.reproduceTimer - 1, parent.maxReproduceTimer);
    }

    /**
     * Constructor for brand new agent
     *
     * @param parent parent agent
     * @param agent  new agent exists
     */
    public SugarScape_CellState(SugarScape_CellState parent, boolean agent) {
        this(agent ? 0 : parent.getState(), parent.maxCapacity, parent.sugarGrowBackRate, agent, agent ? parent.agentMetabolism : 0, parent.agentSugarCapacity, parent.agentSugarCapacity, parent.maxReproduceTimer, parent.maxReproduceTimer);
    }

    /**
     * @return current agent's current amount of sugar, or -1 if there is no agent
     */
    public double getAgentSugar() {
        return hasAgent ? agentCurrentSugar : -1;
    }

    /**
     * Compares two CellState objects. Throws an error if the two states cannot be compared.
     * Compares based on current sugar value
     *
     * @param otherCellState to be compared to
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(SugarScape_CellState otherCellState) {
        return getState().compareTo(otherCellState.getState());
    }

    /**
     * Tests whether two CellStates both have agents or not.
     *
     * @param o object to be compared with
     * @return
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof SugarScape_CellState && this.hasAgent() == ((SugarScape_CellState) o).hasAgent();
    }

    /**
     * @return color representation of the current state. By default, occupied cells are red, and others are shades of orange based on the amount of sand inside.
     */
    @Override
    public Color getFill() {
        return hasAgent() ? Color.RED : Color.ORANGE.deriveColor(0, 1, 1, getState() / maxCapacity);
    }

    /**
     *
     * @return current amount of sugar in this spot
     */
    public double getSugar() {
        return getState();
    }

    /**
     * @return Default successor state to the current state. For SugarScape, sugar amount is incremented by SugarGrowRate
     */
    @Override
    public SugarScape_CellState getSuccessorState() {
        return new SugarScape_CellState(this);
    }

    /**
     * @return True if there is currently an agent occupying this cell statee, else false.
     */
    public boolean hasAgent() {
        return hasAgent;
    }

    /**
     * @return True if the agent in this state is ready to reproduce
     */
    public boolean canReproduce() {
        return reproduceTimer <= 0;
    }

    /**
     * @return Set of all possible states the cell can be in. For SugarScape, the cell can be Occupied or Not Occupied
     */
    @Override
    public Set getDistinctCellStates() {
        Set<Boolean> agentOrNoAgent = new HashSet<>();
        agentOrNoAgent.add(true);
        agentOrNoAgent.add(false);
        return agentOrNoAgent;
    }
}
