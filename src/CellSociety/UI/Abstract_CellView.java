package CellSociety.UI;

import CellSociety.Abstract_Cell;
import javafx.scene.shape.Shape;

/**
 * Base class providing a resizable graphical representation of a Abstract_Cell. The color of that graphical representation can be modified with setHueShift, setSaturationShift, and setLightnessShift.
 * <p>
 * Created by th174 on 2/7/2017.
 *
 * @param <E> Type of Abstract_Cell represented by this CellView
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

    /**
     * @param visibleColumns Number of visible columns in the simulation window
     * @param visibleRows    Number of visible rows in the simulation window
     * @param windowWidth    Width of window in pixels
     * @param windowHeight   Height of window in pixels
     */
    public void updateView(double visibleColumns, double visibleRows, double windowWidth, double windowHeight) {
        myView.setFill(myCell.getCurrentState().getFill().deriveColor(hueShift, saturationShift, lightnessShift, 1));
    }

    protected void setView(Shape view) {
        myView = view;
    }

    /**
     * Sets the hueShift to the specifies amount.Can be used to adjust color.
     *
     * @param amount Number of degrees from 0 to 360 that hue is shifted
     */
    public void setHueShift(double amount) {
        hueShift = amount;
    }

    /**
     * Sets saturationShift to the specified amount. Can be used to adjust color.
     *
     * @param amount Number that the base saturation is multiplied by
     */
    public void setSaturationShift(double amount) {
        saturationShift = amount;
    }

    /**
     * Sets LightessShift to the specified amount. Can be used to adjust color.
     *
     * @param amount Number that the base lightness is multiplied by
     */
    public void setLightnessShift(double amount) {
        lightnessShift = amount;
    }

    protected Shape getView() {
        return myView;
    }

    protected E getCell() {
        return myCell;
    }
}
