package CellSociety.PredatorPrey;

public class Predator extends Animal{
	private int predMovesSinceEaten;
	private int reproductionTime;
	private int movesSinceReproduction;
	private final int daysToStarvation;
	private int movesSinceEaten;
	public Predator(int reproduction, int starvation) {		
		super(reproduction);
		daysToStarvation=starvation;
		predMovesSinceEaten=0;
		movesSinceEaten=0;
	}
	public void resetAll(){
		movesSinceReproduction=0;
		movesSinceEaten=0;
	}
	public boolean canReproduce(){
		return movesSinceReproduction>=reproductionTime;
	}
	public boolean isStarved(){
		return movesSinceEaten>=daysToStarvation;
	}

}
