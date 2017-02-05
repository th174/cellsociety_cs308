package CellSociety.PredatorPrey;

public class Prey extends Animal {
    int reproductionTime;
    int movesSinceReproduction;
    boolean dead;

    public Prey(int reproduction) {
        super(reproduction);
        reproductionTime = reproduction;
        movesSinceReproduction = 0;
        dead=false;

    }

    public void reset() {
        movesSinceReproduction = 0;
    }

    public boolean canReproduce() {
    	if(movesSinceReproduction >= reproductionTime) {
    		movesSinceReproduction = 0;
    		return true;
    	}
        return false;
    }

	@Override
	public void updateMovesSinceReproduction() {
		movesSinceReproduction++;
		
	}

	@Override
	public PredatorPreyCell_State getState() {
		return PredatorPreyCell_State.PREY;
	}

	@Override
	public void updateMovesSinceEaten() {
		return;	
	}

	@Override
	public void resetMovesSinceEaten() {
		return;
	}

	@Override
	public boolean isDead() {
		return dead;
	}


	@Override
	public void kill() {
		dead=true;
	}

}
