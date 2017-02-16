//This entire fire is part of my masterpiece
//Tim Overeem (to41)


/*
 * IMPORTANT NOTE: At one point, Timmy create a duplicate of all files. I'm not sure why; I think maybe he wanted to
 * try to do them without having to push to git/cause errors. The result of this was when he pushed, we had a bunch
 * of duplicate old versions of these files, which we had to delete. This is why the commit history for the files
 * are really messed up and this doesn't show my contributions. This and the Segregation_Cell classes
 * are two classes where I did almost all of the thinking/implementation behind this. I thought this would is 
 * important in case you look at the commit history.
 */

/*
 * I think this class illustrates good design because it is short, readable, good use of a subclass, and the details
 * are encapsulated. It does what it is supposed to do: interact with others based on its/their states, and nothing else.
 * It has one responsibility that it forms and the superclass can count on it to perform. It is very easy to read
 * and understand how it works.
 * Also, compared with some of the other CellState classes, this one does not have a list of various constructors
 * (which is made easier because not a lot of fields are required). Although it is very short and may not be
 * considered "intelligent" it is still necessary to have because putting all of these methods in the Cell class
 * would make that class have multiple responsibilities, and the distinction we made throughout our program that
 * cells do things, states hold information and calculate things based on these methods, would no longer hold.
 * Further, itis likely that if we were to make this simulation more realistic and robust, we would add more methods to
 * this class. 
 */
package CellSociety.Segregation;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;

/**
 * This class models a single CellState of a single Cell in the Segregation simulation.
 * Note: This class is immutable. All fields MUST be declared final.
 *
 * @see CellSociety.AbstractDiscrete_CellState
 * Created by th174 on 1/29/2017.
 */
public class Segregation_CellState extends AbstractDiscrete_CellState<Segregation_CellState, Segregation_CellState.SegregationState> {
    private static final double DEFAULT_SATISFACTORY_THRESHOLD = 0.5;
    public static final Segregation_CellState X = new Segregation_CellState(SegregationState.X, DEFAULT_SATISFACTORY_THRESHOLD);
    public static final Segregation_CellState O = new Segregation_CellState(SegregationState.O, DEFAULT_SATISFACTORY_THRESHOLD);
    public static final Segregation_CellState EMPTY = new Segregation_CellState(SegregationState.EMPTY, DEFAULT_SATISFACTORY_THRESHOLD);
    private final double satisfactionThreshold;

    private Segregation_CellState(SegregationState state, double satisfaction) {
        super(state);
        satisfactionThreshold = satisfaction;
    }

    /**
     * Constructs new Segregation_CellState with String properties read from XML file
     *
     * @param params String parameters read from XML file
     * @see #Segregation_CellState(SegregationState, double)
     */
    public Segregation_CellState(String... params) {
        this(params[0].toLowerCase().equals("rand") ? randomState(SegregationState.class) : SegregationState.valueOf(params[0].toUpperCase()),
                params.length > 1 ? Double.parseDouble(params[1]) : DEFAULT_SATISFACTORY_THRESHOLD);
    }

    /**
     * @param sameStateNeighbors Number of neighbors that share the same state
     * @param nonEmptyNeighbors  Total number of non-Empty neighbors
     * @return true if the ratio of same/all neighbors is greater than or equal to the satisfaction threshold
     */
    public boolean isSatisfiedByNeighbors(int sameStateNeighbors, int nonEmptyNeighbors) {
        return 1.0 * sameStateNeighbors / nonEmptyNeighbors >= satisfactionThreshold;
    }

    /**
     * @return Color.BLUE if cell is X, Color.RED if O, and Color.WHITE if empty
     * @see CellSociety.Abstract_CellState#getFill()
     */
    @Override
    public Color getFill() {
        return getState().equals(SegregationState.X) ? Color.BLUE : (getState().equals(SegregationState.O) ? Color.RED : Color.WHITE);
    }

    /**
     * @return successor state that inherits the same satisfaction threshold as its parent
     * @see CellSociety.Abstract_CellState#getSuccessorState()
     */
    @Override
    public Segregation_CellState getSuccessorState() {
        return new Segregation_CellState(getState(), satisfactionThreshold);
    }

    /**
     * @return XML string representation of this CellState
     * @see CellSociety.AbstractDiscrete_CellState#toString()
     */
    @Override
    public String toString() {
        return super.toString() + String.format("\n\t\t<Threshold>%f</Threshold>", satisfactionThreshold);
    }

    enum SegregationState {
        X, O, EMPTY
    }
}
