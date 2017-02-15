package CellSociety.GameOfLife;

import CellSociety.AbstractDiscrete_CellState;
import javafx.scene.paint.Color;

/**
 * This class models a single CellState of a single Cell in the GameOfLife simulation.
 * Note: This class is immutable. All fields MUST be declared final.
 *
 * @see CellSociety.AbstractDiscrete_CellState
 * Created by th174 on 1/29/2017.
 */
public final class GameOfLife_CellState extends AbstractDiscrete_CellState<GameOfLife_CellState, GameOfLife_CellState.GameOfLifeState> {
    public static final GameOfLife_CellState DEAD = new GameOfLife_CellState(GameOfLifeState.DEAD);
    public static final GameOfLife_CellState ALIVE = new GameOfLife_CellState(GameOfLifeState.ALIVE);

    private GameOfLife_CellState(GameOfLifeState state) {
        super(state);
    }

    /**
     * Constructs new GameOfLife_Cellstate with String properties read from XML file
     *
     * @param params String properties from XML file
     * @see #GameOfLife_CellState(GameOfLifeState)
     */
    public GameOfLife_CellState(String... params) {
        this(params[0].toLowerCase().equals("rand") ? randomState(GameOfLifeState.class) : GameOfLifeState.valueOf(params[0].toUpperCase()));
    }

    /**
     * Constructs new GameOfLife_CellState from another GameOfLife_CellState
     *
     * @param state CellState to be copied
     */
    public GameOfLife_CellState(GameOfLife_CellState state) {
        this(state.getState());
    }


    /**
     * @return Graphical representation of this CellState
     * @see AbstractDiscrete_CellState#getFill()
     */
    @Override
    public Color getFill() {
        return getState().equals(GameOfLifeState.ALIVE) ? Color.GREEN : Color.WHITE;
    }

    /**
     * @see AbstractDiscrete_CellState#getSuccessorState()
     */
    @Override
    public GameOfLife_CellState getSuccessorState() {
        return new GameOfLife_CellState(this);
    }

    enum GameOfLifeState {
        ALIVE, DEAD
    }
}
