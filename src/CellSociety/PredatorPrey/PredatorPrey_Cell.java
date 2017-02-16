package CellSociety.PredatorPrey;

import CellSociety.AbstractDiscrete_CellState;
import CellSociety.Abstract_Cell;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class models a single cell in the PredatorPrey simulation.
 * <p>
 * Created by th174 on 1/29/2017.
 *
 * @see CellSociety.Abstract_Cell
 */
public class PredatorPrey_Cell extends Abstract_Cell<PredatorPrey_Cell, PredatorPrey_CellState> {
    /**
     * Constructs new PredatorPrey_Cell from XML Properties
     *
     * @param x      x-position
     * @param y      y-position
     * @param params String paramters from xml input
     * @see #PredatorPrey_Cell(int, int, PredatorPrey_CellState)
     */
    public PredatorPrey_Cell(int x, int y, String... params) {
        this(x, y, new PredatorPrey_CellState(params));
    }

    /**
     * Constructs new PredatorPrey_Cell with initial CellState
     *
     * @param x     x-position
     * @param y     y-position
     * @param state initial CellState of this Cell
     * @see Abstract_Cell#Abstract_Cell(int, int, AbstractDiscrete_CellState)
     */
    public PredatorPrey_Cell(int x, int y, PredatorPrey_CellState state) {
        super(x, y, state);
    }

    /**
     * Every turn of the simulation a fish will move to a random adjacent cell
     * unless all four are occupied. If the fish has survived the number of turns
     * necessary to breed it produces a new fish if there is an empty adjacent cell..
     * <p>
     * Each turn if there is a fish adjacent to a shark the shark eats it. If there are
     * multiple adjacent fish the shark eats one at random. If there are no adjacent fish
     * the shark moves in a random direction. After eating or moving if the shark has
     * survived the number of turns necessary to breed it produces a new shark if there is
     * an empty adjacent cell.
     *
     * @see Abstract_Cell#interact()
     */
    @Override
    public void interact() {
        if (getCurrentIndex() % 2 != 0 && getCurrentState().equals(PredatorPrey_CellState.PREY) && !reproduced(getEmptyNeighbor())) {
            move(getEmptyNeighbor());
        } else if (getCurrentIndex() % 2 == 0 && getCurrentState().equals(PredatorPrey_CellState.PREDATOR)) {
            if (getCurrentState().willStarve()) {
                setNextState(PredatorPrey_CellState.EMPTY);
            } else {
                if (!predatorEat(getPreyNeighbor())) {
                    move(getEmptyNeighbor());
                }
                reproduced(getEmptyNeighbor());

            }
        }
    }

    private PredatorPrey_Cell getEmptyNeighbor() {
        Collection<PredatorPrey_Cell> neighbors = getNeighbors().stream().filter(e -> e.getNextState().equals(PredatorPrey_CellState.EMPTY)).collect(Collectors.toSet());
        return neighbors.stream().skip((long) (Math.random() * neighbors.size())).findAny().orElse(null);
    }

    private PredatorPrey_Cell getPreyNeighbor() {
        Collection<PredatorPrey_Cell> potentialFood = getNeighbors().stream().filter(e -> e.getCurrentState().equals(PredatorPrey_CellState.PREY)).collect(Collectors.toSet());
        return potentialFood.stream().skip((long) (Math.random() * potentialFood.size())).findAny().orElse(null);
    }

    private boolean predatorEat(PredatorPrey_Cell cell) {
        if (Objects.nonNull(cell)) {
            cell.setNextState(PredatorPrey_CellState.EMPTY);
            setNextState(new PredatorPrey_CellState(getCurrentState(), -1, getCurrentState().getMaxStarvationTimer()));
            return true;
        }
        return false;
    }

    private void move(PredatorPrey_Cell cell) {
        if (Objects.nonNull(cell)) {
            cell.setNextState(getCurrentState().getSuccessorState());
            setNextState(PredatorPrey_CellState.EMPTY);
        }
    }

    private boolean reproduced(PredatorPrey_Cell cell) {
        if (getCurrentState().canReproduce()) {
            if (Objects.nonNull(cell)) {
                cell.setNextState(new PredatorPrey_CellState(getCurrentState(), getCurrentState().getMaxReproductionTimer(), -1));
                setNextState(new PredatorPrey_CellState(getCurrentState(), getCurrentState().getMaxReproductionTimer(), getCurrentState().getMaxStarvationTimer()));
            }
            return true;
        }
        return false;
    }
}