package CellSociety.UI;

import CellSociety.Abstract_Cell;
import CellSociety.Abstract_CellState;
import CellSociety.Grids.SimulationGrid;

public interface InputDataGetter {

    SimulationGrid<? extends Abstract_Cell, ? extends Abstract_CellState> getSimulationGrid();

    String getSimulationType();

    double getFramesPerSecond();

    String getCellShape();

    String getGridBoundType();

    String getNeighborMode();

    String getGridOutline();
}
