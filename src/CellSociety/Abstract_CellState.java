package CellSociety;

import javafx.scene.paint.Paint;

/**
 * Created by th174 on 1/29/2017.
 */
public abstract class Abstract_CellState implements Comparable<Abstract_CellState> {

    /**
     * Compares two CellState objects. Throws an error if the two states cannot be compared.
     *
     * @param state to be compared to
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public abstract int compareTo(Abstract_CellState state);

    /**
     * Compares two CellState objects for equality. Throws and error if the two states cannot be compared.
     *
     * @param state to be compared to
     * @return true if two CellState objects share the same state
     */
    @Override
    public boolean equals(Object state) {
        if (!(state instanceof Abstract_CellState)) {
            return false;
        }
        return compareTo((Abstract_CellState) state) == 0;
    }

    /**
     * @return Graphical representation of this CellState
     */
    public abstract Paint getFill();

    public abstract Abstract_CellState getSuccessorState();
}
