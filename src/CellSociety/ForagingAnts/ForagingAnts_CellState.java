package CellSociety.ForagingAnts;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public final class ForagingAnts_CellState extends AbstractDiscrete_CellState<ForagingAnts_CellState, ForagingAnts_CellState.ForagingAntsState> {
    public static final ForagingAnts_CellState HOME = new ForagingAnts_CellState(ForagingAntsState.HOME);
    public static final ForagingAnts_CellState SOURCE = new ForagingAnts_CellState(ForagingAntsState.SOURCE);
    public static final ForagingAnts_CellState EMPTY = new ForagingAnts_CellState(ForagingAntsState.EMPTY);
    public static final ForagingAnts_CellState OBSTACLE = new ForagingAnts_CellState(ForagingAntsState.OBSTACLE);

    private int ants;
    private int foodPheromone;
    private int homePheromone;
    private Collection<Ant> myAnts;
    private int maxCapacity = 10;
    private int maxFoodPheromone = 100;
    private int maxHomePheromone = 100;
    private int pheromoneConstant = 10;

    private ForagingAnts_CellState(ForagingAntsState state) {
        super(state);
        myAnts = new ArrayList<Ant>();
        ants = 0;
        foodPheromone = 0;
        homePheromone = 0;
    }

    public ForagingAnts_CellState(String... params) {
        super(params[0].toLowerCase().equals("rand") ? randomState(ForagingAntsState.class) : ForagingAntsState.valueOf(params[0].toUpperCase()));
        maxCapacity = params.length > 1 ? Integer.parseInt(params[1]) : 10;
        maxFoodPheromone = params.length > 2 ? Integer.parseInt(params[2]) : 100;
        maxHomePheromone = params.length > 3 ? Integer.parseInt(params[3]) : 100;
        pheromoneConstant = params.length > 4 ? Integer.parseInt(params[4]) : 10;
    }

    @Override
    public Color getFill() {
        return getState().equals(ForagingAntsState.EMPTY) ? Color.BLUE :
                getState().equals(ForagingAntsState.SOURCE) ? Color.DARKGRAY :
                        getState().equals(ForagingAntsState.OBSTACLE) ? Color.WHITESMOKE :
                                Color.DARKOLIVEGREEN;
    }

    public Collection<Ant> getAnts() {
        return myAnts;
    }

    public int getNumberOfAgents() {
        return myAnts.size();
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
        return new ForagingAnts_CellState(ForagingAntsState.EMPTY);
    }

    public int getFoodPheromone() {
        return foodPheromone;
    }

    public int getHomePheromone() {
        return homePheromone;
    }

    public int compareFoodTo(ForagingAnts_CellState state) {
        return state.getFoodPheromone() - foodPheromone;
    }

    public int compareHomeTo(ForagingAnts_CellState state) {
        return state.getHomePheromone() - homePheromone;
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

    enum ForagingAntsState {
        HOME, SOURCE, EMPTY, OBSTACLE
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
}
