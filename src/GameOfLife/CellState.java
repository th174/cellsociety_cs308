package GameOfLife;

/**
 * Created by th174 on 1/29/2017.
 */
public final class CellState extends CellSociety.CellState {
    public static final CellSociety.CellState DEAD = new CellState(GOLstate.DEAD);
    public static final CellSociety.CellState ALIVE = new CellState(GOLstate.ALIVE);

    private enum GOLstate {
        ALIVE, DEAD
    }

    private GOLstate myState;

    public CellState() {
        this(GOLstate.DEAD);
    }

    public CellState(GOLstate state) {
        myState = state;
    }

    public CellState(String s){
        myState = GOLstate.valueOf(s);
    }

    @Override
    public int compareTo(CellSociety.CellState state) {
        if (state instanceof CellState) {
            return getState() == ((CellState) state).getState() ? 0 : 1;
        } else {
            throw new Error("invalid state:\tCellState.class != CellState.class");
        }
    }

    private GOLstate getState() {
        return myState;
    }

    @Override
    public String toString(){
        return myState.toString();
    }
}
