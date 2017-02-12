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
    private double saturationShift;
    private double lightnessShift;

    protected Abstract_CellView(E cell) {
        myCell = cell;
        hueShift = 0;
        saturationShift = 1;
        lightnessShift = 1;
    }

    public void updateView(double visibleColumns, double visibleRows, double windowWidth, double windowHeight) {
        myView.setFill(myCell.getCurrentState().getFill().deriveColor(hueShift, saturationShift, lightnessShift, 1));
    }

    protected void setShape(Shape view) {
        myView = view;
    }

    public void setHueShift(double amount) {
        hueShift = amount;
    }

    public void setSaturationShift(double amount) {
        saturationShift = amount;
    }

    public void setLightnessShift(double amount) {
        lightnessShift = amount;
    }

    public Shape getView() {
        return myView;
    }

    protected E getCell() {
        return myCell;
    }
}
