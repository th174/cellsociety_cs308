package CellSociety.Grids;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import CellSociety.Abstract_Cell;
import CellSociety.Grids.SimulationGrid;
import CellSociety.CellStateTimeline;
import CellSocietyMain;


public class LineGraph<E> {

	private SimulationGrid mySimulation;
	private Pane myRoot;
	private HashMap<Integer, XYChart.Series<Number, Number>> mySeriesMap;
	private int xSeriesData = 0;

	public <T> void Graph(SimulationGrid sim, Pane root) {
		mySimulation= new SimulationGrid sim;
		myRoot = root;
		mySeriesMap = new HashMap<Integer, XYChart.Series<Number, Number>>();
		init();
	}

	private void init() {
		//create axes and graph
		//Need someway yo put in y axis the complete total of the grid
		//get the name of the simulation
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis(0, mySimulation.countTotalOfState(state), mySimulation.countTotalOfState(state) / 100);
		xAxis.setLabel("Time");
		yAxis.setLabel("Number of Cells");
		final LineChart<Number, Number> graph = new LineChart<Number, Number>(xAxis, yAxis);
		graph.setTitle("Cell State Populations");
		//create objects to hold data
		for (int stateInt : mySimulation.countTotalOfState(state)) {
			XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
			series.setName(mySimulation.(stateInt));
			mySeriesMap.put(stateInt, series);
			graph.getData().add(series);
		}
		//Set where we want to put it
		graph.setLayoutX(100);
		graph.setLayoutY(50);
		myRoot.getChildren().add(graph);
	}

	/**
	 * loops through the Grid, calculates populations of each cell type, and updates value
	 * called by animation.java for each step
	 *
	 * @param grid - the simulation's grid
	 */
	public void updateGraph(GridPane grid) {
		ArrayList<Integer> stateCount = new ArrayList<Integer>();
		//number of states
		for (int i = 0; i < mySeriesMap.size(); i++) {
			stateCount.add(0);
		}
		//How do I get total number of cell per each
		
		/*for (int i = 0; i < grid.getHeight(); i++) {
			for (int j = 0; j < grid.getWidth(); j++) {
				Cell cell = grid.getCell(i, j);
				int oldCount = stateCount.get(cell.getCurrState());
				stateCount.set(cell.getCurrState(), oldCount + 1);
			}
		}
		*/
		for (int state = 0; state < stateCount.size(); state++) {
			mySeriesMap.get(state).getData().add(new XYChart.Data<Number, Number>(xSeriesData, stateCount.get(state)));
		}
		xSeriesData += 1;
	}

	/**
	 * removes the Graph object from the scene
	 * this is called by Animation.java when the simulation type changes
	 */
	//How do we remove each?
	public void clearGraph() {
		myRoot.getChildren().remove(mySimulation);
	}


}	
