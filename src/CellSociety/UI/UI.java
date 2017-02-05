package CellSociety.UI;

import CellSociety.Abstract_Cell;
import CellSociety.SimulationGrid;
import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;


public class UI {
	private Timeline myAnimation;
	private MenuBar myMenuBar;
	private BorderPane myBorderPane;
	private Pane myRoot;
	private SimulationGrid<? extends Abstract_Cell> mySimulationGrid;
	private static double ANIMATION_RATE_STEP = 5.0/4;//CellSocietyMain.ANIMATION_RATE_CAP;
	private static double ANIMATION_RATE_CAP = 12;
	
	public UI(Timeline animation, SimulationGrid<? extends Abstract_Cell> simulationGrid,
			Pane root) {
		myRoot=root;
		myAnimation = animation;
		mySimulationGrid = simulationGrid;
		myMenuBar= new MenuBar();
		myBorderPane= new BorderPane();
		setUpMenu();
	}
	private void setUpMenu(){
		
		Menu pause = new Menu("Pause");
		pause.setOnAction( e -> { 
			System.out.println("trying to ");
			if(myAnimation.getStatus().equals(Animation.Status.PAUSED)){
				myAnimation.play();
			} else {
				myAnimation.pause();
			}
		});
		Menu speedUp = new Menu("Speed Up");
		Menu slowDown = new Menu("Slow Down");
		myMenuBar.getMenus().addAll(pause,speedUp,slowDown);
		myRoot.getChildren().add(myMenuBar);
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
