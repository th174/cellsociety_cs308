package CellSociety.UI.Shapes;

import CellSociety.Abstract_Cell;
import CellSociety.UI.AbstractRegularPolygon_CellView;
import javafx.scene.paint.Color;

/**
 * Created by th174 on 2/11/2017.
 */
public final class Square<E extends Abstract_Cell> extends AbstractRegularPolygon_CellView<E> {
    public static final int SQUARE_SIDES = 4;
    private static final double SIDE_LENGTH_OVER_RADIUS = 2 / Math.sqrt(2);
    private static final double QUARTER_SPIN = Math.toRadians(45);

    public Square(E cell, String outlineColor) {
        super(cell, outlineColor);
    }

    @Override
    protected int getNumSides() {
        return SQUARE_SIDES;
    }

    @Override
    protected double getRotationAngle() {
        return QUARTER_SPIN;
    }

    @Override
    protected double getRadius(double visibleColumns, double visibleRows, double windowWidth, double windowHeight) {
        return windowWidth / visibleColumns / SIDE_LENGTH_OVER_RADIUS;
    }

    @Override
    protected double calculateX(double visibleColumns, double visibleRows, double windowWidth, double windowHeight) {
        double radius = getRadius(visibleColumns, visibleRows, windowWidth, windowHeight);
        return getCell().getX() * radius * SIDE_LENGTH_OVER_RADIUS;
    }

    @Override
    protected double calculateY(double visibleColumns, double visibleRows, double windowWidth, double windowHeight) {
        double radius = getRadius(visibleColumns, visibleRows, windowWidth, windowHeight);

        return getCell().getY() * radius * SIDE_LENGTH_OVER_RADIUS;
    }
}
