package CellSociety.LangtonsLoop;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;

public final class LangtonsLoop_CellState extends AbstractDiscrete_CellState<LangtonsLoop_CellState, LangtonsLoop_CellState.LangtonsLoopState> {

    public LangtonsLoop_CellState(LangtonsLoopState state) {
        super(state);
    }

    public LangtonsLoop_CellState(LangtonsLoop_CellState cellState) {
        super(cellState.getState());
    }

    @Override
    public Color getFill() {
        return null;
    }

    @Override
    public LangtonsLoop_CellState getSuccessorState() {
        return new LangtonsLoop_CellState(this);
    }


    enum LangtonsLoopState {
    	EMPTY, CORE, SHEATH, TURNSUPPORT, TURN, DISCONNECT, FINISH, GROWTH
    	//black, blue, red,   green,     yellow, pink,      white,   cyan
    }

}
