package CellSociety.SlimeMold;

import java.util.stream.Collectors;

import CellSociety.Abstract_Cell;
import CellSociety.Grids.SimulationGrid;
import CellSociety.SlimeMold.SlimeMold_CellState.SlimeMoldState;


public class SlimeMold_Cell extends Abstract_Cell<SlimeMold_Cell, SlimeMold_CellState> implements Comparable<SlimeMold_Cell>{

    public SlimeMold_Cell(int x, int y, SlimeMoldState state) {
        super(x, y, new SlimeMold_CellState(state));
    }
    public SlimeMold_Cell(int x, int y, String...params) {
        super(x, y, new SlimeMold_CellState(params));
    }

    @Override
    public void interact() {
        SimulationGrid<SlimeMold_Cell, SlimeMold_CellState> neighborsGrid = getNeighbors();
        if (getCurrentState().hasTurtle()) {
        	for(Turtle turtle: getCurrentState().getTurtles()){
        		turtle.depositChemical(this);
                if(turtle.followGradient(getCurrentState())) turtle.moveToCell(neighborsGrid);
                else turtle.moveSameDirection(neighborsGrid);
        	}
        }

        diffuseAndEvaporate(neighborsGrid);

    }

    public void diffuseAndEvaporate(SimulationGrid<SlimeMold_Cell, SlimeMold_CellState> neighborsGrid) {
        getCurrentState().evaporate();
        int numberNeighbors = (int) neighborsGrid.stream().filter(e -> e != null).count();
        
        double diffusionAmount = getCurrentState().getDiffusionAmount();
        double diffusionPerNeighbor = diffusionAmount / numberNeighbors;
        for(SlimeMold_Cell cell : neighborsGrid.stream().collect(Collectors.toSet())){
        	SlimeMold_Cell nextCell = neighborsGrid.iterator().next();
            if (nextCell != null) {
                nextCell.getCurrentState().addNextChemical(diffusionPerNeighbor);
            }
        }


    }

	@Override
	public int compareTo(SlimeMold_Cell o) {
		return (int) (getCurrentState().getChemical()-o.getCurrentState().getChemical());
	}


}
