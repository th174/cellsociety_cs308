package CellSociety.PredatorPrey;

import CellSociety.Abstract_Cell;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by th174 on 1/29/2017.
 */
public class PredatorPrey_Cell extends Abstract_Cell<PredatorPreyCell_State> {
    private final int preyReproductionTime = 5;
    private final int predReproductionTime = 5;
    private final int daysToStarvation = 5;
    private Animal myAnimal;
    private int predMovesSinceEaten;
    private int movesSinceReproduction = 0;
    private Map<PredatorPreyCell_State, Animal> possibleAnimals = new HashMap<PredatorPreyCell_State, Animal>() {{
        put(PredatorPreyCell_State.EMPTY, null);
        put(PredatorPreyCell_State.PREDATOR, new Predator(predReproductionTime, daysToStarvation));
        put(PredatorPreyCell_State.PREDATOR, new Prey(preyReproductionTime));
    }};

    public PredatorPrey_Cell(int x, int y, String... params) {
        this(x, y, new PredatorPreyCell_State(params[0]));
    }

    public PredatorPrey_Cell(int x, int y, PredatorPreyCell_State state) {
        super(x, y, state);
        myAnimal = possibleAnimals.get(state);
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
        if (!getState().equals(PredatorPreyCell_State.EMPTY)) {
            Collection<PredatorPrey_Cell> adjNeighbors = getAdjNeighbors().asCollection().stream().filter(PredatorPrey_Cell.class::isInstance).map(PredatorPrey_Cell.class::cast).collect(Collectors.toSet());
            if (getState().equals(PredatorPreyCell_State.PREDATOR)) {
                Optional<PredatorPrey_Cell> potentialPrey = adjNeighbors.stream()
                        .skip((long) Math.random() * adjNeighbors.size())
                        .filter(neighbor -> neighbor.getState().equals(PredatorPreyCell_State.PREY)).findAny();
                if (potentialPrey.isPresent()) {
                    move(potentialPrey.get(), PredatorPreyCell_State.EMPTY);
                } else {
                    predMovesSinceEaten++;
                    if (isStarved()) {
                        setState(PredatorPreyCell_State.EMPTY);
                    } else {
                        adjNeighbors.stream()
                                .skip((long) Math.random() * adjNeighbors.size())
                                .filter(neighbor -> neighbor.getState().equals(PredatorPreyCell_State.EMPTY))
                                .findAny().ifPresent(e -> move(e, PredatorPreyCell_State.EMPTY));
                    }
                }
                if (myAnimal.canReproduce()) {
                    setState(PredatorPreyCell_State.PREDATOR);
                    resetReproduction();
                }
            }
<<<<<<< HEAD

            if (myAnimal.canReproduce()) {
                setState(PredatorPreyCell_State.PREDATOR);
                resetReproduction();
            }
        }
        if (getState().equals(PredatorPreyCell_State.PREY) && !nextStateDead()) {
            adjNeighbors.stream()
                    .skip((long) Math.random() * adjNeighbors.size())
                    .filter(PredatorPrey_Cell::nextStateEmpty)
                    .findAny().ifPresent(e -> e.setState(PredatorPreyCell_State.PREY));
            if (!canReproduce()) {
                setState(PredatorPreyCell_State.EMPTY);
            } else {
                setState(PredatorPreyCell_State.PREY);
                resetReproduction();
                if (getState().equals(PredatorPreyCell_State.PREY) && !nextStateDead()) {
=======
            if (getState().equals(PredatorPreyCell_State.PREY) && !nextStateDead()) {
>>>>>>> 1e64fb2a9e0fe2671e5f9314b205c7d7599fa9ff
                adjNeighbors.stream()
                        .skip((long) Math.random() * adjNeighbors.size())
                        .filter(PredatorPrey_Cell::nextStateEmpty)
                        .findAny().ifPresent(e -> e.setState(PredatorPreyCell_State.PREY));
                if (!canReproduce()) {
                    setState(PredatorPreyCell_State.EMPTY);
                } else {
                    setState(PredatorPreyCell_State.PREY);
                    resetReproduction();
                }
<<<<<<< HEAD

=======
>>>>>>> 1e64fb2a9e0fe2671e5f9314b205c7d7599fa9ff
            }
            movesSinceReproduction++;
        }
    }

    public boolean canReproduce() {
        if (getState().equals(PredatorPreyCell_State.PREDATOR) && movesSinceReproduction >= predReproductionTime ||
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