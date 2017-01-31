package CellSociety;


/**
 * Created by th174 on 1/31/2017.
 */
public abstract class AbstractDiscrete_CellState extends Abstract_CellState {

    public <T extends Enum<T>> T randomState(T myState) {
        return myState.getDeclaringClass().getEnumConstants()[(int) Math.random() * myState.getDeclaringClass().getEnumConstants().length];
    }

    @Override
    public int compareTo(Abstract_CellState cellState) {
        if (cellState instanceof AbstractDiscrete_CellState) {
            return getState().compareTo(((AbstractDiscrete_CellState) cellState).getState());
        } else {
            throw new Error("invalid state:\tExpected: " + getClass().getName() + "\tActual: " + cellState.getClass().getName());
        }
    }

    protected abstract <T extends Enum<T>> T getState();
}
