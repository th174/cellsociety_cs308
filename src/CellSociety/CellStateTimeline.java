package CellSociety;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores all previously visited CellStates on a navigable timeline. It can be advanced, appended to, seeked, and reversed.
 * @param <T> Type of CellStates on this Timeline
 * Created by th174 on 1/31/2017.
 */
public class CellStateTimeline<T extends Abstract_CellState<T, ?>> {
    private int currentIndex;
    private boolean isReversed;
    private List<T> myStateTimeline;

    /**
     * initializes timeline from initial state
     * @param initialState The initial state to be stored on the timeline.
     */
    public CellStateTimeline(T initialState) {
        this();
        myStateTimeline.add(initialState);
        myStateTimeline.add(initialState);
    }

    /**
     * Initializes timeline
     */
    public CellStateTimeline() {
        currentIndex = 0;
        isReversed = false;
        myStateTimeline = new ArrayList<>();
    }

    /**
     * Advances in the timeline to the specified index
     * @param index index of timeline to jump to
     */
    public void seek(int index) {
        if (index <= size() - 2) {
            currentIndex = index;
        } else {
            currentIndex = size() - 2;
        }
    }

    /**
     * Advances the timeline according to its order. Goes in reversed order if isReversed
     */
    public void advance() {
        currentIndex += isReversed ? -1 : 1;
        if (currentIndex < 0) {
            currentIndex = 0;
        }
    }

    /**
     * Reverses the order the timeline advances
     */
    public void reverse() {
        isReversed = !isReversed;
    }

    /**
     * @return CellState corresponding to the current index
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
     * Appends the next state of the cell to the index immediately after the currentIndex. Does nothing if the currentIndex is not at the end of the timeline
     * @param newState The successor to the current CellState
     * @return true if can set the next state was set successfully
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
     * @return Total number of CellStates recorded on the Timeline
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
