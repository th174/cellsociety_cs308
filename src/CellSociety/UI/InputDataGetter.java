package CellSociety.UI;

import CellSociety.Abstract_Cell;
import CellSociety.Abstract_CellState;
import CellSociety.Grids.SimulationGrid;

public interface InputDataGetter {

    /**
     * @return simulationGrid of the animation
     */
    SimulationGrid<? extends Abstract_Cell, ? extends Abstract_CellState> getSimulationGrid();

    /**
     * @return simulationType of the animation
     */
    String getSimulationType();

    /**
     * @return framesPerSecond, i.e. speed of animation
     */
    double getFramesPerSecond();

    /**
     * @return shape of cells in the animatino
     */
    String getCellShape();

    /**
     * @return bound type of the grid
     */
    String getGridBoundType();

    /**
     * @return neighbormode, i.e. which neighbors are being considered
     */
    String getNeighborMode();

    /**
     * @return outline of the grid
     */
    String getGridOutline();
}
