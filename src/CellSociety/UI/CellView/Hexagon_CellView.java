package CellSociety.UI.CellView;

import CellSociety.Abstract_Cell;
import CellSociety.Abstract_CellState;

/**
 * Created by th174 on 2/7/2017.
 */
public class Hexagon_CellView extends AbstractRegularPolygon_CellView {
    public static final int HEXAGON_SIDES = 6;
    public static final double LENGTH_OVER_RADIUS = Math.sqrt(3);
    public static final double THIRTY_DEGREES = Math.toRadians(30);

    public Hexagon_CellView(Abstract_Cell<Abstract_CellState> cell) {
        super(cell);
        setNumSides(HEXAGON_SIDES);
        setRotation(THIRTY_DEGREES);
    }

    @Override
    public void updateView(int columns, int rows, double windowWidth, double windowHeight) {
        setRadius(windowWidth / (columns + .5) / LENGTH_OVER_RADIUS);
        super.updateView(columns, rows, windowWidth, windowHeight);
    }

    @Override
    protected double calculateX() {
        return getRadius() * LENGTH_OVER_RADIUS * getCell().getX() + (getCell().getY() % 2 == 1 ? getRadius() * LENGTH_OVER_RADIUS / 2 : 0);
    }

    @Override
    protected double calculateY() {
        return getCell().getY() * getRadius() * 1.5;
    }
}
