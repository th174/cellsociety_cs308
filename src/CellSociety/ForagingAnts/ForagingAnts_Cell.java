package CellSociety.ForagingAnts;

import CellSociety.Abstract_Cell;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;



public class ForagingAnts_Cell extends Abstract_Cell<ForagingAnts_Cell, ForagingAnts_CellState> {

    protected ForagingAnts_Cell(int x, int y, ForagingAnts_CellState state) {
        super(x, y, state);
    }

    public void generateAnts() {

    }

    @Override
    public void interact() {
        if (!getCurrentState().equals(ForagingAnts_CellState.OBSTACLE)) {
            Collection<ForagingAnts_Cell> neighbors = getNeighbors().stream().collect(Collectors.toSet());
            for (Ant a : getCurrentState().getAnts()) {
                if (a.hasFood()) returnToNest(neighbors, a);
                else findFoodSource(neighbors, a);
            }
        }

    }

    private void returnToNest(Collection<ForagingAnts_Cell> neighbors, Ant a) {
        //TODO: look at top 3

        List<ForagingAnts_Cell> possibleNeighbors = neighbors.stream().filter(e ->
                e.getCurrentState().canMoveToCell()).sorted((c, d) ->
                c.getCurrentState().compareHomeTo(d.getCurrentState())).collect(Collectors.toList());//might want to refactor out
        if (!possibleNeighbors.isEmpty()) {
            dropFoodPheromones(neighbors);
            //TODO: set orientation
            possibleNeighbors.get(0).getNextState().addAnt(a);
            getNextState().removeAnt(a);
            if (possibleNeighbors.get(0).getCurrentState().equals(ForagingAnts_CellState.SOURCE)) {
                a.dropFood();
            }
        }

    }

    private void findFoodSource(Collection<ForagingAnts_Cell> neighbors, Ant a) {
        // TODO: find top 3 if any are options

        //now look at all neighbors sorted by being able to go to them and by their # of pheromones
        List<ForagingAnts_Cell> possibleNeighbors = neighbors.stream().filter(e ->
                e.getCurrentState().canMoveToCell()).sorted((c, d) ->
                c.getCurrentState().compareFoodTo(d.getCurrentState())).collect(Collectors.toList());//might want to refactor out
        if (!possibleNeighbors.isEmpty()) {
            dropHomePheromones(neighbors);
            //TODO: set oriention
            possibleNeighbors.get(0).getNextState().addAnt(a);
            getNextState().removeAnt(a);
            if (possibleNeighbors.get(0).getCurrentState().equals(ForagingAnts_CellState.SOURCE)) {
                a.pickUpFood();
            }
        }
    }

    private void dropHomePheromones(Collection<ForagingAnts_Cell> neighbors) {
        if (getCurrentState().equals(ForagingAnts_CellState.SOURCE)) getCurrentState().setHomePheromoneToMax();

        else if (getCurrentState().canDropHomePheromone()) {
            int max = neighbors.stream().mapToInt(e -> e.getCurrentState().getHomePheromone()).sum();
            int desired = max - getCurrentState().getPheromoneConstant();
            int d = desired - getCurrentState().getHomePheromone();
            if (d > 0) getNextState().addHomePheromone(d);
        }
    }

    private void dropFoodPheromones(Collection<ForagingAnts_Cell> neighbors) {
        if (getCurrentState().equals(ForagingAnts_CellState.SOURCE)) getCurrentState().setFoodPheromoneToMax();
        else if (getCurrentState().canDropFoodPheromone()) {

        }

    }

}
