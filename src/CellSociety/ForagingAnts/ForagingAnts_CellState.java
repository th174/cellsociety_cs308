package CellSociety.ForagingAnts;

import CellSociety.Abstract_CellState;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public final class ForagingAnts_CellState extends Abstract_CellState<ForagingAnts_CellState, ForagingAnts_CellState.ForagingAntsState> {

    private int ants;
    private int foodPheromone;
    private int homePheromone;
    private Collection<Ant> myAnts;
    private int maxCapacity = 10;
    private int maxFoodPheromone = 100;
    private int maxHomePheromone = 100;
    private int pheromoneConstant = 10;

    protected ForagingAnts_CellState(ForagingAntsState state) {
        super(state);
        myAnts = new ArrayList<Ant>();
        ants = 0;
        foodPheromone = 0;
        homePheromone = 0;
    }

    enum ForagingAntsState {
        HOME, SOURCE, EMPTY, OBSTACLE
    }

    @Override
    public Color getFill() {
        return getState().equals(ForagingAntsState.EMPTY) ? Color.BLUE : getState().equals(ForagingAntsState.SOURCE) ?
                Color.DARKGRAY : getState().equals(ForagingAntsState.OBSTACLE) ? Color.WHITESMOKE : Color.DARKOLIVEGREEN;
    }

    public Collection<Ant> getAnts() {
        return myAnts;
    }

    public void addAnt(Ant a) {
        myAnts.add(a);
    }

    public boolean canMoveToCell() {
        return myAnts.size() <= maxCapacity && !getState().equals(ForagingAntsState.OBSTACLE);
    }

    @Override
    public ForagingAnts_CellState getSuccessorState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ForagingAnts_CellState getInactiveState() {
        //TODO: Auto-generated method stub
        return null;
    }

    public int getFoodPheromone() {
        return foodPheromone;
    }

    public int getHomePheromone() {
        return homePheromone;
    }

    @Override
    public int compareTo(ForagingAnts_CellState state) {
        // TODO Auto-generated method stub
        return 0;
    }

    public class foodPheromoneComparator implements Comparator<ForagingAnts_Cell> {

        @Override
        public int compare(ForagingAnts_Cell o1, ForagingAnts_Cell o2) {
            // TODO Auto-generated method stub
            return (o2.getCurrentState().getFoodPheromone() - o1.getCurrentState().getFoodPheromone());
        }

    }

    public class homePheromoneComparator implements Comparator<ForagingAnts_Cell> {

        @Override
        public int compare(ForagingAnts_Cell o1, ForagingAnts_Cell o2) {
            // TODO Auto-generated method stub
            return (o2.getCurrentState().getHomePheromone() - o1.getCurrentState().getHomePheromone());
        }

    }

    public int compareFoodTo(ForagingAnts_CellState state) {
        return (int) (state.getFoodPheromone() - foodPheromone);
    }

    public int compareHomeTo(ForagingAnts_CellState state) {
        return (int) (state.getHomePheromone() - homePheromone);
    }

    public void removeAnt(Ant a) {
        myAnts.remove(a);
    }

    public boolean canDropFoodPheromone() {
        return foodPheromone <= maxFoodPheromone;
    }

    public boolean canDropHomePheromone() {
        return homePheromone <= maxHomePheromone;
    }

    public int getPheromoneConstant() {
        return pheromoneConstant;
    }

    public void addFoodPheromone(int f) {
        foodPheromone += f;
    }

    public void addHomePheromone(int h) {
        homePheromone += h;
    }

    public void setHomePheromoneToMax() {
        homePheromone = maxHomePheromone;
    }

    public void setFoodPheromoneToMax() {
        foodPheromone = maxFoodPheromone;
    }

}
