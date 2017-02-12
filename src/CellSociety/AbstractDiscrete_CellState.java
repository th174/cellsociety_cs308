package CellSociety;


import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by th174 on 1/31/2017.
 */
public abstract class AbstractDiscrete_CellState<E extends AbstractDiscrete_CellState<E, T>, T extends Enum<T>> extends Abstract_CellState<E, T> {

    protected AbstractDiscrete_CellState(T state) {
        super(state);
    }

    protected static <E extends Enum<E>> E randomState(Class<E> enumState) {
        return enumState.getEnumConstants()[(int) (Math.random() * enumState.getEnumConstants().length)];
    }

    @Override
    public Set<T> getDistinctCellStates() {
        return Arrays.stream(getState().getClass().getEnumConstants()).map(e -> (T) e).collect(Collectors.toSet());
    }

    @Override
    public int compareTo(E cellState) {
        return getState().compareTo(cellState.getState());
    }
}
