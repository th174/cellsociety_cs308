package CellSociety.UI.Shapes;

import CellSociety.Abstract_Cell;
import CellSociety.UI.AbstractRegularPolygon_CellView;

/**
 * Created by th174 on 2/11/2017.
 */
public final class Hexagon<E extends Abstract_Cell> extends AbstractRegularPolygon_CellView<E> {
    public static final int HEXAGON_SIDES = 6;
    private static final double LENGTH_OVER_RADIUS = Math.sqrt(3);
    private static final double THIRTY_DEGREES = Math.toRadians(30);
    private static final double HEIGHT_OVER_RADIUS = 1.5;

    /**
     * Constructs a regular Hexagon of a certain cell and outline color
     * @param cell
     * @param outlineColor 
     */
    public Hexagon(E cell, String outlineColor) {
        super(cell, outlineColor);
    }

    @Override
    protected int getNumSides() {
        return HEXAGON_SIDES;
    }

    @Override
    protected double getRotationAngle() {
        return THIRTY_DEGREES;
    }

    @Override
    protected double getRadius(double visibleColumns, double visibleRows, double windowWidth, double windowHeight) {
        return windowWidth / (visibleRows + .5) / LENGTH_OVER_RADIUS;
    }

    @Override
    protected double calculateX(double visibleColumns, double visibleRows, double windowWidth, double windowHeight) {
        double radius = getRadius(visibleColumns, visibleRows, windowWidth, windowHeight);
        return radius * LENGTH_OVER_RADIUS * getCell().getX() + (getCell().getY() % 2 == 1 ? radius * LENGTH_OVER_RADIUS / 2 : 0);
    }

    @Override
    protected double calculateY(double visibleColumns, double visibleRows, double windowWidth, double windowHeight) {
        double radius = getRadius(visibleColumns, visibleRows, windowWidth, windowHeight);
        return getCell().getY() * radius * HEIGHT_OVER_RADIUS;
    }
}