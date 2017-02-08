package CellSociety.UI.CellView;

import CellSociety.Abstract_Cell;
import CellSociety.Abstract_CellState;

/**
 * Created by th174 on 2/4/2017.
 */
public class Square_CellView extends AbstractRegularPolygon_CellView {
    public static int SQUARE_SIDES = 4;
    public static double SIDE_LENGTH_OVER_RADIUS = 2 / Math.sqrt(2);
    public static double QUARTER_SPIN = Math.toRadians(45);

    public Square_CellView(Abstract_Cell<Abstract_CellState> cell) {
        super(cell);
        setNumSides(SQUARE_SIDES);
        setRotation(QUARTER_SPIN);
    }

    public void updateView(int columns, int rows, double windowWidth, double windowHeight) {
        setRadius(windowWidth / columns / SIDE_LENGTH_OVER_RADIUS);
        super.updateView(columns, rows, windowWidth, windowHeight);
    }

    @Override
    protected double calculateX() {
        return getCell().getX() * getRadius() *  SIDE_LENGTH_OVER_RADIUS;
    }

    @Override
    protected double calculateY() {
        return getCell().getY() * getRadius() * SIDE_LENGTH_OVER_RADIUS;
    }
}
