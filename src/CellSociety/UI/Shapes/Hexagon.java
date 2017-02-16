package CellSociety.UI.Shapes;

import CellSociety.Abstract_Cell;
import CellSociety.UI.AbstractRegularPolygon_CellView;

/**
 * This class provides a regular hexagon graphical representation of an Abstract_Cell.
 * <p>
 * Created by th174 on 2/11/2017.
 *
 * @param <E> Type of Abstract_Cell represented by this CellView
 * @see AbstractRegularPolygon_CellView
 */
public final class Hexagon<E extends Abstract_Cell> extends AbstractRegularPolygon_CellView<E> {
    public static final int HEXAGON_SIDES = 6;
    private static final double LENGTH_OVER_RADIUS = Math.sqrt(3);
    private static final double THIRTY_DEGREES = Math.toRadians(30);
    private static final double HEIGHT_OVER_RADIUS = 1.5;

    /**
     * Constructs new Hexagon of a certain cell and outline color
     *
     * @see AbstractRegularPolygon_CellView#AbstractRegularPolygon_CellView(Abstract_Cell, String)
     */
    public Hexagon(E cell, String outlineColor) {
        super(cell, outlineColor);
    }

    /**
     * @see AbstractRegularPolygon_CellView#getNumSides()
     */
    @Override
    protected int getNumSides() {
        return HEXAGON_SIDES;
    }

    /**
     * @see AbstractRegularPolygon_CellView#getRotationAngle()
     */
    @Override
    protected double getRotationAngle() {
        return THIRTY_DEGREES;
    }

    /**
     * @see AbstractRegularPolygon_CellView#getRadius(double, double, double, double)
     */
    @Override
    protected double getRadius(double visibleColumns, double visibleRows, double windowWidth, double windowHeight) {
        return windowWidth / (visibleRows + .5) / LENGTH_OVER_RADIUS;
    }

    /**
     * @see AbstractRegularPolygon_CellView#calculateX(double, double, double, double)
     */
    @Override
    protected double calculateX(double visibleColumns, double visibleRows, double windowWidth, double windowHeight) {
        double radius = getRadius(visibleColumns, visibleRows, windowWidth, windowHeight);
        return radius * LENGTH_OVER_RADIUS * getCell().getX() + (getCell().getY() % 2 == 1 ? radius * LENGTH_OVER_RADIUS / 2 : 0);
    }

    /**
     * @see AbstractRegularPolygon_CellView#calculateY(double, double, double, double)
     */
    @Override
    protected double calculateY(double visibleColumns, double visibleRows, double windowWidth, double windowHeight) {
        double radius = getRadius(visibleColumns, visibleRows, windowWidth, windowHeight);
        return getCell().getY() * radius * HEIGHT_OVER_RADIUS;
    }
}