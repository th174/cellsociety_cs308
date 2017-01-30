package Fire;

import CellSociety.SimulationGrid;

/**
 * Created by th174 on 1/29/2017.
 */
public class Cell extends CellSociety.Cell {
    private double probCatchFire;

    public Cell(int x, int y, CellSociety.CellState state, String probCatchString) {
        super(x, y, state);
        probCatchFire = Double.parseDouble(probCatchString);
    }

    @Override
    //TODO: Implement this; 
    public void interact(SimulationGrid<CellSociety.Cell> grid) {
        return;
    }
}
