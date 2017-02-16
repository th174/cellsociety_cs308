//This entire fire is part of my masterpiece
//Tim Overeem (to41)


/*
 * IMPORTANT NOTE: At one point, Timmy create a duplicate of all files. I'm not sure why; I think maybe he wanted to
 * try to do them without having to push to git/cause errors. The result of this was when he pushed, we had a bunch
 * of duplicate old versions of these files, which we had to delete. This is why the commit history for the files
 * are really messed up and this doesn't show my contributions. This and the Segregation_CellState classes
 * are two classes where I did almost all of the thinking/implementation behind this. I thought this would is 
 * important in case you look at the commit history.
 */
/*
 * I think this class illustrates good design because it is short, readable, good use of a subclass, and the details
 * are encapsulated. It does what it is supposed to do: interact with others based on its/their states, and nothing else.
 * It has one responsibility that it forms and the superclass can count on it to perform. It is very easy to read
 * and understand how it works.
 */

package CellSociety.Segregation;

import CellSociety.AbstractDiscrete_CellState;
import CellSociety.Abstract_Cell;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * This class models a single cell in the Segregation simulation.
 *
 * @see CellSociety.Abstract_Cell
 * Created by th174 on 1/29/2017.
 */
public class Segregation_Cell extends Abstract_Cell<Segregation_Cell, Segregation_CellState> {

    /**
     * Constructs new Segregation_Cell from XML Properties
     *
     * @param x      x-position
     * @param y      y-position
     * @param params String paramters from xml input
     * @see #Segregation_Cell(int, int, Segregation_CellState)
     */
    public Segregation_Cell(int x, int y, String... params) {
        this(x, y, new Segregation_CellState(params));
    }

    /**
     * Constructs new Segreagtion_Cell with initial CellState
     *
     * @param x     x-position
     * @param y     y-position
     * @param state initial CellState of this Cell
     * @see Abstract_Cell#Abstract_Cell(int, int, AbstractDiscrete_CellState)
     */
    public Segregation_Cell(int x, int y, Segregation_CellState state) {
        super(x, y, state);
    }

    /**
     * A given cell will either move to a random cell or stay where it is based on its satisfaction.
     * If the proportion of neighbors with the same state are above or equal to its threshold, it will not move.
     * Otherwise, it will move to a random anywhere cell on the grid this is open next turn.
     * @see CellSociety.Abstract_Cell#interact()
     */
    @Override
    public void interact() {
        if (!getCurrentState().equals(Segregation_CellState.EMPTY)) {
            int sameStateNeighbors = (int) getNeighbors().parallelStream().filter(e -> e.getCurrentState().equals(getCurrentState())).count();
            int totalStateNeighbors = (int) getNeighbors().parallelStream().filter(e -> !e.getCurrentState().equals(Segregation_CellState.EMPTY)).count();
            if (!getCurrentState().isSatisfiedByNeighbors(sameStateNeighbors, totalStateNeighbors)) {
                Collection<Segregation_Cell> emptyCells = getParentGrid().stream().filter(e -> e.getNextState().equals(Segregation_CellState.EMPTY)).collect(Collectors.toSet());
                emptyCells.stream().skip((long) (emptyCells.size() * Math.random())).findAny().ifPresent(this::move);
            }
        }
    }

    private void move(Segregation_Cell cell) {
        cell.setNextState(getCurrentState());
        setNextState(Segregation_CellState.EMPTY);
    }
}