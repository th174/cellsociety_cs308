package CellSociety.SugarScape;

import CellSociety.AbstractDiscrete_CellState;
import CellSociety.Abstract_Cell;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class models a single cell in the SugarScape simulation.
 * <p>
 * Created by th174 on 2/10/2017.
 *
 * @see CellSociety.Abstract_Cell
 */
public class SugarScape_Cell extends Abstract_Cell<SugarScape_Cell, SugarScape_CellState> implements Comparable<SugarScape_Cell> {


    /**
     * Constructs new SugarScape_Cell from XML Properties
     *
     * @param x      x-position
     * @param y      y-position
     * @param params String paramters from xml input
     * @see #SugarScape_Cell(int, int, SugarScape_CellState)
     */
    public SugarScape_Cell(int x, int y, String... params) {
        this(x, y, new SugarScape_CellState(params));
    }

    /**
     * Constructs new SugarScape_Cell with initial CellState
     *
     * @param x     x-position
     * @param y     y-position
     * @param state initial CellState of this Cell
     * @see Abstract_Cell#Abstract_Cell(int, int, AbstractDiscrete_CellState)
     */
    public SugarScape_Cell(int x, int y, SugarScape_CellState state) {
        super(x, y, state);
    }

    /**
     * This SugarScape_Cell interacts with all other cells on its parentGrid in accordance with rules described in the SugarScape Simulation
     *
     * @see Abstract_Cell#interact()
     */
    @Override
    public void interact() {
        if (getCurrentState().equals(SugarScape_CellState.OCCUPIED)) {
            if (getCurrentState().getAgentSugar() <= 0) {
                starve();
            } else {
                List<SugarScape_Cell> neighbors = getNeighbors().stream().filter(e -> !e.getNextState().equals(SugarScape_CellState.OCCUPIED)).collect(Collectors.toList());
                if (neighbors.size() > 0) {
                    Collections.sort(neighbors);
                    SugarScape_Cell mostSugar = neighbors.get(0);
                    if (getCurrentState().canReproduce()) {
                        reproduce(mostSugar);
                    } else {
                        move(mostSugar);
                    }
                }
            }
        }
    }

    /**
     * Compares SugarScape cells by their currentCellState
     *
     * @param cell to be compared with
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(SugarScape_Cell cell) {
        return cell.getCurrentState().compareTo(getCurrentState());
    }

    private void starve() {
        setNextState(new SugarScape_CellState(getCurrentState(), false, 0));
    }

    private void move(SugarScape_Cell target) {
        if (Objects.nonNull(target)) {
            setNextState(new SugarScape_CellState(getCurrentState(), false, 0));
            target.setNextState(new SugarScape_CellState(getCurrentState(), true, target.getCurrentState().getCurrentSugar() + getCurrentState().getAgentSugar()));
        }
    }

    private void reproduce(SugarScape_Cell target) {
        if (Objects.nonNull(target)) {
            setNextState(new SugarScape_CellState(getCurrentState(), true));
            target.setNextState(new SugarScape_CellState(getCurrentState(), true, target.getCurrentState().getCurrentSugar() + getCurrentState().getAgentSugar()));
        }
    }
}
