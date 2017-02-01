package CellSociety;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by th174 on 1/31/2017.
 */
public class CellStateTimeline<T extends Abstract_CellState> {
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
        currentIndex = index;
    }

    public void advance() {
        currentIndex += isReversed ? -1 : 1;
    }

    public void reverse() {
        isReversed = !isReversed;
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

    public void append(T newState) {
        myStateTimeline.add(currentIndex+1,newState);
    }

    public int size() {
        return myStateTimeline.size();
    }
}
