package CellSociety.SlimeMold;

import CellSociety.Grids.SimulationGrid;

public class Turtle {
	private double sniffThreshold;
	private int sniffAngle;
	private int wiggleAngle;
	private int wiggleBias;
	private int currentDirection; //in degrees
	private double amountDeposited;
	public Turtle() {
		this(1.0,45,10,0,0.6);
	}
	public Turtle(double threshold, int sniffang, int wigangle, int wigbias,double amountDeposited){
		sniffThreshold=threshold;
		sniffAngle=sniffang;
		wiggleAngle=wigangle;
		wiggleBias=wigbias;
		currentDirection = (int)(Math.random()*360 +1);//random angle between 0 and 360
	}
	public void depositChemical(SlimeMold_Cell cell){
		cell.getCurrentState().addNextChemical(amountDeposited);
	}
	public boolean followGradient(SlimeMold_CellState state){
		return state.getChemical()>=sniffThreshold;
	}
	public boolean moveToCell(SimulationGrid<SlimeMold_Cell, SlimeMold_CellState> neighborsGrid){
		int leftMostAngle = currentDirection +sniffAngle - wiggleBias;
		int rightMostAngle = currentDirection-sniffAngle-wiggleBias;
		//scan the grid
		//can only move to wherever there isn't already a turtle
		
		
		
		return true;
	}
public boolean moveSameDirection(SimulationGrid<SlimeMold_Cell, SlimeMold_CellState> neighborsGrid){
		
		//wiggles randomly
		double leftOrRight = Math.random()*2 -1; //random number between -1 and 1
		int randomAngle = currentDirection + (int) (leftOrRight * (wiggleAngle -wiggleBias));
		
		//TODO: find cell according to this angle
		
		
		
		SlimeMold_Cell moveTo=null;
		moveTo.getCurrentState().setNextTurtle(this);
		
		return true;
	}
	

}
