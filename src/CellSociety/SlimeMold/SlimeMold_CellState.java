package CellSociety.SlimeMold;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;

public class SlimeMold_CellState extends AbstractDiscrete_CellState<SlimeMold_CellState, SlimeMold_CellState.SlimeMoldState> {
    private double chemical;
    private double evaporationRate;
    private double diffusionRate;
    private Turtle myTurtle;
    private double threshold;//min required, so green

    private double nextChemical;
    private Turtle nextTurtle;


    public enum SlimeMoldState {
        EMPTY, TURTLE
    }

    public SlimeMold_CellState(SlimeMoldState state) {
        this(state, null, 1.0, 0.9, 0.1);
    }

    public SlimeMold_CellState(SlimeMoldState state, Turtle turt) {
        this(state, turt, 1.0, 0.9, 0.1);
    }

    public SlimeMold_CellState(SlimeMoldState state, Turtle turt, double chem, double evaporation, double diffusion) {
        super(state);
        myTurtle = turt;
        chemical = chem;
        evaporationRate = evaporation;
        diffusionRate = diffusion;
        nextTurtle = null;
        nextChemical = chemical;
    }

    public SlimeMold_CellState(SlimeMoldState state, String... params) {
        super(params[0].toLowerCase().equals("rand") ? randomState(SlimeMoldState.class) :
                SlimeMoldState.valueOf(params[0].toUpperCase()));
        if (params.length > 0) {
            myTurtle = new Turtle();
        }
    }

    @Override
    public Color getFill() {
        return chemical > 3 * threshold ? Color.WHITE : chemical > 2 * threshold ? Color.GREENYELLOW :
                chemical > threshold ? Color.GREEN : Color.BLACK;
    }

    @Override
    public SlimeMold_CellState getSuccessorState() {
        SlimeMoldState nextState = nextTurtle == null ? SlimeMoldState.EMPTY : SlimeMoldState.TURTLE;
        return new SlimeMold_CellState(nextState, nextTurtle, nextChemical, evaporationRate, diffusionRate);
    }

    @Override
    public SlimeMold_CellState getInactiveState() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean hasTurtle() {
        return myTurtle != null;
    }

    public Turtle getTurtle() {
        return myTurtle;
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

    public void setNextTurtle(Turtle t) {
        nextTurtle = t;
    }

    public Turtle getNextTurtle() {
        return nextTurtle;
    }

}
