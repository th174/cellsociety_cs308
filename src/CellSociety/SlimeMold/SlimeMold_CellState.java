package CellSociety.SlimeMold;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collection;

public class SlimeMold_CellState extends AbstractDiscrete_CellState<SlimeMold_CellState, SlimeMold_CellState.SlimeMoldState> {
    private double chemical;
    private double evaporationRate;
    private double diffusionRate;
    private Collection<Turtle> myTurtles;
    private double threshold;//min required, so green

    private double nextChemical;
    private Collection<Turtle> nextTurtles;


    public enum SlimeMoldState {
        EMPTY, TURTLE
    }

    public SlimeMold_CellState(SlimeMoldState state) {
        this(state, null, 1.0, 0.9, 0.1, new ArrayList<Turtle>());
    }

    public SlimeMold_CellState(SlimeMoldState state, Collection<Turtle> turtles) {
        this(state, turtles, 1.0, 0.9, 0.1, new ArrayList<Turtle>());
    }

    public SlimeMold_CellState(SlimeMoldState state, Collection<Turtle> turtles, double chem, double evaporation,
                               double diffusion, Collection<Turtle> nextTurts) {
        super(state);
        myTurtles = turtles;
        chemical = chem;
        evaporationRate = evaporation;
        diffusionRate = diffusion;
        nextTurtles = new ArrayList<Turtle>();
        nextChemical = chemical;
        nextTurtles = nextTurts;
    }

    public SlimeMold_CellState( String... params) {
        super(params[0].toLowerCase().equals("rand") ? randomState(SlimeMoldState.class) :
                SlimeMoldState.valueOf(params[0].toUpperCase()));
        if (params.length > 1) {
            int numTurtles=Integer.parseInt(params[1]);
            Collection<Turtle> turts = new ArrayList<Turtle>();
            for(int i=0;i<numTurtles;i++){
            	turts.add(new Turtle());
            }
            myTurtles=turts;
        }else{
        	myTurtles=new ArrayList<Turtle>();
        }
        chemical = 1.0;
        evaporationRate = 0.9;
        diffusionRate = 0.1;
        nextTurtles = new ArrayList<Turtle>();
        nextChemical = chemical;
    }	

    @Override
    public Color getFill() {
        return chemical > 3 * threshold ? Color.WHITE : chemical > 2 * threshold ? Color.GREENYELLOW :
                chemical > threshold ? Color.GREEN : Color.BLACK;
    }

    @Override
    public SlimeMold_CellState getSuccessorState() {
        SlimeMoldState nextState = nextTurtles.isEmpty() ? SlimeMoldState.EMPTY : SlimeMoldState.TURTLE;
        return new SlimeMold_CellState(nextState, nextTurtles, nextChemical, evaporationRate, diffusionRate, new ArrayList<Turtle>());
    }


    public boolean hasTurtle() {
        return !myTurtles.isEmpty();
    }

    public Collection<Turtle> getTurtles() {
        return myTurtles;
    }

    public double getChemical() {
        return chemical;
    }

    public void setNextChemical(double nextChem) {
        nextChemical = nextChem;
    }

    public void evaporate() {
        chemical = chemical * evaporationRate;
        nextChemical = chemical;
    }

    public double getDiffusionAmount() {
        chemical -= chemical * diffusionRate;
        nextChemical = chemical;
        return chemical * diffusionRate;
    }

    public void addNextChemical(double addChem) {
        nextChemical += addChem;
    }

    public void addNextTurtle(Turtle t) {
        nextTurtles.add(t);
    }

    public Collection<Turtle> getNextTurtle() {
        return nextTurtles;
    }

    public boolean hasNextTurtles() {
        return nextTurtles.isEmpty();
    }

    public void clearNextTurtles() {
        nextTurtles = new ArrayList<Turtle>();
    }

}

