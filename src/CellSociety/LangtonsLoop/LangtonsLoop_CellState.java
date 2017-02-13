package CellSociety.LangtonsLoop;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;

public final class LangtonsLoop_CellState extends AbstractDiscrete_CellState<LangtonsLoop_CellState, LangtonsLoop_CellState.LangtonsLoopState> {

    /**
     * Constructs new state
     * @param state
     */
    public LangtonsLoop_CellState(LangtonsLoopState state) {
        super(state);
    }

    /**
     * Constructs new state given a parent state
     * @param cellState
     */
    public LangtonsLoop_CellState(LangtonsLoop_CellState cellState) {
        super(cellState.getState());
    }

    /** 
     * returns the color of the cells according to their type. 8 colors in total
     * @see CellSociety.Abstract_CellState#getFill()
     */
    @Override
    public Color getFill() {
        // TODO Auto-generated method stub
        return Color.BLACK;
    }

    /** 
     * @return successorstate bases on this state's values
     * @see CellSociety.Abstract_CellState#getSuccessorState()
     */
    @Override
    public LangtonsLoop_CellState getSuccessorState() {
        return new LangtonsLoop_CellState(this);
    }


    enum LangtonsLoopState {
    	EMPTY, CORE, SHEATH, TURNSUPPORT, TURN, DISCONNECT, FINISH, GROWTH
    	//black, blue, red,   green,     yellow, pink,      white,   cyan
    }

}
