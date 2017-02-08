package CellSociety.UI.CellView;

import CellSociety.Abstract_Cell;
import CellSociety.Abstract_CellState;

/**
 * Created by th174 on 2/8/2017.
 */
public class Triangle_CellView extends AbstractRegularPolygon_CellView {
    public static final int TRIANGLE_SIDES = 3;
    public static final double UPSIDE_DOWN = Math.toRadians(180);
    public static final double POINTING_UP = Math.toRadians(90);
    public static final double SIDE_LENGTH_OVER_RADIUS = Math.sqrt(3);


    public Triangle_CellView(Abstract_Cell<Abstract_CellState> cell) {
        super(cell);
        setNumSides(TRIANGLE_SIDES);
        setRotation(POINTING_UP + ((getCell().getX() + getCell().getY()) % 2 == 0 ? UPSIDE_DOWN : 0));
    }

    @Override
    public void updateView(int columns, int rows, double windowWidth, double windowHeight) {
        setRadius(windowWidth / columns / 1.5);
        super.updateView(columns, rows, windowWidth, windowHeight);
    }

    @Override
    protected double calculateX() {
        return getCell().getX() * getRadius() * SIDE_LENGTH_OVER_RADIUS / 2;
    }

    @Override
    protected double calculateY() {
        return getCell().getY() * getRadius() * 1.5;
    }
}
