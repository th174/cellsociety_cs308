/**
 * Created by th174 on 1/29/2017.
 */

import CellSociety.AbstractCell;
import CellSociety.CellState;
import CellSociety.SimulationGrid;
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

import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CellSocietyMain extends Application {
    //test variables, should be read from xml
    public static final double SIZE = 1000;
    public static final String TITLE = "AbstractCell Society";
    private double framesPerSecond = 3;
    private SimulationGrid<AbstractCell> mySimulationGrid;
    private
    Timeline animation;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene simulation = new Scene(root, SIZE, SIZE, Color.BLACK);
        mySimulationGrid = readXML(getParameters().getUnnamed().get(0));
        mySimulationGrid.setWindowDimensions(SIZE, SIZE);
        simulation.setOnKeyPressed(this::handleKeyPress);
        primaryStage.setResizable(false);
        primaryStage.setScene(simulation);
        primaryStage.setTitle(TITLE);
        primaryStage.show();
        KeyFrame frame = new KeyFrame(Duration.seconds(1 / framesPerSecond), e -> update());
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
        root.getChildren().addAll(mySimulationGrid.asCollection().stream().map(AbstractCell::getRectangle).collect(Collectors.toSet()));
    }

    private void update() {
        mySimulationGrid.forEach(AbstractCell::updateState);
        mySimulationGrid.forEach(e -> e.interact(mySimulationGrid));
    }

    /**
     * Read and parse xml file using DOM
     *
     * @param XMLFile name of XML file
     * @return generated SimulationGrid of cells based on XML input
     */
    private SimulationGrid<AbstractCell> readXML(String XMLFile) {
        try {
            Document file = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(XMLFile);
            Element root = file.getDocumentElement();
            AbstractCell[][] grid = new AbstractCell[Integer.parseInt(root.getAttribute("width"))][Integer.parseInt(root.getAttribute("height"))];
            Class<AbstractCell> defaultCellType = (Class<AbstractCell>) Class.forName(root.getAttribute("type") + ".AbstractCell");
            CellState defaultCellState = (CellState) Class.forName(root.getAttribute("type") + ".CellState").getConstructor(String.class).newInstance((root.getAttribute("defaultState")));
            try {
                framesPerSecond = Double.parseDouble(root.getAttribute("fps"));
            } catch (Exception e) {
                System.out.println("Using fps = 3");
            }
            NodeList cells = file.getElementsByTagName("AbstractCell");
            for (int i = 0; i < cells.getLength(); i++) {
                Element currentCell = (Element) cells.item(i);
                List<Object> constructorParams = new ArrayList<>();
                List<Class> constructorParamTypes = new ArrayList<>();
                int x = Integer.parseInt(currentCell.getElementsByTagName("xPos").item(0).getTextContent());
                constructorParams.add(x);
                constructorParamTypes.add(int.class);
                int y = Integer.parseInt(currentCell.getElementsByTagName("yPos").item(0).getTextContent());
                constructorParams.add(y);
                constructorParamTypes.add(int.class);
                CellState state = (CellState) Class.forName(root.getAttribute("type") + ".CellState").getConstructor(String.class).newInstance(currentCell.getElementsByTagName("State").item(0).getTextContent());
                constructorParams.add(state);
                constructorParamTypes.add(CellSociety.CellState.class);
                for (int j = 3; j < currentCell.getElementsByTagName("*").getLength(); j++) {
                    constructorParams.add(currentCell.getElementsByTagName("*").item(0).getTextContent());
                    constructorParamTypes.add(String.class);
                }
                Class<? extends AbstractCell> cellType = (Class<? extends AbstractCell>) Class.forName(root.getAttribute("type") + ".AbstractCell");
                grid[x][y] = cellType.getConstructor(constructorParamTypes.toArray(new Class[constructorParamTypes.size()]))
                        .newInstance(constructorParams.toArray(new Object[constructorParams.size()]));
            }
            return new SimulationGrid<>(grid, defaultCellType, defaultCellState);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Could not read input file");
        }
    }

    /**
     * Handle Keyboard user keyboard input
     *
     * @param k
     */
    private void handleKeyPress(KeyEvent k) {
        if (k.getCode() == KeyCode.SPACE) {
            if (animation.getStatus().equals(Animation.Status.PAUSED)) {
                animation.play();
            } else {
                animation.pause();
            }
        } else if (k.getCode() == KeyCode.EQUALS) {
            //TODO: Implement Speed Up
        } else if (k.getCode() == KeyCode.MINUS) {
            //TODO: Implement Slow Down
        }
    }
}
