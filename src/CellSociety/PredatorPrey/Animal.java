package CellSociety.PredatorPrey;

public abstract class Animal {
    private int reproductionTime;
    private int movesSinceReproduction;

    public Animal(int reproduction) {
        reproductionTime = reproduction;
        movesSinceReproduction = 0;
    }

    public abstract boolean canReproduce();

}
