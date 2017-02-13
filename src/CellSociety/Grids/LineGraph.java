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


public class LineGraph extends Application implements SimulationGridImpl {
	/* private static final int MAX_DATA_POINTS = 6000;

    private Series series;
    private int xSeriesData = 0;
    private ConcurrentLinkedQueue<Number> dataQ = new ConcurrentLinkedQueue<Number>();
    private ExecutorService executor;
    private AddToQueue addToQueue;
    private Timeline timeline2;
    private NumberAxis xAxis;

    private void init(Stage primaryStage) {
        xAxis = new NumberAxis(0,MAX_DATA_POINTS,MAX_DATA_POINTS/10);
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(true);

        //-- Chart
        final LineChart<Number, Number> sc = new LineChart<Number, Number>(xAxis, yAxis) {
            // Override to remove symbols on each data point
            @Override protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {}
        };
        sc.setAnimated(false);
        sc.setId("liveLineChart");
        sc.setTitle("Animated Area Chart");

        //-- Chart Series
        series = new LineChart.Series<Number, Number>();
        series.setName("Area Chart Series");
        sc.getData().add(series);

        primaryStage.setScene(new Scene(sc));
    }

  public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();

        //-- Prepare Executor Services
        executor = Executors.newCachedThreadPool();
        addToQueue = new AddToQueue();
        executor.execute(addToQueue);
        //-- Prepare Timeline
        prepareTimeline();
    }


    private class AddToQueue implements Runnable {
        public void run() {
            try {
                // add a item of random data to queue
                dataQ.add(Math.random());
                Thread.sleep(10);
                executor.execute(this);
            } catch (InterruptedException ex) {
                Logger.getLogger(LineChart.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //-- Timeline gets called in the JavaFX Main thread
    private void prepareTimeline() {
        // Every frame to take any data from queue and add to chart
        new AnimationTimer() {
            @Override public void handle(long now) {
                addDataToSeries();
            }
        }.start();
    }

    private void addDataToSeries() {
        for (int i = 0; i <size() ; i++) { //-- add 20 numbers to the plot+
            if (dataQ.isEmpty()) break;
            series.getData().add(new LineChart.Data(xSeriesData++, dataQ.remove()));
        }
        // remove points to keep us at no more than MAX_DATA_POINTS
        if (series.getData().size() > MAX_DATA_POINTS) {
            series.getData().remove(0, series.getData().size() - MAX_DATA_POINTS);
        }
        // update 
        xAxis.setLowerBound(xSeriesData-MAX_DATA_POINTS);
        xAxis.setUpperBound(xSeriesData-1);
  }*/
	//Where should I get the Simulation
	private Simulation mySimulation;
	private Pane myRoot;
	private HashMap<Integer, XYChart.Series<Number, Number>> mySeriesMap;
	private int xSeriesData = 0;

	public Graph(Simulation sim, Pane root) {
		mySimulation = sim;
		myRoot = root;
		mySeriesMap = new HashMap<Integer, XYChart.Series<Number, Number>>();
		init();
	}

	private void init() {
		//create axes and graph
		//Need someway yo put in y axis the complete total of the grid
		//get the name of the simulation
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis(0, CellSociety.Grids.SimulationGrid.countTotalofState, maxY / 100);
		xAxis.setLabel("Time");
		yAxis.setLabel("Number of Cells");
		final LineChart<Number, Number> graph = new LineChart<Number, Number>(xAxis, yAxis);
		graph.setTitle("Cell State Populations");
		//create objects to hold data
		for (int stateInt : mySimulation.getStateMap().keySet()) {
			XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
			series.setName(mySimulation.get(stateInt));
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

