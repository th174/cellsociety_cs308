package CellSociety;

import javafx.scene.paint.Color;

import java.util.Set;

/**
 * Base class for representing the CellState of a Cell at any particular instance in time.
 * <p>
 * Note: All concrete implementations of this class should be immutable.
 * <p>
 * Created by th174 on 1/29/2017.
 *
 * @param <E> The Abstract_CellState type subclass
 * @param <T> The type of object used to represent the current state
 */
public abstract class Abstract_CellState<E extends Abstract_CellState<E, T>, T> implements Comparable<E> {

    private final T myState;

    protected Abstract_CellState(T state) {
        myState = state;
    }

    /**
     * Compares two CellState objects based on natural ordering of T. Throws an error if the two states cannot be compared.
     *
     * @param state to be compared to
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public abstract int compareTo(E state);

    /**
     * Compares two CellState objects for equality based on T nature ordering of T. Returns false if the two states cannot be compared.
     *
     * @param state to be compared to
     * @return true if two CellState objects share the same state
     */
    @Override
    public boolean equals(Object state) {
        try {
            return compareTo((E) state) == 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @return Graphical representation of this CellState
     */
    public abstract Color getFill();

    /**
     * @return A new object extending Abstract_CellState that represents the default successor state to this state
     */
    public abstract E getSuccessorState();

    /**
     * @return The set of distinct states of type T that myState can contain.
     */
    public abstract Set<T> getDistinctCellStates();

    protected T getState() {
        return myState;
    }

    /**
     * @return XML formatted representation of this Abstract_CellState
     */
    public String toString() {
        return String.format("\n\t\t<State>%s</State>", myState.toString());
    }
}
