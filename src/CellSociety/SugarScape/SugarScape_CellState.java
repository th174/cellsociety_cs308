package CellSociety.SugarScape;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;


public final class SugarScape_CellState extends AbstractDiscrete_CellState<SugarScape_CellState, SugarScape_CellState.SugarScapeState> {
    private int growBackTicks = 1;
    private int sugarGrowBackUnits = 1;
    private int maxCapacity;
    private int ticks;
    private int sugar;
    private double sugarFillCutoff = maxCapacity / 5.0;
    private List<Agent> myAgents;

    public SugarScape_CellState(SugarScapeState state) {
        this(state, 0, 0, null);
    }

    public SugarScape_CellState(SugarScapeState state, int startingSugar, int life, List<Agent> nextAgents) {
        super(state);
        sugar = startingSugar;
        ticks = life;
        maxCapacity = 20;
        myAgents = nextAgents == null ? new ArrayList<Agent>() : nextAgents;
    }

    @Override
    public int compareTo(SugarScape_CellState state) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Color getFill() {
        if (sugar < sugarFillCutoff) return Color.WHITE;
        if (sugar < 2 * sugarFillCutoff) return Color.LIGHTPINK;
        if (sugar < 3 * sugarFillCutoff) return Color.ORANGE;
        if (sugar < 4 * sugarFillCutoff) return Color.ORANGERED;
        return Color.CRIMSON;
    }

    @Override
    public SugarScape_CellState getSuccessorState() {
        return new SugarScape_CellState(getState(), sugar, ticks, myAgents);
    }

    @Override
    public SugarScape_CellState getInactiveState() {
        return null;
    }

    public void growSugar() {
        ticks++;
        if (ticks == growBackTicks) {
            ticks = 0;
            sugar = Math.min(sugar + sugarGrowBackUnits, maxCapacity);
        }
    }

    public void removeAgent(Agent a) {
        myAgents.remove(a);
    }

    public List<Agent> getAgents() {
        return myAgents;
    }

    public int getSugar() {
        return sugar;
    }

    public void removeSugar() {
        sugar = 0;
    }

    public enum SugarScapeState {
        DEAD, ALIVE
    }
}
