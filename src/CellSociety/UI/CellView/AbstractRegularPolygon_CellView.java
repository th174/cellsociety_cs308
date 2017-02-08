package CellSociety.UI.CellView;

import CellSociety.Abstract_Cell;
import CellSociety.Abstract_CellState;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * Created by th174 on 2/8/2017.
 */
public abstract class AbstractRegularPolygon_CellView extends Abstract_CellView {
    private static final double FULL_CIRCLE = Math.PI * 2;
    private double radius;
    private int numSides;
    private double rotationAngle;

    public AbstractRegularPolygon_CellView(Abstract_Cell<Abstract_CellState> cell) {
        super(cell);
        setShape(new Polygon());
        getView().setStroke(Color.BLACK);

    }

    @Override
    public void updateView(int columns, int rows, double windowWidth, double windowHeight) {
        super.updateView(columns, rows, windowWidth, windowHeight);
        getView().getPoints().setAll(getRegularPolygonCoordinates());
        getView().relocate(calculateX(), calculateY());
    }

    protected abstract double calculateX();

    protected abstract double calculateY();

    private Double[] getRegularPolygonCoordinates() {
        Double[] points = new Double[numSides * 2];
        for (int i = 0; i < numSides; i++) {
            double currentVertexAngle = i * FULL_CIRCLE / numSides;
            points[i * 2] = Math.cos(currentVertexAngle + rotationAngle) * radius;
            points[i * 2 + 1] = Math.sin(currentVertexAngle + rotationAngle) * radius;
        }
        return points;
    }

    public Polygon getView() {
        return (Polygon) super.getView();
    }

    protected void setRadius(double length) {
        radius = length;
    }

    protected double getRadius() {
        return radius;
    }

    protected void setNumSides(int sides) {
        numSides = sides;
    }

    protected void setRotation(double degrees) {
        rotationAngle = degrees;
    }
}
