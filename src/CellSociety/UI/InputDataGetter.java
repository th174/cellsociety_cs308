package CellSociety.UI;

import CellSociety.Abstract_Cell;
import CellSociety.Abstract_CellState;
import CellSociety.Grids.SimulationGrid;

public interface InputDataGetter {

    /**
     * @return SimulationGrid to run the simulation
     */
    SimulationGrid<? extends Abstract_Cell, ? extends Abstract_CellState> getSimulationGrid();

    /**
     * @return Current Type of Simulation
     */
    String getSimulationType();

    /**
     * @return Number of times to update simulation per second
     */
    double getFramesPerSecond();

    /**
     * @return Shape of cells. Currently supports Hexagon, Triangle, and Square
     */
    String getCellShape();

    /**
     * @return Type of grid bounds. Currently supports Finite and Wrapped
     */
    String getGridBoundType();

    /**
     * @return Type of neighbors. Currently supports All, Corners (only), and Adjacent (only)
     */
    String getNeighborMode();

    /**
     * @return Color of grid outline
     */
    String getGridOutline();
}
