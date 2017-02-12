package CellSociety.GameOfLife;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;

/**
 * Created by th174 on 1/29/2017.
 */
public final class GameOfLife_CellState extends AbstractDiscrete_CellState<GameOfLife_CellState, GameOfLife_CellState.GameOfLifeState> {
    public static final GameOfLife_CellState DEAD = new GameOfLife_CellState(GameOfLifeState.DEAD);
    public static final GameOfLife_CellState ALIVE = new GameOfLife_CellState(GameOfLifeState.ALIVE);

    private GameOfLife_CellState(GameOfLifeState state) {
        super(state);
    }

    public GameOfLife_CellState(String... params) {
        super(params[0].toLowerCase().equals("rand") ? randomState(GameOfLifeState.class) : GameOfLifeState.valueOf(params[0].toUpperCase()));
    }

    @Override
    public Color getFill() {
        return getState().equals(GameOfLifeState.ALIVE) ? Color.GREEN : Color.WHITE;
    }

    @Override
    public GameOfLife_CellState getSuccessorState() {
        return new GameOfLife_CellState(getState());
    }

    @Override
    public GameOfLife_CellState getInactiveState() {
        return new GameOfLife_CellState(GameOfLifeState.DEAD);
    }

    enum GameOfLifeState {
        ALIVE, DEAD
    }
}
