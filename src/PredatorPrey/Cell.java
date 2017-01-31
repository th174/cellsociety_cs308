package PredatorPrey;

import CellSociety.AbstractCell;
import CellSociety.SimulationGrid;

import java.util.ArrayList;

/**
 * Created by th174 on 1/29/2017.
 */
public class Cell extends AbstractCell {
    private int movesSinceReproduction = 0;
    private final int preyReproductionTime = 5;
    private final int predReproductionTime = 5;

    public Cell(int x, int y, CellSociety.CellState state) {
        super(x, y, state);
    }

    @Override
    //TODO: Implement this;
    /**
     * Every turn of the simulation a fish will move to a random adjacent cell 
     * unless all four are occupied. If the fish has survived the number of turns 
     * necessary to breed it produces a new fish if there is an empty adjacent cell..
     *
     * Each turn if there is a fish adjacent to a shark the shark eats it. If there are 
     * multiple adjacent fish the shark eats one at random. If there are no adjacent fish 
     * the shark moves in the same manner as fish. After eating or moving if the shark has 
     * survived the number of turns necessary to breed it produces a new shark if there is 
     * an empty adjacent cell.
     * @see AbstractCell#interact(CellSociety.SimulationGrid)
     */
    public void interact(SimulationGrid<AbstractCell> grid) {
        ArrayList<AbstractCell> adjNeighbors = new ArrayList<AbstractCell>(getAdjNeighbors().asCollection());
        if (getState().equals(CellState.PREDATOR)) {

            for (AbstractCell neighbor : adjNeighbors) {
                if (neighbor.getState().equals(CellState.PREY)) {

                    neighbor.setState(CellState.PREDATOR); //eat the first fish it sees
                    setState(CellState.EMPTY);
                    break;
                }
            }
            if (canReproduce()) {
                setState(CellState.PREDATOR);
                resetReproduction();
            }
            movesSinceReproduction++;
        }
        if (getState().equals(CellState.PREY)) {
            if (nextStateDead()) {
                return;
            }
            for (AbstractCell neighbor : adjNeighbors) {
                if (neighbor instanceof PredatorPrey.Cell) {
                    if (((PredatorPrey.Cell) neighbor).nextStateEmpty()) {
                        neighbor.setState(CellState.PREY);
                        break;
                    }
                }
            }
            if (!canReproduce()) {
                setState(CellState.EMPTY);
                movesSinceReproduction++;
            }
        }
        return;
    }

    public boolean canReproduce() {
        if (getState().equals(CellState.PREDATOR) && movesSinceReproduction >= predReproductionTime ||
                getState().equals(CellState.PREY) && movesSinceReproduction >= preyReproductionTime) {
            movesSinceReproduction = 0;
            return true;
        }
        return false;
    }

    private void resetReproduction() {
        movesSinceReproduction = 0;
    }

    /*
     * private get state method
     */
    private boolean nextStateEmpty() {
        return getNextState().equals(CellState.EMPTY);
    }

    private boolean nextStateDead() {
        return getNextState().equals(CellState.PREDATOR);
    }

}