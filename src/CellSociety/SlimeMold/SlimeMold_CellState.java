package CellSociety.SlimeMold;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;

public class SlimeMold_CellState extends AbstractDiscrete_CellState<SlimeMold_CellState, SlimeMold_CellState.SlimeMoldState> {
	private double chemical;
	private double evaporationRate;
	private double diffusionRate;
	private Turtle myTurtle;
	private double threshold;//min required, so green
	
	private double nextChemical;
	private Turtle nextTurtle;
	

	public enum SlimeMoldState {
		EMPTY, TURTLE, 
	}

	public SlimeMold_CellState(SlimeMoldState state) {
		this(state,null);
	}
	public SlimeMold_CellState(SlimeMoldState state, Turtle turt) {
		super(state);
		myTurtle = turt;
	}
	public SlimeMold_CellState(SlimeMoldState state, Turtle turt, double chem, double evaporation, double diffusion) {
		super(state);
		myTurtle = turt;
		chemical=chem;
		evaporationRate=evaporation;
		diffusionRate=diffusion;
		nextTurtle=null;
	}

	@Override
	public Color getFill() {
		//color according to amount of chemical or trutle
		return chemical>3*threshold? Color.WHITE: chemical>2*threshold ? Color.GREENYELLOW:
				chemical>threshold? Color.GREEN: Color.BLACK;
	}

	@Override
	public SlimeMold_CellState getSuccessorState() {
		return new SlimeMold_CellState(SlimeMoldState.TURTLE,nextTurtle,nextChemical,evaporationRate,diffusionRate);
	}

	@Override
	public SlimeMold_CellState getInactiveState() {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean hasTurtle(){
		return myTurtle!=null;
	}
	public Turtle getTurtle(){
		return myTurtle;
	}
	public double getChemical(){
		return chemical;
	}
	public void setNextChemical(double nextChem){
		nextChemical = nextChem;
	}
	public void evaporate(){
		chemical = chemical *evaporationRate;
		nextChemical=chemical;
	}
	public double getDiffusionAmount(){
		chemical-=chemical*diffusionRate;
		nextChemical=chemical;
		return chemical*diffusionRate;
	}
	public void addNextChemical(double addChem){
		nextChemical+=addChem;
	}
	public void setNextTurtle(Turtle t){
		nextTurtle = t;
	}
	public Turtle getNextTurtle(){
		return nextTurtle;
	}

}
