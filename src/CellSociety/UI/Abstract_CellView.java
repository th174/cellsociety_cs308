package CellSociety.UI;

import CellSociety.Abstract_Cell;
import javafx.scene.shape.Shape;


/**
 * Created by th174 on 2/7/2017.
 */
public abstract class Abstract_CellView<E extends Abstract_Cell> {
    private E myCell;
    private Shape myView;
    private double hueShift;

    protected Abstract_CellView(E cell) {
        myCell = cell;
    }

    public void updateView(double visibleColumns, double visibleRows, double windowWidth, double windowHeight) {
        myView.setFill(myCell.getCurrentState().getFill().deriveColor(hueShift, 1, 1, 1));
    }

    protected void setShape(Shape view) {
        myView = view;
    }

    public void setHueShift(double hueShiftAmount) {
        hueShift = hueShiftAmount;
    }

    public Shape getView() {
        return myView;
    }

    protected E getCell() {
        return myCell;
    }
}
