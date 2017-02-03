package CellSociety.UI;

import CellSociety.Abstract_Cell;
import CellSociety.SimulationGrid;
import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class UI {
	private Timeline myAnimation;
	private SimulationGrid<? extends Abstract_Cell> mySimulationGrid;
	private static double ANIMATION_RATE_STEP = 5.0/4;//CellSocietyMain.ANIMATION_RATE_CAP;
	private static double ANIMATION_RATE_CAP = 12;
	
	public UI(Timeline animation, SimulationGrid<? extends Abstract_Cell> simulationGrid) {
		myAnimation = animation;
		mySimulationGrid = simulationGrid;
	}

    /**
     * Handle Keyboard user keyboard input
     *
     * @param k
     */
	
    public void handleKeyPress(KeyEvent k) {
        if (k.getCode() == KeyCode.SPACE) {
            if (myAnimation.getStatus().equals(Animation.Status.PAUSED)) {
                myAnimation.play();
            } else {
                myAnimation.pause();
            }
        } else if (k.getCode() == KeyCode.EQUALS) {
            double rate = myAnimation.getCurrentRate();
            if (rate * ANIMATION_RATE_STEP < ANIMATION_RATE_CAP) {
                myAnimation.setRate(rate * ANIMATION_RATE_STEP);
            }
            System.out.printf("New animation speed:\t%.2f\n", myAnimation.getCurrentRate());
        } else if (k.getCode() == KeyCode.MINUS) {
            myAnimation.setRate(myAnimation.getCurrentRate() / ANIMATION_RATE_STEP);
            System.out.printf("New animation speed:\t%.2f\n", myAnimation.getCurrentRate());
        } else if (k.getCode() == KeyCode.R) {
            mySimulationGrid.forEach(Abstract_Cell::reverse);
        } else if (k.getCode() == KeyCode.DIGIT1) {
            mySimulationGrid.forEach(e -> e.seek(1));
        } else if (k.getCode() == KeyCode.DIGIT2) {
            mySimulationGrid.forEach(e -> e.seek(2));
        } else if (k.getCode() == KeyCode.DIGIT3) {
            mySimulationGrid.forEach(e -> e.seek(3));
        } else if (k.getCode() == KeyCode.DIGIT4) {
            mySimulationGrid.forEach(e -> e.seek(4));
        } else if (k.getCode() == KeyCode.DIGIT5) {
            mySimulationGrid.forEach(e -> e.seek(5));
        } else if (k.getCode() == KeyCode.DIGIT6) {
            mySimulationGrid.forEach(e -> e.seek(6));
        } else if (k.getCode() == KeyCode.DIGIT7) {
            mySimulationGrid.forEach(e -> e.seek(7));
        } else if (k.getCode() == KeyCode.DIGIT8) {
            mySimulationGrid.forEach(e -> e.seek(8));
        } else if (k.getCode() == KeyCode.DIGIT9) {
            mySimulationGrid.forEach(e -> e.seek(9));
        } else if (k.getCode() == KeyCode.DIGIT0) {
            mySimulationGrid.forEach(e -> e.seek(10));
        }
    }
}
