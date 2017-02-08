package CellSociety.UI.CellView;

import CellSociety.Abstract_Cell;
import CellSociety.Abstract_CellState;
import javafx.scene.shape.Shape;


/**
 * Created by th174 on 2/7/2017.
 */
public abstract class Abstract_CellView {
    private Abstract_Cell<Abstract_CellState> myCell;
    private Shape myView;

    public Abstract_CellView(Abstract_Cell<Abstract_CellState> cell) {
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

    public Abstract_Cell<Abstract_CellState> getCell() {
        return myCell;
    }
}
