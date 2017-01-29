/**
 * Created by th174 on 1/29/2017.
 */

import CellSociety.Cell;
import CellSociety.CellState;
import CellSociety.SimulationGrid;
import CellSociety.WindowProperties;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;


public class CellSocietyMain extends Application {
    //test variables, should be read from xml
    public static final double SIZE = 1000;
    public static final double FRAMES_PER_SECOND = 1;
    public static final double MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final String TITLE = "Cell Society";
    public static final String INPUT = "gameOfLife.xml";
    private SimulationGrid<Cell> gameOfLifeGrid;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene simulation = new Scene(root, SIZE, SIZE, Color.BLACK);
        gameOfLifeGrid = readXML(INPUT);
        WindowProperties.setDimensions(SIZE, SIZE);
        primaryStage.setScene(simulation);
        primaryStage.setTitle(TITLE);
        primaryStage.show();
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> update());
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
        root.getChildren().addAll(gameOfLifeGrid.asCollection());
    }

    public void update() {
        gameOfLifeGrid.forEach(Cell::updateState);
        gameOfLifeGrid.forEach(e -> e.interact(gameOfLifeGrid));
    }

    public SimulationGrid<Cell> readXML(String XMLFile) {
        try {
            String userDirectory = System.getProperty("user.dir");
            Document file = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(userDirectory + "/data/" + XMLFile);
            Cell[][] grid = new Cell[Integer.parseInt(file.getDocumentElement().getAttribute("width"))][Integer.parseInt(file.getDocumentElement().getAttribute("height"))];
            Class<Cell> defaultCellType = (Class<Cell>) Class.forName(file.getDocumentElement().getAttribute("type") + ".Cell");
            CellState defaultCellState = (CellState) Class.forName(file.getDocumentElement().getAttribute("type") + ".CellState").getDeclaredConstructor(String.class).newInstance((file.getDocumentElement().getAttribute("defaultState")));
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
            e.printStackTrace();
        }
        return null;
    }
}
