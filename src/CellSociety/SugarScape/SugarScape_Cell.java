package CellSociety.SugarScape;

import CellSociety.Abstract_Cell;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SugarScape_Cell extends Abstract_Cell<SugarScape_Cell, SugarScape_CellState> implements Comparable<SugarScape_Cell> {

	public SugarScape_Cell(int x, int y, String... params){
		this(x,y,new SugarScape_CellState(params));
	}

    public SugarScape_Cell(int x, int y, SugarScape_CellState state) {
        super(x, y, state);
    }

    @Override
    public void interact() {
    	getNextState().growSugar();
    	if(!getCurrentState().isEmpty()){
    		Agent a = getCurrentState().getAgent();
    		List<SugarScape_Cell> allNeighbors = getNeighbors().stream().collect(Collectors.toList());
    		reproduce(a,allNeighbors);
    		
            List<SugarScape_Cell> emptyNeighbors = getNeighbors().stream().filter(e -> e.getCurrentState().isEmpty()).collect(Collectors.toList());
            //use Agent's vision as well
            if(!emptyNeighbors.isEmpty()){
            	move(a,emptyNeighbors);
            }
           
    	}  
    }
    public void reproduce(Agent a, List<SugarScape_Cell> neighbors){
    	if(a.canReproduce()){
    		for(SugarScape_Cell cell:neighbors.stream().filter(e -> e.getCurrentState().canReproduceHere()).collect(Collectors.toList())){
    			Agent otherAgent = cell.getCurrentState().getAgent();
    			if(a.canReproduce(otherAgent)&& (getVacantNeighbor()!=null || cell.getVacantNeighbor()!=null)){
    				Agent baby = a.reproduceWith(otherAgent);
    				SugarScape_Cell babyCell = getVacantNeighbor()==null? cell.getVacantNeighbor() : getVacantNeighbor();
    				babyCell.getCurrentState().setNextAgent(baby);
    			}
    		}
    	}
    }
    public void move(Agent a, List<SugarScape_Cell> neighbors){
    	 Collections.sort(neighbors);
     	a.grabSugar(neighbors.get(0));
     	a.metabolize();
     	if(a.isDead()) getCurrentState().removeAgent(a);
     	neighbors.get(0).getCurrentState().setNextAgent(a);
             
    }
    public SugarScape_Cell getVacantNeighbor(){
    	Optional<SugarScape_Cell> potentialN = getNeighbors().stream().filter(e -> e.getCurrentState().isEmpty()).findAny();
    	if(potentialN.isPresent()) return potentialN.get();
    	return null;
    }


    @Override
    public int compareTo(SugarScape_Cell o) {
        if (o.getCurrentState().getSugar() != getCurrentState().getSugar())
            return o.getNextState().getSugar() - getNextState().getSugar();
        else return 0; //return closest
    }

}
