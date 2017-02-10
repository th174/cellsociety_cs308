package CellSociety;


/**
 * Created by th174 on 1/31/2017.
 */
public abstract class AbstractDiscrete_CellState<T extends Enum<T>> extends Abstract_CellState<T> {

    protected AbstractDiscrete_CellState(T state) {
        super(state);
    }

    protected static <E extends Enum<E>> E randomState(Class<E> enumState) {
        return enumState.getEnumConstants()[(int) (Math.random() * enumState.getEnumConstants().length)];
    }

    @Override
    public int compareTo(Abstract_CellState<T> cellState) {
        if (cellState instanceof AbstractDiscrete_CellState) {
            return getState().compareTo(cellState.getState());
        } else {
            throw new Error("invalid state:\tExpected: " + getClass().getName() + "\tActual: " + cellState.getClass().getName());
        }
    }
}
