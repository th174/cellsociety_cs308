package CellSociety.UI;

import CellSociety.Abstract_Cell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * Base class providing a resizable graphical representation of an Cell as a regular polygon.
 *
 * @param <E> Type of Abstract_Cell represented by this CellView
 *            Created by th174 on 2/8/2017.
 * @see Abstract_CellView
 */
public abstract class AbstractRegularPolygon_CellView<E extends Abstract_Cell> extends Abstract_CellView<E> {
    public static final double FULL_CIRCLE = Math.PI * 2;

    /**
     * Constructs regular polygonal cell view of a regular polygon (e.g. triangle)
     *
     * @param cell Abstract_Cell that this CellView represents
     * @param outlineColor String representing the color of the grid outline of this CellView. The format of the string representation is the same as in web(String)
     */
    public AbstractRegularPolygon_CellView(E cell, String outlineColor) {
        super(cell);
        setView(new Polygon());
        if (outlineColor.length() > 0) {
            getView().setStroke(Color.valueOf(outlineColor.toLowerCase()));
        }
    }

    /**
     * @param visibleColumns Number of visible columns in the simulation window
     * @param visibleRows    Number of visible rows in the simulation window
     * @param windowWidth    Width of window in pixels
     * @param windowHeight   Height of window in pixels
     * @see CellSociety.UI.Abstract_CellView#updateView(double, double, double, double)
     */
    @Override
    public void updateView(double visibleColumns, double visibleRows, double windowWidth, double windowHeight) {
        super.updateView(visibleColumns, visibleRows, windowWidth, windowHeight);
        getView().getPoints().setAll(getRegularPolygonCoordinates(getRadius(visibleColumns, visibleRows, windowWidth, windowHeight)));
        getView().relocate(calculateX(visibleColumns, visibleRows, windowWidth, windowHeight), calculateY(visibleColumns, visibleRows, windowWidth, windowHeight));
    }

    /**
     * @return Number of sides of the regular polygon
     */
    protected abstract int getNumSides();

    /**
     * @return Angle in radians by which the regular polygon should be rotated.
     */
    protected abstract double getRotationAngle();

    /**
     * @param visibleColumns Number of visible columns in the simulation window
     * @param visibleRows    Number of visible rows in the simulation window
     * @param windowWidth    Width of window in pixels
     * @param windowHeight   Height of window in pixels
     * @return Distance from the center of the regular polygon to one of its vertices
     */
    protected abstract double getRadius(double visibleColumns, double visibleRows, double windowWidth, double windowHeight);

    /**
     * @param visibleColumns Number of visible columns in the simulation window
     * @param visibleRows    Number of visible rows in the simulation window
     * @param windowWidth    Width of window in pixels
     * @param windowHeight   Height of window in pixels
     * @return x-position in window of the left bound of the regular polygon in pixels
     */
    protected abstract double calculateX(double visibleColumns, double visibleRows, double windowWidth, double windowHeight);

    /**
     * @param visibleColumns Number of visible columns in the simulation window
     * @param visibleRows    Number of visible rows in the simulation window
     * @param windowWidth    Width of window in pixels
     * @param windowHeight   Height of window in pixels
     * @return y-position in window of the left bound of the regular polygon in pixels
     */
    protected abstract double calculateY(double visibleColumns, double visibleRows, double windowWidth, double windowHeight);

    /**
     * @param radius Distance from the center of the regular polygon to one of its vertices
     * @return Array of doubles representing the vertices of the regular polygon
     */
    private Double[] getRegularPolygonCoordinates(double radius) {
        Double[] points = new Double[getNumSides() * 2];
        for (int i = 0; i < getNumSides(); i++) {
            double currentVertexAngle = i * FULL_CIRCLE / getNumSides();
            points[i * 2] = Math.cos(currentVertexAngle + getRotationAngle()) * radius;
            points[i * 2 + 1] = Math.sin(currentVertexAngle + getRotationAngle()) * radius;
        }
        return points;
    }

    /**
     * @see CellSociety.UI.Abstract_CellView#getView()
     */
    @Override
    public Polygon getView() {
        return (Polygon) super.getView();
    }
}
