package CellSociety.PredatorPrey;

public class Prey extends Animal {
    int reproductionTime;
    int movesSinceReproduction;

    public Prey(int reproduction) {
        super(reproduction);
        reproductionTime = reproduction;
        movesSinceReproduction = 0;

    }

    public void reset() {
        movesSinceReproduction = 0;
    }

    public boolean canReproduce() {
        return movesSinceReproduction >= reproductionTime;
    }

}
