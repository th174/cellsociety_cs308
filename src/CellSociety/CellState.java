package CellSociety;

import javafx.scene.paint.Paint;

/**
 * Created by th174 on 1/29/2017.
 */
public abstract class CellState implements Comparable<CellState> {

    @Override
    public abstract int compareTo(CellState state);

    @Override
    public boolean equals(Object state) {
        return compareTo((CellState) state) == 0;
    }

    public abstract Paint getFill();
}
