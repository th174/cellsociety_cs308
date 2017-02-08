package CellSociety.UI.CellView;

import CellSociety.Abstract_Cell;
import CellSociety.Abstract_CellState;

/**
 * Created by th174 on 2/7/2017.
 */
public class Hexagon_CellView extends AbstractRegularPolygon_CellView {
    public static final int HEXAGON_SIDES = 6;
    public static final double HEIGHT_OVER_RADIUS = Math.sqrt(3);

    public Hexagon_CellView(Abstract_Cell<Abstract_CellState> cell) {
        super(cell);
        setNumSides(HEXAGON_SIDES);
    }

    @Override
    public void updateView(int columns, int rows, double windowWidth, double windowHeight) {
        setRadius(windowHeight / (rows + .5) / HEIGHT_OVER_RADIUS);
        super.updateView(columns, rows, windowWidth, windowHeight);
    }

    @Override
    protected double calculateX() {
        return getCell().getX() * getRadius() * 1.5;
    }

    @Override
    protected double calculateY() {
        return getRadius() * HEIGHT_OVER_RADIUS * getCell().getY() + (getCell().getX() % 2 == 1 ? getRadius() * HEIGHT_OVER_RADIUS / 2 : 0);
    }
}
