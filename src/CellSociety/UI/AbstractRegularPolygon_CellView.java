package CellSociety.UI;

import CellSociety.Abstract_Cell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * Created by th174 on 2/8/2017.
 */
public abstract class AbstractRegularPolygon_CellView<E extends Abstract_Cell> extends Abstract_CellView<E> {
    public static final double FULL_CIRCLE = Math.PI * 2;

    public AbstractRegularPolygon_CellView(E cell, String outlineColor) {
        super(cell);
        setShape(new Polygon());
        if (outlineColor.length() > 0) {
            getView().setStroke(Color.valueOf(outlineColor.toLowerCase()));
        }
    }

    @Override
    public void updateView(double visibleColumns, double visibleRows, double windowWidth, double windowHeight) {
        super.updateView(visibleColumns, visibleRows, windowWidth, windowHeight);
        getView().getPoints().setAll(getRegularPolygonCoordinates(getRadius(visibleColumns, visibleRows, windowWidth, windowHeight)));
        getView().relocate(calculateX(visibleColumns, visibleRows, windowWidth, windowHeight), calculateY(visibleColumns, visibleRows, windowWidth, windowHeight));
    }

    protected abstract int getNumSides();

    protected abstract double getRotationAngle();

    protected abstract double getRadius(double visibleColumns, double visibleRows, double windowWidth, double windowHeight);

    protected abstract double calculateX(double visibleColumns, double visibleRows, double windowWidth, double windowHeight);

    protected abstract double calculateY(double visibleColumns, double visibleRows, double windowWidth, double windowHeight);

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
}
