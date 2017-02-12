package CellSociety.LangtonsLoop;

import CellSociety.AbstractDiscrete_CellState;
import CellSociety.Abstract_CellState;
import javafx.scene.paint.Color;

public final class LangtonsLoop_CellState extends AbstractDiscrete_CellState<LangtonsLoop_CellState, LangtonsLoop_CellState.LangtonsLoopState> {

    enum LangtonsLoopState {
        CORE, SHEATH, FORWARD, TURN, EMPTY
    }

    public LangtonsLoop_CellState(LangtonsLoopState state) {
        super(state);
    }

    public LangtonsLoop_CellState(LangtonsLoop_CellState cellState) {
        super(cellState.getState());
    }

    @Override
    public Color getFill() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LangtonsLoop_CellState getSuccessorState() {
        return new LangtonsLoop_CellState(this);
    }

    @Override
    public LangtonsLoop_CellState getInactiveState() {
        return new LangtonsLoop_CellState(LangtonsLoopState.EMPTY);
    }

}
