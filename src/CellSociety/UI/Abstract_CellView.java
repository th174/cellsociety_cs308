package CellSociety.UI;

import CellSociety.Abstract_Cell;
import javafx.scene.shape.Shape;


/**
 * Created by th174 on 2/7/2017.
 */
public abstract class Abstract_CellView<E extends Abstract_Cell> {
    private E myCell;
    private Shape myView;

    public Abstract_CellView(E cell) {
        myCell = cell;
    }

    public void updateView(int columns, int rows, double windowWidth, double windowHeight) {
        myView.setFill(myCell.getCurrentState().getFill());
    }

    protected void setShape(Shape view) {
        myView = view;
    }

    public Shape getView() {
        return myView;
    }

    public E getCell() {
        return myCell;
    }
}
