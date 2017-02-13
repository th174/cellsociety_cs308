package CellSociety;

import javafx.scene.paint.Color;

import java.util.Set;

/**
 * Created by th174 on 1/29/2017.
 */
public abstract class Abstract_CellState<E extends Abstract_CellState<E, T>, T> implements Comparable<E> {

    private T myState;

    protected Abstract_CellState(T state) {
        myState = state;
    }

    /**
     * Compares two CellState objects. Throws an error if the two states cannot be compared.
     *
     * @param state to be compared to
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public abstract int compareTo(E state);

    public int getNumOfAgents() {
        return 0;
    }

    /**
     * Compares two CellState objects for equality. Throws and error if the two states cannot be compared.
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

    public abstract E getSuccessorState();

    public abstract Set<T> getDistinctCellStates();

    protected T getState() {
        return myState;
    }

    public String toString() {
        return String.format("\n\t\t<State>%s</State>", myState.toString());
    }
}
