package CellSociety.UI;

import CellSociety.Abstract_Cell;
import CellSociety.Grids.SimulationGrid;

public interface InputDataGetter {

    SimulationGrid<? extends Abstract_Cell> getSimulationGrid();

    double getFramesPerSecond();

    String getCellShape();

    String getGridBoundType();

    String getNeighborMode();
}
