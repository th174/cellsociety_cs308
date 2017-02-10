package CellSociety.UI;

import CellSociety.Abstract_Cell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * Created by th174 on 2/8/2017.
 */
public abstract class AbstractRegularPolygon_CellView<E extends Abstract_Cell> extends Abstract_CellView<E> {
    public static final double FULL_CIRCLE = Math.PI * 2;

    public AbstractRegularPolygon_CellView(E cell) {
        super(cell);
        setShape(new Polygon());
        getView().setStroke(Color.BLACK);
    }

    @Override
    public void updateView(int columns, int rows, double windowWidth, double windowHeight) {
        super.updateView(columns, rows, windowWidth, windowHeight);
        getView().getPoints().setAll(getRegularPolygonCoordinates(getRadius(columns, rows, windowWidth, windowHeight)));
        getView().relocate(calculateX(columns, rows, windowWidth, windowHeight), calculateY(columns, rows, windowWidth, windowHeight));
    }

    protected abstract int getNumSides();

    protected abstract double getRotationAngle();

    protected abstract double getRadius(int columns, int rows, double windowWidth, double windowHeight);

    protected abstract double calculateX(int columns, int rows, double windowWidth, double windowHeight);

    protected abstract double calculateY(int columns, int rows, double windowWidth, double windowHeight);

    private Double[] getRegularPolygonCoordinates(double radius) {
        Double[] points = new Double[getNumSides() * 2];
        for (int i = 0; i < getNumSides(); i++) {
            double currentVertexAngle = i * FULL_CIRCLE / getNumSides();
            points[i * 2] = Math.cos(currentVertexAngle + getRotationAngle()) * radius;
            points[i * 2 + 1] = Math.sin(currentVertexAngle + getRotationAngle()) * radius;
        }
        return points;
    }

    @Override
    public Polygon getView() {
        return (Polygon) super.getView();
    }

    /**
     * Created by th174 on 2/7/2017.
     */
    private static final class Hexagon<E extends Abstract_Cell> extends AbstractRegularPolygon_CellView<E> {
        public static final int HEXAGON_SIDES = 6;
        private static final double LENGTH_OVER_RADIUS = Math.sqrt(3);
        private static final double THIRTY_DEGREES = Math.toRadians(30);
        private static final double HEIGHT_OVER_RADIUS = 1.5;

        public Hexagon(E cell) {
            super(cell);
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
        protected double getRadius(int rows, int columns, double windowWidth, double windowHeight) {
            return windowWidth / (columns + .5) / LENGTH_OVER_RADIUS;
        }

        @Override
        protected double calculateX(int columns, int rows, double windowWidth, double windowHeight) {
            double radius = getRadius(columns, rows, windowWidth, windowHeight);
            return radius * LENGTH_OVER_RADIUS * getCell().getX() + (getCell().getY() % 2 == 1 ? radius * LENGTH_OVER_RADIUS / 2 : 0);
        }

        @Override
        protected double calculateY(int columns, int rows, double windowWidth, double windowHeight) {
            double radius = getRadius(columns, rows, windowWidth, windowHeight);
            return getCell().getY() * radius * HEIGHT_OVER_RADIUS;
        }
    }

    /**
     * Created by th174 on 2/4/2017.
     */
    private static final class Square<E extends Abstract_Cell> extends AbstractRegularPolygon_CellView<E> {
        public static final int SQUARE_SIDES = 4;
        private static final double SIDE_LENGTH_OVER_RADIUS = 2 / Math.sqrt(2);
        private static final double QUARTER_SPIN = Math.toRadians(45);

        public Square(E cell) {
            super(cell);
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
        protected double getRadius(int columns, int rows, double windowWidth, double windowHeight) {
            return windowWidth / columns / SIDE_LENGTH_OVER_RADIUS;
        }

        @Override
        protected double calculateX(int columns, int rows, double windowWidth, double windowHeight) {
            double radius = getRadius(columns, rows, windowWidth, windowHeight);
            return getCell().getX() * radius * SIDE_LENGTH_OVER_RADIUS;
        }

        @Override
        protected double calculateY(int columns, int rows, double windowWidth, double windowHeight) {
            double radius = getRadius(columns, rows, windowWidth, windowHeight);

            return getCell().getY() * radius * SIDE_LENGTH_OVER_RADIUS;
        }
    }

    /**
     * Created by th174 on 2/8/2017.
     */
    private static final class Triangle<E extends Abstract_Cell> extends AbstractRegularPolygon_CellView<E> {
        public static final int TRIANGLE_SIDES = 3;
        private static final double UPSIDE_DOWN = Math.toRadians(180);
        private static final double SIDE_LENGTH_OVER_RADIUS = Math.sqrt(3);
        private static final double WIDTH_OVER_RADIUS = 1.5;

        public Triangle(E cell) {
            super(cell);
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
        protected double getRadius(int columns, int rows, double windowWidth, double windowHeight) {
            return windowWidth / columns / WIDTH_OVER_RADIUS;
        }

        @Override
        protected double calculateX(int columns, int rows, double windowWidth, double windowHeight) {
            double radius = getRadius(columns, rows, windowWidth, windowHeight);
            return getCell().getX() * radius * WIDTH_OVER_RADIUS;
        }

        @Override
        protected double calculateY(int columns, int rows, double windowWidth, double windowHeight) {
            double radius = getRadius(columns, rows, windowWidth, windowHeight);
            return getCell().getY() * radius * SIDE_LENGTH_OVER_RADIUS / 2;
        }
    }
}
