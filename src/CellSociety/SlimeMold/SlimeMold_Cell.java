//package CellSociety.SlimeMold;
//
//import CellSociety.Abstract_Cell;
//import CellSociety.Grids.SimulationGrid;
//
//
//public class SlimeMold_Cell extends Abstract_Cell<SlimeMold_Cell, SlimeMold_CellState> implements Comparable<SlimeMold_Cell>{
//
//    public SlimeMold_Cell(int x, int y, SlimeMold_CellState state) {
//        super(x, y, state);
//    }
//
//    @Override
//    public void interact() {
//        SimulationGrid<SlimeMold_Cell, SlimeMold_CellState> neighborsGrid = getNeighbors();
//
//        if (getCurrentState().hasTurtle()) {
//        	for(Turtle turtle: getCurrentState().getTurtles()){
//        		turtle.depositChemical(this);
//                if(turtle.followGradient(getCurrentState())) turtle.moveToCell(neighborsGrid);
//                else turtle.moveSameDirection(neighborsGrid);
//                //remove turtle
//        	}
//        }
//
//        diffuseAndEvaporate(neighborsGrid);
//
//    }
//
//    public void diffuseAndEvaporate(SimulationGrid<SlimeMold_Cell, SlimeMold_CellState> neighborsGrid) {
//        getCurrentState().evaporate();
//        int numberNeighbors = (int) neighborsGrid.stream().filter(e -> e != null).count();
//
//        double diffusionAmount = getCurrentState().getDiffusionAmount();
//        double diffusionPerNeighbor = diffusionAmount / numberNeighbors;
//        while (neighborsGrid.iterator().hasNext()) {
//            SlimeMold_Cell nextCell = neighborsGrid.iterator().next();
//            if (nextCell != null) {
//                nextCell.getCurrentState().addNextChemical(diffusionPerNeighbor);
//            }
//        }
//
//    }
//
//	@Override
//	public int compareTo(SlimeMold_Cell o) {
//		return (int) (getCurrentState().getChemical()-o.getCurrentState().getChemical());
//	}
//
//
//}
