package CellSociety.UI;

import CellSociety.Abstract_Cell;
import CellSociety.Abstract_CellState;
import CellSociety.Grids.SimulationGrid;
import javafx.scene.paint.Color;

public interface InputDataGetter {

    SimulationGrid<? extends Abstract_Cell, ? extends Abstract_CellState> getSimulationGrid();

    double getFramesPerSecond();

    String getCellShape();

    String getGridBoundType();

    String getNeighborMode();

    String getGridOutline();
}
