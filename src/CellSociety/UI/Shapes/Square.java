package CellSociety.UI.Shapes;

import CellSociety.Abstract_Cell;
import CellSociety.UI.AbstractRegularPolygon_CellView;

/**
 * This class provides a square graphical representation of an Abstract_Cell
 *
 * @param <E> Type of Abstract_Cell represented by this CellView
 * @see AbstractRegularPolygon_CellView
 * Created by th174 on 2/11/2017.
 */
public final class Square<E extends Abstract_Cell> extends AbstractRegularPolygon_CellView<E> {
    public static final int SQUARE_SIDES = 4;
    private static final double SIDE_LENGTH_OVER_RADIUS = 2 / Math.sqrt(2);
    private static final double QUARTER_SPIN = Math.toRadians(45);

    /**
     * Constructs new Triangle of a certain cell and outline color
     *
     * @see AbstractRegularPolygon_CellView#AbstractRegularPolygon_CellView(Abstract_Cell, String)
     */
    public Square(E cell, String outlineColor) {
        super(cell, outlineColor);
    }

    /**
     * @see AbstractRegularPolygon_CellView#getNumSides()
     */
    @Override
    protected int getNumSides() {
        return SQUARE_SIDES;
    }

    /**
     * @see AbstractRegularPolygon_CellView#getRotationAngle()
     */
    @Override
    protected double getRotationAngle() {
        return QUARTER_SPIN;
    }

    /**
     * @see AbstractRegularPolygon_CellView#getRadius(double, double, double, double)
     */
    @Override
    protected double getRadius(double visibleColumns, double visibleRows, double windowWidth, double windowHeight) {
        return windowWidth / visibleColumns / SIDE_LENGTH_OVER_RADIUS;
    }

    /**
     * @see AbstractRegularPolygon_CellView#calculateX(double, double, double, double)
     */
    @Override
    protected double calculateX(double visibleColumns, double visibleRows, double windowWidth, double windowHeight) {
        double radius = getRadius(visibleColumns, visibleRows, windowWidth, windowHeight);
        return getCell().getX() * radius * SIDE_LENGTH_OVER_RADIUS;
    }

    /**
     * @see AbstractRegularPolygon_CellView#calculateY(double, double, double, double)
     */
    @Override
    protected double calculateY(double visibleColumns, double visibleRows, double windowWidth, double windowHeight) {
        double radius = getRadius(visibleColumns, visibleRows, windowWidth, windowHeight);

        return getCell().getY() * radius * SIDE_LENGTH_OVER_RADIUS;
    }
}
