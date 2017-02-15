package CellSociety.SugarScape;

import CellSociety.AbstractDiscrete_CellState;
import CellSociety.Abstract_CellState;
import javafx.scene.paint.Color;

/**
 * This class models a single CellState of a single Cell in the SugarScape simulation.
 * Note: This class is immutable. All fields MUST be declared final.
 *
 * @see CellSociety.AbstractDiscrete_CellState
 * Created by th174 on 2/10/2017.
 */
public final class SugarScape_CellState extends AbstractDiscrete_CellState<SugarScape_CellState, SugarScape_CellState.SugarScapeState> {
    public static final SugarScape_CellState EMPTY = new SugarScape_CellState(SugarScapeState.EMPTY);
    public static final SugarScape_CellState OCCUPIED = new SugarScape_CellState(SugarScapeState.OCCUPIED);

    private final double currentSugar;
    private final double sugarGrowBackRate;
    private final double maxCapacity;
    private final double agentMetabolism;
    private final double agentCurrentSugar;
    private final double agentSugarCapacity;
    private final int maxReproduceTimer;
    private final int reproduceTimer;

    private SugarScape_CellState(SugarScapeState state) {
        this(state, 0, 100, 20, 30, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    private SugarScape_CellState(SugarScapeState state, double sugar, double maxSugar, double growRate, double metabolism, double agentMaxSugar, int maxReproduceTime) {
        super(state);
        currentSugar = sugar;
        maxCapacity = maxSugar;
        sugarGrowBackRate = growRate;
        agentMetabolism = metabolism;
        agentSugarCapacity = agentMaxSugar;
        agentCurrentSugar = agentSugarCapacity;
        maxReproduceTimer = maxReproduceTime;
        reproduceTimer = maxReproduceTimer;

    }

    /**
     * Constructs new SugarScape_CellState with String properties read from XML file
     *
     * @param params String parameters read from XML file
     * @see #SugarScape_CellState(SugarScapeState, double, double, double, double, double, int)
     */
    public SugarScape_CellState(String... params) {
        this(params[0].toLowerCase().equals("rand") ? randomState(SugarScapeState.class) : SugarScapeState.valueOf(params[0].toUpperCase()),
                params.length > 1 ? Double.parseDouble(params[1]) : 0,
                params.length > 2 ? Double.parseDouble(params[2]) : 100,
                params.length > 3 ? Double.parseDouble(params[3]) : 20,
                params.length > 4 ? Double.parseDouble(params[4]) : 30,
                params.length > 5 ? Double.parseDouble(params[5]) : 0,
                params.length > 6 ? Integer.parseInt(params[6]) : Integer.MAX_VALUE);
    }

    private SugarScape_CellState(double sugar, double maxSugar, double growRate, boolean agent, double metabolism, double agentSugar, double agentMaxSugar, int reproductionTime, int maxReproductionTime) {
        super(agent ? SugarScapeState.OCCUPIED : SugarScapeState.EMPTY);
        currentSugar = sugar;
        maxCapacity = maxSugar;
        agentMetabolism = metabolism;
        agentCurrentSugar = agentSugar;
        agentSugarCapacity = agentMaxSugar;
        sugarGrowBackRate = growRate;
        maxReproduceTimer = maxReproductionTime;
        reproduceTimer = reproductionTime;
    }

    private SugarScape_CellState(SugarScape_CellState parent) {
        this(Math.min(parent.currentSugar + parent.sugarGrowBackRate, parent.maxCapacity), parent.maxCapacity, parent.sugarGrowBackRate, parent.equals(OCCUPIED), parent.agentMetabolism, parent.agentCurrentSugar - parent.agentMetabolism, parent.agentSugarCapacity, parent.reproduceTimer - 1, parent.maxReproduceTimer);
    }

    /**
     * Constructor for new CellState when agent moves locations
     *
     * @param parent     previous agent location
     * @param agent      agent exists
     * @param agentSugar agent sugar amount
     */
    public SugarScape_CellState(SugarScape_CellState parent, boolean agent, double agentSugar) {
        this(agent ? 0 : parent.currentSugar, parent.maxCapacity, parent.sugarGrowBackRate, agent, agent ? parent.agentMetabolism : 0, Math.min(agentSugar, parent.agentSugarCapacity) - parent.agentMetabolism, parent.agentSugarCapacity, parent.reproduceTimer - 1, parent.maxReproduceTimer);
    }

    /**
     * Constructor for new CellState with brand new agent
     *
     * @param parent parent agent
     * @param agent  new agent exists
     */
    public SugarScape_CellState(SugarScape_CellState parent, boolean agent) {
        this(agent ? 0 : parent.currentSugar, parent.maxCapacity, parent.sugarGrowBackRate, agent, agent ? parent.agentMetabolism : 0, parent.agentSugarCapacity, parent.agentSugarCapacity, parent.maxReproduceTimer, parent.maxReproduceTimer);
    }

    /**
     * @return current agent's current amount of sugar, or -1 if there is no agent
     */
    public double getAgentSugar() {
        return equals(OCCUPIED) ? agentCurrentSugar : -1;
    }

    /**
     * Compares two CellState objects. Throws an error if the two states cannot be compared.
     * Compares based on current sugar value
     *
     * @param otherCellState to be compared to
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * @see AbstractDiscrete_CellState#compareTo(Abstract_CellState)
     */
    @Override
    public int compareTo(SugarScape_CellState otherCellState) {
        return (int) ((getCurrentSugar() - otherCellState.getCurrentSugar()) * 100);
    }

    /**
     * @return Color representation of the current state. By default, occupied cells are red, and others are shades of orange based on the amount of sand inside.
     * @see Abstract_CellState#getFill()
     */
    @Override
    public Color getFill() {
        return equals(OCCUPIED) ? Color.RED : Color.ORANGE.deriveColor(0, 1, 1, getCurrentSugar() / maxCapacity);
    }

    /**
     * @return current amount of sugar in this spot
     */
    public double getCurrentSugar() {
        return currentSugar;
    }

    /**
     * @return Default successor state to the current state. For SugarScape, sugar amount is incremented by SugarGrowRate
     * @see Abstract_CellState#getSuccessorState()
     */
    @Override
    public SugarScape_CellState getSuccessorState() {
        return new SugarScape_CellState(this);
    }

    /**
     * @return True if the agent in this state is ready to reproduce
     */
    public boolean canReproduce() {
        return reproduceTimer <= 0;
    }

    /**
     * @see AbstractDiscrete_CellState#toString()
     */
    @Override
    public String toString() {
        return String.format("\n\t\t<State>%s</State>\n\t\t<Sugar>%f</Sugar>\n\t\t<MaxCapacity>%f</MaxCapacity>\n\t\t<GrowRate>%f</GrowRate>\n\t\t<Metabolism>%f</Metabolism>\n\t\t<AgentSugar>%f</AgentSugar>\n\t\t<ReproduceTimer>%d</ReproduceTimer>",
                getState().name(), currentSugar, maxCapacity, sugarGrowBackRate, agentMetabolism, agentCurrentSugar, reproduceTimer);
    }

    enum SugarScapeState {
        EMPTY, OCCUPIED
    }
}
