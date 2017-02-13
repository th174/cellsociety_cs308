package CellSociety.ForagingAnts;

public class Ant {
    private boolean food;
    private int life;
    private int orientation;


    /**
     * intializes Ant with lifetime
     * @param lifetime
     */
    public Ant(int lifetime) {
        life = lifetime;
        food = false;
    }

    /**
     * Decrements the moves the ant has left before dying
     */
    public void growOlder() {
        life--;
    }

    /**
     * @return true if the ant is dead.
     */
    public boolean isDead() {
        return life <= 0;
    }

    /**
     * @return true if the ant is holding food.
     */
    public boolean hasFood() {
        return food;
    }

    /**
     * @return the orientation of the ant. Given as an integer (degrees)
     */
    public int getOrientation() {
        return orientation;
    }

    /**
     * Sets the new orientation of the ant
     * @param newOrientation
     */
    public void setOrientation(int newOrientation) {
        orientation = newOrientation;
    }

    /**
     * Ant picks up food. Sets value of food to true.
     */
    public void pickUpFood() {
        food = true;
    }

    /**
     * Ant drops food, sets avlue of food to false.
     */
    public void dropFood() {
        food = false;
    }

}
