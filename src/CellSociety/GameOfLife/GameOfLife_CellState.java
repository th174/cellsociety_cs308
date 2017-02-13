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

    /**
     * Initializes the cellstate
     * @param state 
     */
    public GameOfLife_CellState(GameOfLife_CellState state) {
        this(state.getState());
    }

    /**
     * Initializes cellstate with unknown number of parameters
     * @param params
     */
    public GameOfLife_CellState(String... params) {
        super(params[0].toLowerCase().equals("rand") ? randomState(GameOfLifeState.class) : GameOfLifeState.valueOf(params[0].toUpperCase()));
    }

    /**
     * @return Graphical representation of this CellState according to being alive
     */
    @Override
    public Color getFill() {
        return getState().equals(GameOfLifeState.ALIVE) ? Color.GREEN : Color.WHITE;
    }

    /**
     * @return the successor state of the cell
     */
    @Override
    public GameOfLife_CellState getSuccessorState() {
        return new GameOfLife_CellState(this);
    }

    enum GameOfLifeState {
        ALIVE, DEAD
    }
}
