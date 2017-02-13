package CellSociety;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by th174 on 1/31/2017.
 */
public class CellStateTimeline<T extends Abstract_CellState<T, ?>> {
    private int currentIndex;
    private boolean isReversed;
    private List<T> myStateTimeline;

    /**
     * initializes timeline
     * @param initialState
     */
    public CellStateTimeline(T initialState) {
        this();
        myStateTimeline.add(initialState);
        myStateTimeline.add(initialState);
    }

    /**
     * Constructs timeline with default parameters
     */
    public CellStateTimeline() {
        currentIndex = 0;
        isReversed = false;
        myStateTimeline = new ArrayList<>();
    }

    /**
     * Advances in the timeline to the specified index
     * @param index
     */
    public void seek(int index) {
        if (index <= size() - 2) {
            currentIndex = index;
        } else {
            currentIndex = size() - 2;
        }
    }

    /**
     * Advances the timeline according to its order. Goes in reversed order if activated
     */
    public void advance() {
        currentIndex += isReversed ? -1 : 1;
        if (currentIndex < 0) {
            currentIndex = 0;
        }
    }

    /**
     * Reverses the timeline
     */
    public void reverse() {
        isReversed = !isReversed;
    }

    /**
     * @return current Index of the timeline
     */
    public int getIndex() {
        return currentIndex;
    }

    /**
     * @return the current state of the cell
     */
    public T getCurrentState() {
        return myStateTimeline.get(currentIndex);
    }

    /**
     * @return the next state of the cell
     */
    public T getNextState() {
        if (size() == currentIndex + 1) {
            return getCurrentState();
        }
        return myStateTimeline.get(currentIndex + 1);
    }

    /**
     * Sets the next state of the cell.
     * @param newState
     * @return true if can set the next state
     */
    public boolean setNextState(T newState) {
        if (size() == currentIndex + 1) {
            myStateTimeline.add(newState);
        } else if (size() == currentIndex + 2) {
            myStateTimeline.set(currentIndex + 1, newState);
        } else if (size() > currentIndex + 2) {
            return false;
        }
        return true;
    }

    /**
     * @return size of the timeline to this point
     */
    public int size() {
        return myStateTimeline.size();
    }

    /**
     * @return current index of the timeline
     */
    public int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * @return the max index of the timeline at this point
     */
    public int getMaxIndex() {
        return myStateTimeline.size() - 1;
    }
}
