package CellSociety.Fire;

import CellSociety.Abstract_Cell;

/**
 * Created by th174 on 1/29/2017.
 */
public class Fire_Cell extends Abstract_Cell<Fire_CellState> {
    public static final double DEFAULT_PROB_CATCH_FIRE = 0.2;
    private double probCatchFire;

    public Fire_Cell(int x, int y, String... params) {
        this(x, y, new Fire_CellState(params[0]), DEFAULT_PROB_CATCH_FIRE);
        if (params.length > 1) {
            probCatchFire = Double.parseDouble(params[1]);
        }
    }

    public Fire_Cell(int x, int y, Fire_CellState state, double chanceFire) {
        super(x, y, state);
        probCatchFire = chanceFire;
    }

    @Override
    public void interact() {
        //TODO: Implement this
    }
}
