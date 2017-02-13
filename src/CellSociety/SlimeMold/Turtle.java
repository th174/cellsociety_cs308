package CellSociety.SlimeMold;

import CellSociety.Grids.SimulationGrid;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Turtle {
    private double sniffThreshold;
    private int sniffAngle;
    private int wiggleAngle;
    private int wiggleBias;
    private int currentDirection; //in degrees
    private double amountDeposited;

    public Turtle() {
        this(1.0, 45, 10, 0, 0.6);
    }

    public Turtle(double threshold, int sniffang, int wigangle, int wigbias, double amountDeposited) {
        sniffThreshold = threshold;
        sniffAngle = sniffang;
        wiggleAngle = wigangle;
        wiggleBias = wigbias;
        currentDirection = (int) (Math.random() * 360 + 1);//random angle between 0 and 360
    }

    public void depositChemical(SlimeMold_Cell cell) {
        cell.getCurrentState().addNextChemical(amountDeposited);
    }

    public boolean followGradient(SlimeMold_CellState state) {
        return state.getChemical() >= sniffThreshold;
    }

    public void moveToCell(SimulationGrid<SlimeMold_Cell, SlimeMold_CellState> neighborsGrid) {
        int leftMostAngle = currentDirection + sniffAngle - wiggleBias;
        int rightMostAngle = currentDirection - sniffAngle - wiggleBias;

        List<SlimeMold_Cell> possibleCells = neighborsGrid.stream().collect(Collectors.toList());
        Collections.sort(possibleCells);
        SlimeMold_Cell nextCell = possibleCells.get(0);
        nextCell.getCurrentState().addNextTurtle(this);

    }

    public void moveSameDirection(SimulationGrid<SlimeMold_Cell, SlimeMold_CellState> neighborsGrid) {

        //wiggles randomly
        double leftOrRight = Math.random() * 2 - 1;
        int randomAngle = currentDirection + (int) (leftOrRight * (wiggleAngle - wiggleBias));

        List<SlimeMold_Cell> possibleCells = neighborsGrid.stream().collect(Collectors.toList());

        SlimeMold_Cell nextCell = possibleCells.iterator().next();
        nextCell.getCurrentState().addNextTurtle(this);

    }


}
