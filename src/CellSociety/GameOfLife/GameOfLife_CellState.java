package CellSociety.GameOfLife;


import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;

/**
 * Created by th174 on 1/29/2017.
 */
public final class GameOfLife_CellState extends AbstractDiscrete_CellState<GameOfLife_CellState.GameOfLifeState> {
    public static final GameOfLife_CellState DEAD = new GameOfLife_CellState(GameOfLifeState.DEAD);
    public static final GameOfLife_CellState ALIVE = new GameOfLife_CellState(GameOfLifeState.ALIVE);

    private GameOfLife_CellState(GameOfLifeState state) {
        super(state);
    }

    public GameOfLife_CellState(String s) {
        super(s.toLowerCase().equals("rand") ? randomState(GameOfLifeState.class) : GameOfLifeState.valueOf(s.toUpperCase()));
    }

    @Override
    public Color getFill() {
        return getState().equals(GameOfLifeState.ALIVE) ? Color.GREEN : Color.WHITE;
    }

    @Override
    public GameOfLife_CellState getSuccessorState() {
        return new GameOfLife_CellState(getState());
    }

    enum GameOfLifeState {
        ALIVE, DEAD
    }
}
