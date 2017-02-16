//This entire fire is part of my masterpiece
//Tim Overeem (to41)

/*
 *  I think this represents good design because it a good use of inheritance and this feature is very clearly
 *  open for extension. The Segregation_Cell subclass shows how this is done, and I can be certain that
 *  any new simulation that inherits this class with perform all the methods it needs to for the program to work.
 *  This class is relatively short, the proper methods are abstract or implemented. 
 *  The SimulationGrid can call on this class and be sure that it will interact properly and perform the proper
 *  functions, based on which subclass is actually relevant.
 *  
 *  Although Timmy laid out the foundation for this class, I was very involved in the brainstorming and would
 *  give him a lot of feedback. This class is definitely a collaborative effort between us two. It was just a 
 *  nature of our teamwork and flexibility that determined who actually wrote and committed the code.
 */

package CellSociety;

import CellSociety.Grids.SimulationGrid;

import java.util.Objects;

/**
 * Base class for modeling a single Cell with a CellState inside a SimulationGrid
 *
 * @param <E> The Abstract_Cell type subclass
 * @param <T> The AbstractDiscrete_CellState type of this cell at any moment in time
 *            Created by th174 on 1/29/2017.
 */
public abstract class Abstract_Cell<E extends Abstract_Cell<E, T>, T extends AbstractDiscrete_CellState<T, ? extends Enum<?>>> {
    private final int xPos;
    private final int yPos;
    private final CellStateTimeline<T> myTimeline;
    private SimulationGrid<E, T> parentGrid;

    protected Abstract_Cell(int x, int y, T state) {
        xPos = x;
        yPos = y;
        myTimeline = new CellStateTimeline<>(state);
    }

    /**
     * Advances currentState to nextState
     * This is the only way currentState can be modified
     */
    public final void updateState() {
        myTimeline.advance();
        setNextState(getCurrentState().getSuccessorState());
    }

    /**
     * Determines how the cells interact
     */
    public abstract void interact();

    /**
     * @return Grid of neighboring cells. See SimulationGrid::getNeighbors
     */
    protected final SimulationGrid<E, T> getNeighbors() {
        if (Objects.nonNull(parentGrid)) {
            return parentGrid.getNeighbors(xPos, yPos);
        } else {
            return null;
        }
    }

    /**
     * Goes to the index in the timeline, can jump to any visited CellState.
     *
     * @param index index to jump to
     * @see CellStateTimeline#seek(int)
     */
    public final void seek(int index) {
        myTimeline.seek(index);
    }

    /**
     * Reverses the animation.
     *
     * @see CellStateTimeline#reverse()
     */
    public final void reverse() {
        myTimeline.reverse();
    }

    /**
     * @return current index of the Timeline
     * @see CellStateTimeline#getCurrentIndex()
     */
    public final int getCurrentIndex() {
        return myTimeline.getCurrentIndex();
    }

    /**
     * @return max index of the Timeline
     * @see CellStateTimeline#getMaxIndex()
     */
    public final int getMaxIndex() {
        return myTimeline.getMaxIndex();
    }

    protected SimulationGrid<E, T> getParentGrid() {
        return parentGrid;
    }

    /**
     * Set the SimulationGrid this cell is a part of
     *
     * @param grid parent SimulationGrid
     */
    public void setParentGrid(SimulationGrid<E, T> grid) {
        parentGrid = grid;
    }

    /**
     * @return currentState of cell
     */
    public final T getCurrentState() {
        return myTimeline.getCurrentState();
    }

    /**
     * @return nextState of cell
     */
    public final T getNextState() {
        return myTimeline.getNextState();
    }

    /**
     * Sets the state that this cell will change into the next time updateState is called
     *
     * @param state CellState on next update
     */
    public final void setNextState(T state) {
        myTimeline.setNextState(state);
    }

    /**
     * @return x position of this cell in parent SimulationGrid
     */
    public int getX() {
        return xPos;
    }

    /**
     * @return y position of the cell in parent SimulationGrid
     */
    public int getY() {
        return yPos;
    }

    /**
     * @return XML formatted representation of this Abstract_Cell
     */
    @Override
    public String toString() {
        return String.format("\n\t<Cell xPos=\"%d\" yPos=\"%d\">%s\n\t</Cell>", getX(), getY(), getCurrentState());
    }
}
