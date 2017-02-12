package CellSociety.SugarScape;

import CellSociety.Abstract_Cell;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SugarScape_Cell extends Abstract_Cell<SugarScape_Cell, SugarScape_CellState> implements Comparable<SugarScape_Cell>{

    public SugarScape_Cell(int x, int y, SugarScape_CellState state) {
        super(x, y, state);
    }

    @Override
    public void interact() {
        List<SugarScape_Cell> adjNeighbors = getNeighbors().stream().collect(Collectors.toList());
        //use Agent's vision as well
        Collections.sort(adjNeighbors);
        for (Agent a : getCurrentState().getAgents()) {
            a.grabSugar(adjNeighbors.get(0));
            a.metabolize();
            if (a.isDead()) getNextState().removeAgent(a);
            Collections.sort(adjNeighbors); //update so we always grab the best cell
        }
        getNextState().growSugar();
    }

    

	@Override
	public int compareTo(SugarScape_Cell o) {
		if (o.getNextState().getSugar() != getNextState().getSugar())
            return o.getNextState().getSugar() - getNextState().getSugar();
        else return 0; //return closest
	}

}
