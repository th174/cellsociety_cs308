package CellSociety.PredatorPrey;

public abstract class Animal {
    private int reproductionTime;
    private int movesSinceReproduction;

    public Animal(int reproduction) {
        reproductionTime = reproduction;
        movesSinceReproduction = 0;
    }
    public boolean isPrey(){
    	return this instanceof Prey;
    }
    public boolean isPred(){
    	return this instanceof Predator;
    }
    public abstract void updateMovesSinceReproduction();
    
    public abstract void updateMovesSinceEaten();
    
    public abstract void resetMovesSinceEaten();
    
    public abstract boolean canReproduce();
    
    public abstract PredatorPreyCell_State getState();
    
    public abstract boolean isDead();
    
    public abstract void kill();
}
