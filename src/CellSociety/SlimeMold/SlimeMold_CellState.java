package CellSociety.SlimeMold;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collection;

public class SlimeMold_CellState extends AbstractDiscrete_CellState<SlimeMold_CellState, SlimeMold_CellState.SlimeMoldState> {
    public static final double DEFAULT_CHEMICAL=1.0;
    public static final double DEFAULT_EVAP=0.9;
    public static final double DEFAULT_DIFF=1.0;
    public static final double DEFAULT_THRESHOLD=0.5;
	
	private double chemical;
    private double evaporationRate;
    private double diffusionRate;
    private Collection<Turtle> myTurtles;
    private double threshold;

    private double nextChemical;
    private Collection<Turtle> nextTurtles;


    public enum SlimeMoldState {
        EMPTY, TURTLE
    }

    /**
     * constructs new cellstate according to state, but with defautl values
     * @param state
     */
    public SlimeMold_CellState(SlimeMoldState state) {
        this(state, null);
    }

    /**
     * Uses default values except for turtles and state
     * @param state
     * @param turtles
     */
    public SlimeMold_CellState(SlimeMoldState state, Collection<Turtle> turtles) {
        this(state, turtles, DEFAULT_CHEMICAL, DEFAULT_EVAP, DEFAULT_DIFF, DEFAULT_THRESHOLD, new ArrayList<Turtle>());
    }

    /**
     * Constructs the cellstate with the values given, not the default values
     * @param state
     * @param turtles
     * @param chem
     * @param evaporation
     * @param diffusion
     * @param threshold
     * @param nextTurts
     */
    public SlimeMold_CellState(SlimeMoldState state, Collection<Turtle> turtles, double chem, double evaporation,
                               double diffusion, double threshold, Collection<Turtle> nextTurts) {
        super(state);
        myTurtles = turtles==null? new ArrayList<Turtle>():turtles;
        chemical = chem;
        evaporationRate = evaporation;
        diffusionRate = diffusion;
        nextTurtles = new ArrayList<Turtle>();
        nextChemical = chemical;
        nextTurtles = nextTurts;
    }

    /**
     * constructs a cell state with default values and name of param
     * @param params
     */
    public SlimeMold_CellState( String... params) {
        this(params[0].toLowerCase().equals("rand") ? randomState(SlimeMoldState.class) :
                SlimeMoldState.valueOf(params[0].toUpperCase()));
    }	



    /** 
     * Colors according to how much chemical there is. Color gets lighter with more chemical
     * @see CellSociety.Abstract_CellState#getFill()
     */
    @Override
    public Color getFill() {
        return chemical > 3 * threshold ? Color.WHITE : chemical > 2 * threshold ? Color.GREENYELLOW :
                chemical > threshold ? Color.GREEN : Color.BLACK;
    }

    /**
     * Gets successorState according to the current values
     * @see CellSociety.Abstract_CellState#getSuccessorState()
     */
    @Override
    public SlimeMold_CellState getSuccessorState() {
        SlimeMoldState nextState = nextTurtles.isEmpty() ? SlimeMoldState.EMPTY : SlimeMoldState.TURTLE;
        return new SlimeMold_CellState(nextState, nextTurtles, nextChemical, evaporationRate, diffusionRate, threshold, new ArrayList<Turtle>());
    }


    /**
     * @return true if this cell has a turtle
     */
    public boolean hasTurtle() {
        return !myTurtles.isEmpty();
    }

    /**
     * @return collection of turtles
     */
    public Collection<Turtle> getTurtles() {
        return myTurtles;
    }

    /**
     * @return amount of chemical
     */
    public double getChemical() {
        return chemical;
    }

    /**
     * sets chemical of the next state of this cell
     * @param nextChem 
     */
    public void setNextChemical(double nextChem) {
        nextChemical = nextChem;
    }

    /**
     * reduces chemical in cell according to evaporation rate
     */
    public void evaporate() {
        chemical = chemical * evaporationRate;
        nextChemical = chemical;
    }

    /**
     * @return amount of chemical that should be diffused
     */
    public double getDiffusionAmount() {
        chemical -= chemical * diffusionRate;
        nextChemical = chemical;
        return chemical * diffusionRate;
    }

    /**
     * Adds amount of chemical to the next state's chemical
     * @param addChem 
     */
    public void addNextChemical(double addChem) {
        nextChemical += addChem;
    }

    /**
     * Adds turtle to the next state
     * @param t
     */
    public void addNextTurtle(Turtle t) {
        nextTurtles.add(t);
    }

    /**
     * returns collection of turtles in the next state
     * @return
     */
    public Collection<Turtle> getNextTurtle() {
        return nextTurtles;
    }

    /**
     * @return true if this cell has turtles in the next state
     */
    public boolean hasNextTurtles() {
        return !nextTurtles.isEmpty();
    }

    /**
     * clears all turtles in the next state
     */
    public void clearNextTurtles() {
        nextTurtles = new ArrayList<Turtle>();
    }

}

