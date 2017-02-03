package CellSociety.PredatorPrey;

import CellSociety.Abstract_Cell;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by th174 on 1/29/2017.
 */
public class PredatorPrey_Cell extends Abstract_Cell<PredatorPreyCell_State> {
    private final int preyReproductionTime = 5;
    private final int predReproductionTime = 5;
    private int predMovesSinceEaten;
    private final int daysToStarvation = 5;
    private int movesSinceReproduction = 0;

    public PredatorPrey_Cell(int x, int y, String... params) {
        this(x, y, new PredatorPreyCell_State(params[0]));
    }

    public PredatorPrey_Cell(int x, int y, PredatorPreyCell_State state) {
        super(x, y, state);
        predMovesSinceEaten = 0;
    }

    /**
     * Every turn of the simulation a fish will move to a random adjacent cell
     * unless all four are occupied. If the fish has survived the number of turns
     * necessary to breed it produces a new fish if there is an empty adjacent cell..
     * <p>
     * Each turn if there is a fish adjacent to a shark the shark eats it. If there are
     * multiple adjacent fish the shark eats one at random. If there are no adjacent fish
     * the shark moves in the same manner as fish. After eating or moving if the shark has
     * survived the number of turns necessary to breed it produces a new shark if there is
     * an empty adjacent cell.
     *
     * @see Abstract_Cell#interact()
     */
    @Override
    public void interact() {
        Collection<Abstract_Cell<PredatorPreyCell_State>> adjNeighbors = getAdjNeighbors().asCollection();
        if (getState().equals(PredatorPreyCell_State.PREDATOR)) {
            Optional<PredatorPrey_Cell> potentialPrey = adjNeighbors.stream()
                    .filter(PredatorPrey_Cell.class::isInstance).map(PredatorPrey_Cell.class::cast)
                    .filter(neighbor -> neighbor.getState().equals(PredatorPreyCell_State.PREY)).findAny();
            if (potentialPrey.isPresent()) {
                move(potentialPrey.get(), PredatorPreyCell_State.EMPTY);
            } else {
                predMovesSinceEaten++;
                if (isStarved()) {
                    setState(PredatorPreyCell_State.EMPTY);
                } else {
                    adjNeighbors.stream().filter(neighbor -> neighbor.getState().equals(PredatorPreyCell_State.EMPTY)).findAny().ifPresent(e -> move(e, PredatorPreyCell_State.EMPTY));
                }
            }
            if (canReproduce()) {
                setState(PredatorPreyCell_State.PREDATOR);
                resetReproduction();
            }
        }
        if (getState().equals(PredatorPreyCell_State.PREY) && !nextStateDead()) {
            adjNeighbors.stream().filter(PredatorPrey_Cell.class::isInstance).map(PredatorPrey_Cell.class::cast)
                    .filter(PredatorPrey_Cell::nextStateEmpty)
                    .findAny().ifPresent(e -> e.setState(PredatorPreyCell_State.PREY));
            if (!canReproduce()) {
                setState(PredatorPreyCell_State.EMPTY);
            } else {
                setState(PredatorPreyCell_State.PREY);
                resetReproduction();
            }
            movesSinceReproduction++;
        }
    }

    public boolean canReproduce() {
        if (!getState().equals(PredatorPreyCell_State.PREDATOR) && movesSinceReproduction >= predReproductionTime ||
                getState().equals(PredatorPreyCell_State.PREY) && movesSinceReproduction >= preyReproductionTime) {
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
        return getNextState().equals(PredatorPreyCell_State.EMPTY);
    }

    private boolean nextStateDead() {
        return getNextState().equals(PredatorPreyCell_State.PREDATOR);
    }

    private boolean isStarved() {
        return predMovesSinceEaten >= daysToStarvation;
    }

}