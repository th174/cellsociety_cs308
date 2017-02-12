package CellSociety.ForagingAnts;

public class Ant {
    private boolean food;
    private int life;
    private int orientation;


    public Ant(int lifetime) {
        life = lifetime;
        food = false;
    }

    public void growOlder() {
        life--;
    }

    public boolean isDead() {
        return life <= 0;
    }

    public boolean hasFood() {
        return food;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int newO) {
        orientation = newO;
    }

    public void pickUpFood() {
        food = true;
    }

    public void dropFood() {
        food = false;
    }

}
