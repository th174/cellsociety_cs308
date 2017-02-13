package CellSociety.ForagingAnts;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public final class ForagingAnts_CellState extends AbstractDiscrete_CellState<ForagingAnts_CellState, ForagingAnts_CellState.ForagingAntsState> {
    public static final ForagingAnts_CellState HOME = new ForagingAnts_CellState(ForagingAntsState.HOME);
    public static final ForagingAnts_CellState SOURCE = new ForagingAnts_CellState(ForagingAntsState.SOURCE);
    public static final ForagingAnts_CellState EMPTY = new ForagingAnts_CellState(ForagingAntsState.EMPTY);
    public static final ForagingAnts_CellState OBSTACLE = new ForagingAnts_CellState(ForagingAntsState.OBSTACLE);
    
    private int foodPheromone;
    private int homePheromone;
    private Collection<Ant> myAnts;
    private Collection<Ant> nextAnts;
    private int maxCapacity = 10;
    private int maxFoodPheromone = 100;
    private int maxHomePheromone = 100;
    private int pheromoneConstant = 10;
    
    private ForagingAnts_CellState(ForagingAntsState state) {
        super(state);
        myAnts = new ArrayList<Ant>();
        foodPheromone = 0;
        homePheromone = 0;
        nextAnts = new ArrayList<Ant>();
    }

    /**
     * Generates new cell state
     * @param params
     */
    public ForagingAnts_CellState(String... params) {
        super(params[0].toLowerCase().equals("rand") ? randomState(ForagingAntsState.class) : ForagingAntsState.valueOf(params[0].toUpperCase()));
        maxCapacity = params.length > 1 ? Integer.parseInt(params[1]) : 10;
        maxFoodPheromone = params.length > 2 ? Integer.parseInt(params[2]) : 100;
        maxHomePheromone = params.length > 3 ? Integer.parseInt(params[3]) : 100;
        pheromoneConstant = params.length > 4 ? Integer.parseInt(params[4]) : 10;
        myAnts= new ArrayList<Ant>();
        int numberAnts = params.length > 5 ? Integer.parseInt(params[5]) : 0;
        for(int i=0;i<numberAnts;i++){
        	myAnts.add(new Ant(10));
        }
        nextAnts = new ArrayList<Ant>();
        foodPheromone=40;
        homePheromone=40;
        
    }
    public ForagingAnts_CellState(ForagingAnts_CellState.ForagingAntsState state, int maxCapac,int maxFood, int maxHome, int pherConstant,
    		Collection<Ant> ants, int food, int home) {
    	super(state);
    	maxCapacity = maxCapac;
        maxFoodPheromone = maxFood;
        maxHomePheromone = maxHome;
        pheromoneConstant = pherConstant;
        myAnts= ants;
        nextAnts = new ArrayList<Ant>();
        foodPheromone=food;
        homePheromone=home;
    }

    /** 
     * Cells colored according to their states. Blue if empty, gray if source of food
     * white if obstacle and dark green if home.
     * @see CellSociety.Abstract_CellState#getFill()
     */
    @Override
    public Color getFill() {
        return getState().equals(ForagingAntsState.HOME) ? Color.BLUE :
                getState().equals(ForagingAntsState.SOURCE) ? Color.DARKGRAY :
                        getState().equals(ForagingAntsState.OBSTACLE) ? Color.BLACK :
                                foodPheromone > 0.6* maxFoodPheromone? Color.DARKOLIVEGREEN: 
                                	homePheromone > 0.6* maxHomePheromone? Color.DARKBLUE: 
	                                	foodPheromone > 0.1* maxFoodPheromone? Color.DARKSEAGREEN:
	                                		homePheromone > 0.1* maxHomePheromone? Color.LIGHTBLUE: 
	                                			Color.WHITE;
    }

    /**
     * @return ants associated with this cellstate
     */
    public Collection<Ant> getAnts() {
        return myAnts;
    }

    /**
     * @return the number of agents in this cellstate
     */
    public int getNumberOfAgents() {
        return myAnts.size();
    }

    /**
     * Adds ant to this cell's collection of ants of next State
     * @param a Ant to be added
     */
    public void addAnt(Ant a) {
        nextAnts.add(a);
    }
    /**
     * Remove ant to this cell's collection of ants of next State
     * @param a Ant to be added
     */
    public void removeAnt(Ant a) {
        nextAnts.remove(a);
    }

    /**
     * @return true if the number of ants is less than max capacity and this is not an obstacle
     */
    public boolean canMoveToCell() {
        return myAnts.size() <= maxCapacity && !getState().equals(ForagingAntsState.OBSTACLE);
    }

    /**
     * @return the successor state of the cell
     */
    @Override
    public ForagingAnts_CellState getSuccessorState() {
        return new ForagingAnts_CellState(this.getState(),maxCapacity,maxFoodPheromone,maxHomePheromone,pheromoneConstant,myAnts,foodPheromone,homePheromone);
    }

    /**
     * @return amount of food Pheromone in the cell.
     */
    public int getFoodPheromone() {
        return foodPheromone;
    }

    /**
     * @return amount of home Pheromone in the cell
     */
    public int getHomePheromone() {
        return homePheromone;
    }

    /**
     * Compares cellstates to one other. Ones with greater foodPheromone levels are first
     * @param state to be compared with
     * @return negative number if should come before, 0 if equal, positive number if should come after 
     */
    public int compareFoodTo(ForagingAnts_CellState state) {
        return state.getFoodPheromone() - foodPheromone;
    }

    /**
     * Compares cellstates to one other. Ones with greater homePheromone levels are first
     * @param state to be compared with
     * @return negative number if should come before, 0 if equal, positive number if should come after 
     */
    public int compareHomeTo(ForagingAnts_CellState state) {
        return state.getHomePheromone() - homePheromone;
    }


    /**
     * @return True if can drop foodPheromone
     */
    public boolean canDropFoodPheromone() {
        return foodPheromone <= maxFoodPheromone;
    }

    /**
     * @return true if can dropHomePheromone
     */
    public boolean canDropHomePheromone() {
        return homePheromone <= maxHomePheromone;
    }

    /**
     * @return the pheromoneConstant of this cell
     */
    public int getPheromoneConstant() {
        return pheromoneConstant;
    }

    /**
     * @param f amount of food pheromone to be added
     */
    public void addFoodPheromone(int f) {
        foodPheromone += f;
    }

    /**
     * @param h about of home pheromone to be added
     */
    public void addHomePheromone(int h) {
        homePheromone += h;
    }

    /**
     * sets home pheromone to max value
     */
    public void setHomePheromoneToMax() {
        homePheromone = maxHomePheromone;
    }

    /**
     * sets food pheromone to max value
     */
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
