package CellSociety.UI;

import CellSociety.Abstract_Cell;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Created by th174 on 2/8/2017.
 */
public abstract class AbstractRegularPolygon_CellView<E extends Abstract_Cell> extends Abstract_CellView<E> {
    public static final double FULL_CIRCLE = Math.PI * 2;
    Text content;


    /**
     * Constructs abstract cell view of a regular polygon (e.g. triangle)
     * @param cell
     * @param outlineColor
     */
    public AbstractRegularPolygon_CellView(E cell, String outlineColor) {
        super(cell);
        setShape(new Polygon());
        if (outlineColor.length() > 0) {
            getView().setStroke(Color.valueOf(outlineColor.toLowerCase()));
        }
        content = new Text("");
        content.setFill(Color.BLACK);
        content.setFont(new Font(30));
    }

    /** 
     * Updates view of the abstract cell according to given params
     * @param visibleColumns
     * @param visibleRows
     * @param windowWidth
     * @param windowHeight
     * @see CellSociety.UI.Abstract_CellView#updateView(double, double, double, double)
     */
    @Override
    public void updateView(double visibleColumns, double visibleRows, double windowWidth, double windowHeight) {
        super.updateView(visibleColumns, visibleRows, windowWidth, windowHeight);
        getView().getPoints().setAll(getRegularPolygonCoordinates(getRadius(visibleColumns, visibleRows, windowWidth, windowHeight)));
        getView().relocate(calculateX(visibleColumns, visibleRows, windowWidth, windowHeight), calculateY(visibleColumns, visibleRows, windowWidth, windowHeight));
        content.setX((getView().getBoundsInParent().getMinX() + getView().getBoundsInParent().getMaxX()) / 2 - 10);
        content.setY((getView().getBoundsInParent().getMinY() + getView().getBoundsInParent().getMaxY()) / 2 - 10);
        int agents = getCell().getCurrentState().getNumOfAgents();
        String text = "";
        for (int i = 1; i <= agents; i++) {
            text += ".";
        }
        content.setText(text);
    }

    /** 
     * gets the content of this cellview object.
     * @see CellSociety.UI.Abstract_CellView#getContent()
     */
    public Node getContent() {
        return content;
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

    /* (non-Javadoc)
     * @see CellSociety.UI.Abstract_CellView#getView()
     */
    @Override
    public Polygon getView() {
        return (Polygon) super.getView();
    }
}
