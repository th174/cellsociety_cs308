package CellSociety.UI;

import CellSociety.Abstract_Cell;
import javafx.scene.shape.Rectangle;

/**
 * Created by th174 on 2/4/2017.
 */
public class CellView extends Rectangle {
    private Abstract_Cell myCell;
    public static final double CELL_BORDER = .5;

    public CellView(Abstract_Cell cell, CellSocietyView myUI) {
        super();
        myCell = cell;
    }

    public void updateView(int columns, int rows, double windowWidth, double windowHeight) {
        setFill(myCell.getState().getFill());
        setWidth(windowWidth / columns - CELL_BORDER * 2);
        setHeight(windowHeight / rows - CELL_BORDER * 2);
        setX(windowWidth * myCell.getX() / columns + CELL_BORDER);
        setY(windowHeight * myCell.getY() / rows + CELL_BORDER);
    }
}
