//This entire fire is part of my masterpiece
//Tim Overeem (to41)

/*
 * Note that ideally, I would have liked to get rid of the extension of Abstract_CellState for the reasons in my 
 * analysis (basically, every simulation's cellstate class extends this class only, and having both Abstract_CellState
 * and AbstractDiscrete_CellState is confusing for someone not familiar with our code. However, this would require
 *  me to change several other classes. At this point, it seems easier to blatantly comment on the Abstract_CellState
 *  class that it should not be extended by simulations. 
 *  
 *  I think this represents good design because it a good use of inheritance and this feature is very clearly
 *  open for extension. The Segregation_CellState subclass shows how this is done, and I can be certain that
 *  any new simulation that inherits this class with perform all the methods it needs to for the program to work.
 *  This class is relatively short, the proper methods are abstract or implemented. 
 *  
 *  Although Timmy laid out the foundation for this class, I was very involved in the brainstorming and would
 *  give him a lot of feedback. This class is definitely a collaborative effort between us two. It was just a 
 *  nature of our teamwork and flexibility that determined who actually wrote and committed the code.
 */

package CellSociety;


import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class provides a general implementation of a Discrete CellState, where the state can be represented 
 * in the form of an Enumeration. In addition, this also provides a method to obtain a random state from an enum of states
 * 
 * @param <E> The AbstractDiscrete_CellState type subclass
 * @param <T> The type extending Enum used to represent the CellState
 */
public abstract class AbstractDiscrete_CellState<E extends AbstractDiscrete_CellState<E, T>, T extends Enum<T>> extends Abstract_CellState<E, T> {

    protected AbstractDiscrete_CellState(T state) {
        super(state);
    }

    protected static <E extends Enum<E>> E randomState(Class<E> enumState) {
        return enumState.getEnumConstants()[(int) (Math.random() * enumState.getEnumConstants().length)];
    }

    /**
     * Returns a set of Distinct States in the grid.
     * @see CellSociety.Abstract_CellState#getDistinctCellStates()
     */
    @Override
    public final Set<T> getDistinctCellStates() {
        return Arrays.stream(getState().getClass().getEnumConstants()).map(e -> (T) e).collect(Collectors.toSet());
    }

    /**
     * @see CellSociety.Abstract_CellState#equals(Object o)
     */
    @Override
    public final boolean equals(Object o) {
        if (o instanceof AbstractDiscrete_CellState) {
            return getState().equals(((AbstractDiscrete_CellState) o).getState());
        } else {
            return o instanceof Enum && getState().equals(o);
        }
    }

    /**
     * Returns an int based on the ordering of the states. 
     * @see CellSociety.Abstract_CellState#compareTo(CellSociety.Abstract_CellState)
     */
    @Override
    public int compareTo(E cellState) {
        return getState().compareTo(cellState.getState());
    }
}
