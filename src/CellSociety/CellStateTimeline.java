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

    public CellStateTimeline(T initialState) {
        this();
        myStateTimeline.add(initialState);
        myStateTimeline.add(initialState);
    }

    public CellStateTimeline() {
        currentIndex = 0;
        isReversed = false;
        myStateTimeline = new ArrayList<>();
    }

    public void seek(int index) {
        if (index <= size() - 2) {
            currentIndex = index;
        } else {
            currentIndex = size() - 2;
        }
    }

    public void advance() {
        currentIndex += isReversed ? -1 : 1;
        if (currentIndex < 0) {
            currentIndex = 0;
        }
    }

    public void reverse() {
        isReversed = !isReversed;
    }

    public int getIndex() {
        return currentIndex;
    }

    public T getCurrentState() {
        return myStateTimeline.get(currentIndex);
    }

    public T getNextState() {
        if (size() == currentIndex + 1) {
            return getCurrentState();
        }
        return myStateTimeline.get(currentIndex + 1);
    }

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

    public int size() {
        return myStateTimeline.size();
    }
}
