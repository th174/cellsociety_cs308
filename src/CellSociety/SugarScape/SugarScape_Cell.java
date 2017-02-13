package CellSociety.SugarScape;

import CellSociety.Abstract_Cell;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SugarScape_Cell extends Abstract_Cell<SugarScape_Cell, SugarScape_CellState> implements Comparable<SugarScape_Cell> {

    private SugarScape_Cell(int x, int y, SugarScape_CellState state) {
        super(x, y, state);
    }

    /**
     * Constructor from xml
     *
     * @param x      xPos
     * @param y      yPos
     * @param params xml properties
     */
    public SugarScape_Cell(int x, int y, String... params) {
        this(x, y, new SugarScape_CellState(params));
    }

    /**
     * SugarScape
     */
    @Override
    public void interact() {
        if (getCurrentState().hasAgent()) {
            if (getCurrentState().getAgentSugar() <= 0) {
                starve();
            } else {
                List<SugarScape_Cell> neighbors = getNeighbors().stream().filter(e -> !e.getNextState().hasAgent()).collect(Collectors.toList());
                if (neighbors.size() > 0) {
                    Collections.shuffle(neighbors);
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
            target.setNextState(new SugarScape_CellState(getCurrentState(), true, target.getCurrentState().getSugar() + getCurrentState().getAgentSugar()));
        }
    }

    private void reproduce(SugarScape_Cell target) {
        if (Objects.nonNull(target)) {
            setNextState(new SugarScape_CellState(getCurrentState(), true));
            target.setNextState(new SugarScape_CellState(getCurrentState(), true, target.getCurrentState().getSugar() + getCurrentState().getAgentSugar()));
        }
    }
}
