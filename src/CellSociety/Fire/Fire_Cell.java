package CellSociety.Fire;

import CellSociety.Abstract_Cell;

import java.util.ArrayList;

/**
 * Created by th174 on 1/29/2017.
 */
public class Fire_Cell extends Abstract_Cell<Fire_CellState> {
	public static final double DEFAULT_PROB_CATCH_FIRE = 0.5;
	private double probCatchFire;


	public Fire_Cell(int x, int y, String... params) {
		this(x, y, new Fire_CellState(params[0]), DEFAULT_PROB_CATCH_FIRE);
		if (params.length > 1) {
			probCatchFire = Double.parseDouble(params[1]);
		}
	}

	public Fire_Cell(int x, int y, Fire_CellState state, double chanceFire) {
		super(x, y, state);
		probCatchFire = chanceFire;
	}

	@SuppressWarnings("unused")
	/* private void neighbor() {
       // ArrayList<Abstract_Cell> adjNeighbors = new ArrayList<>(getAdjNeighbors().asCollection());
        if (getState().equals(Fire_CellState.BURNING)) {
        	if(Math.random()<DEFAULT_PROB_CATCH_FIRE){
        		setState(Fire_CellState.TREE);
        	}
        	else{
        		setState(Fire_CellState.BURNING);
        	}

        }
        if(getState().equals(Fire_CellState.EMPTY)||getState().equals(Fire_CellState.TREE)){
        	setState(Fire_CellState.getState);
        }
        if(myState)
        //grid.asCollection().stream().filter(e->e instance of Segregation_Cell && ((Segregation_Cell) e).nextStateE
    }
	 */
	@Override
	public void interact() {
		// ArrayList<Abstract_Cell> adjNeighbors = new ArrayList<>(getAdjNeighbors().asCollection());
		int AdjBurning=(int) getAdjNeighbors().asCollection().stream().filter(e ->e.getState().equals(Fire_CellState.BURNING)).count();
		if (AdjBurning<1 && Math.random()<DEFAULT_PROB_CATCH_FIRE && getState().equals(Fire_CellState.TREE)) {
			setState(Fire_CellState.BURNING);
		}else if(getState().equals(Fire_CellState.BURNING)){
			setState(Fire_CellState.EMPTY);
		} else //(getState().equals(Fire_CellState.EMPTY)|| getState().equals(Fire_CellState.TREE)) {
			if (getState().equals(Fire_CellState.EMPTY)){
				setState(Fire_CellState.EMPTY);
			}else{
				setState(Fire_CellState.TREE);}
	}
	//TODO: Implement this
}

