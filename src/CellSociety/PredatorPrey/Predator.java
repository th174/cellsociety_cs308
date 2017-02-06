package CellSociety.PredatorPrey;

public class Predator extends Animal {
    private final int daysToStarvation;
    private int reproductionTime;
    private int movesSinceReproduction;
    private int movesSinceEaten;

    public Predator(int reproduction, int starvation) {
        super(reproduction);
        System.out.println("making a new shark");
        reproductionTime = reproduction;
        daysToStarvation = starvation;
        movesSinceEaten = 0;
    }

    public void resetAll() {
        movesSinceReproduction = 0;
        movesSinceEaten = 0;
    }

    public boolean canReproduce() {
    	if(movesSinceReproduction >= reproductionTime){
    		movesSinceReproduction --;
    		return true;
    	}
        return false;
    }

    public boolean isStarved() {
    	System.out.println(" starving?? " + movesSinceEaten + "  an " +daysToStarvation);
        return movesSinceEaten >= daysToStarvation;
    }
    public void updateMovesSinceEaten(){
    	movesSinceEaten++;
    }

	@Override
	public void updateMovesSinceReproduction() {
		movesSinceReproduction++;
	}
	@Override
	public PredatorPreyCell_State getState() {
		return PredatorPreyCell_State.PREDATOR;
	}

	@Override
	public void resetMovesSinceEaten() {
		movesSinceEaten=0;
		
	}

	@Override
	public boolean isDead() {
		return isStarved();
	}

	@Override
	public void kill() {
		// TODO Auto-generated method stub
		
	}

}
