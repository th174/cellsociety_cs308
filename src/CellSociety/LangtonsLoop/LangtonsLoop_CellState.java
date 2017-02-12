package CellSociety.LangtonsLoop;

import CellSociety.Abstract_CellState;
import CellSociety.SugarScape.SugarScape_CellState;
import javafx.scene.paint.Color;

public final class LangtonsLoop_CellState extends Abstract_CellState<LangtonsLoop_CellState,LangtonsLoop_CellState.LangtonsLoopState> {

	public enum LangtonsLoopState {
		CORE,SHEATH,FORWARD,TURN, EMPTY
	}

	public LangtonsLoop_CellState(LangtonsLoopState state) {
		super(state);
	}

	@Override
	public int compareTo(LangtonsLoop_CellState state) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Color getFill() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LangtonsLoop_CellState getSuccessorState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LangtonsLoop_CellState getInactiveState() {
		// TODO Auto-generated method stub
		return null;
	}

}
