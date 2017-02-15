package CellSociety;


import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class provides a general implementation of a Discrete CellState, where the state can be represented in the form of an Enumeration. In addition, this also provides a method to obtain a random state from an enum of sttates
 * @param <E> The AbstractDiscrete_CellState type subclass
 * @param <T> The type extending Enum used to represent the CellState
 * Created by th174 on 1/31/2017.
 */
public abstract class AbstractDiscrete_CellState<E extends AbstractDiscrete_CellState<E, T>, T extends Enum<T>> extends Abstract_CellState<E, T> {

    protected AbstractDiscrete_CellState(T state) {
        super(state);
    }

    protected static <E extends Enum<E>> E randomState(Class<E> enumState) {
        return enumState.getEnumConstants()[(int) (Math.random() * enumState.getEnumConstants().length)];
    }

    /**
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
     * @see CellSociety.Abstract_CellState#compareTo(CellSociety.Abstract_CellState)
     */
    @Override
    public int compareTo(E cellState) {
        return getState().compareTo(cellState.getState());
    }
}
