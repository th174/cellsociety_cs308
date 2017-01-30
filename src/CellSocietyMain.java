/**
 * Created by th174 on 1/29/2017.
 */

import CellSociety.Cell;
import CellSociety.CellState;
import CellSociety.SimulationGrid;
import CellSociety.WindowProperties;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilderFactory;


public class CellSocietyMain extends Application {
    //test variables, should be read from xml
    public static final double SIZE = 1000;
    public static final String TITLE = "Cell Society";
    private double framesPerSecond = 3;
    private SimulationGrid<Cell> gameOfLifeGrid;
    Timeline animation;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene simulation = new Scene(root, SIZE, SIZE, Color.BLACK);
        gameOfLifeGrid = readXML(getParameters().getUnnamed().get(0));
        simulation.setOnKeyPressed(this::handleKeyPress);
        WindowProperties.setDimensions(SIZE, SIZE);
        primaryStage.setScene(simulation);
        primaryStage.setTitle(TITLE);
        primaryStage.show();
        KeyFrame frame = new KeyFrame(Duration.seconds(1 / framesPerSecond), e -> update());
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
        root.getChildren().addAll(gameOfLifeGrid.asCollection().stream().map(Cell::getRectangle).collect(Collectors.toSet()));
    }

    private void update() {
        gameOfLifeGrid.forEach(Cell::updateState);
        gameOfLifeGrid.forEach(e -> e.interact(gameOfLifeGrid));
    }

    /**
     * Read and parse xml file using DOM
     * @param XMLFile name of XML file
     * @return generated SimulationGrid of cells based on XML input
     */
    private SimulationGrid<Cell> readXML(String XMLFile) {
        try {
            Document file = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(XMLFile);
            Element root = file.getDocumentElement();
            Cell[][] grid = new Cell[Integer.parseInt(file.getDocumentElement().getAttribute("width"))][Integer.parseInt(file.getDocumentElement().getAttribute("height"))];
            Class<Cell> defaultCellType = (Class<Cell>) Class.forName(file.getDocumentElement().getAttribute("type") + ".Cell");
            CellState defaultCellState = (CellState) Class.forName(file.getDocumentElement().getAttribute("type") + ".CellState").getDeclaredConstructor(String.class).newInstance((file.getDocumentElement().getAttribute("defaultState")));
            try {
                framesPerSecond = Double.parseDouble(root.getAttribute("fps"));
            } catch (Exception e) {
                System.out.println("Using fps = 3");
            }
            NodeList cells = file.getElementsByTagName("Cell");
            for (int i = 0; i < cells.getLength(); i++) {
                Element currentCell = (Element) cells.item(i);
                Class<? extends Cell> cellType = (Class<? extends Cell>) Class.forName(currentCell.getAttribute("type") + ".Cell");
                CellState state = (CellState) Class.forName(file.getDocumentElement().getAttribute("type") + ".CellState").getDeclaredConstructor(String.class).newInstance(currentCell.getElementsByTagName("State").item(0).getTextContent());
                int x = Integer.parseInt(currentCell.getElementsByTagName("xPos").item(0).getTextContent());
                int y = Integer.parseInt(currentCell.getElementsByTagName("yPos").item(0).getTextContent());
                grid[x][y] = cellType.getDeclaredConstructor(int.class, int.class, CellState.class, SimulationGrid.class).newInstance(x, y, state, null);
            }
            return new SimulationGrid<>(grid, defaultCellType, defaultCellState);
        } catch (Exception e) {
            System.out.println("Could not read input file");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Handle Keyboard user keyboard input
     * @param k
     */
    private void handleKeyPress(KeyEvent k) {
        if (k.getCode() == KeyCode.SPACE) {
            if (animation.getStatus().equals(Animation.Status.PAUSED)) {
                animation.play();
            } else {
                animation.pause();
            }
        } else if (k.getCode() == KeyCode.EQUALS){
            //TODO: Implement Speed Up
        } else if (k.getCode() == KeyCode.MINUS){
            //TODO: Implement Slow Down
        }
    }
}
