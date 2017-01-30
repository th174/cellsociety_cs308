package Segregation;

import CellSociety.SimulationGrid;

/**
 * Created by th174 on 1/29/2017.
 */
public class Cell extends CellSociety.Cell {

    public Cell(int x, int y, CellSociety.CellState state) {
        super(x, y, state);
    }

    @Override
    //TODO: Implement this;
    public void interact(SimulationGrid<CellSociety.Cell> grid) {
        return;
    }
}