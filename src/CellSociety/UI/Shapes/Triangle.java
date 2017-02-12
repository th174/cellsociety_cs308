package CellSociety.UI.Shapes;

import CellSociety.Abstract_Cell;
import CellSociety.UI.AbstractRegularPolygon_CellView;

/**
 * Created by th174 on 2/11/2017.
 */
public final class Triangle<E extends Abstract_Cell> extends AbstractRegularPolygon_CellView<E> {
    public static final int TRIANGLE_SIDES = 3;
    private static final double UPSIDE_DOWN = Math.toRadians(180);
    private static final double SIDE_LENGTH_OVER_RADIUS = Math.sqrt(3);
    private static final double WIDTH_OVER_RADIUS = 1.5;

    public Triangle(E cell, String outlineColor) {
        super(cell, outlineColor);
    }

    @Override
    protected int getNumSides() {
        return TRIANGLE_SIDES;
    }

    @Override
    protected double getRotationAngle() {
        return (getCell().getX() + getCell().getY()) % 2 == 0 ? 0 : UPSIDE_DOWN;
    }

    @Override
    protected double getRadius(double visibleColumns, double visibleRows, double windowWidth, double windowHeight) {
        return windowWidth / visibleColumns / WIDTH_OVER_RADIUS;
    }

    @Override
    protected double calculateX(double visibleColumns, double visibleRows, double windowWidth, double windowHeight) {
        double radius = getRadius(visibleColumns, visibleRows, windowWidth, windowHeight);
        return getCell().getX() * radius * WIDTH_OVER_RADIUS;
    }

    @Override
    protected double calculateY(double visibleColumns, double visibleRows, double windowWidth, double windowHeight) {
        double radius = getRadius(visibleColumns, visibleRows, windowWidth, windowHeight);
        return getCell().getY() * radius * SIDE_LENGTH_OVER_RADIUS / 2;
    }
}
