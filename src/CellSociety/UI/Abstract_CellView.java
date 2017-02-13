package CellSociety.UI;

import CellSociety.Abstract_Cell;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Shape;


/**
 * Created by th174 on 2/7/2017.
 */
public abstract class Abstract_CellView<E extends Abstract_Cell> {
    private E myCell;
    protected BorderPane myView;
    private Shape myShape;
    private double hueShift;
    private double saturationShift;
    private double lightnessShift;

    protected Abstract_CellView(E cell) {
        myCell = cell;
        hueShift = 0;
        saturationShift = 1;
        lightnessShift = 1;
        myView = new BorderPane();
    }

    /**
     * @param visibleColumns
     * @param visibleRows
     * @param windowWidth
     * @param windowHeight
     */
    public void updateView(double visibleColumns, double visibleRows, double windowWidth, double windowHeight) {
        myShape.setFill(myCell.getCurrentState().getFill().deriveColor(hueShift, saturationShift, lightnessShift, 1));
    }

    protected void setShape(Shape view) {
        myShape = view;
    }

    /**
     * Sets the hueShift to the specifies amount.Can be used to adjust color.
     * @param amount
     */
    public void setHueShift(double amount) {
        hueShift = amount;
    }

    /**
     * Sets saturationShift to the specified amount. Can be used to adjust color.
     * @param amount
     */
    public void setSaturationShift(double amount) {
        saturationShift = amount;
    }

    /**
     * Sets LightessShift to the specified amount. Can be used to adjust color.
     * @param amount
     */
    public void setLightnessShift(double amount) {
        lightnessShift = amount;
    }

    protected Shape getView() {
        return myShape;
    }

    protected abstract Node getContent();

    protected E getCell() {
        return myCell;
    }
}
