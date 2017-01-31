/**
 * Created by th174 on 1/29/2017.
 */

import CellSociety.Abstract_Cell;
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
    public static final String TITLE = "Cell Society";
    private static final String WILDCARD = "*";
    private double framesPerSecond = 3;
    private SimulationGrid<? extends Abstract_Cell> mySimulationGrid;
    private Timeline animation;

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
        root.getChildren().addAll(mySimulationGrid.asCollection().stream().map(Abstract_Cell::getRectangle).collect(Collectors.toSet()));
    }

    private void update() {
        mySimulationGrid.forEach(Abstract_Cell::updateState);
        mySimulationGrid.forEach(Abstract_Cell::interact);
    }

    /**
     * Read and parse xml file using DOM
     *
     * @param XMLFile name of XML file
     * @return generated SimulationGrid of cells based on XML input
     */
    private SimulationGrid<? extends Abstract_Cell> readXML(String XMLFile) {
        try {
            Document file = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(XMLFile);
            Element root = file.getDocumentElement();
            //set up grid of references to constructorParams
            String[][][] grid = new String[Integer.parseInt(root.getAttribute("width"))][Integer.parseInt(root.getAttribute("height"))][0];
            try {
                framesPerSecond = Double.parseDouble(root.getAttribute("fps"));
            } catch (Exception e) {
            }
            String simulationType = root.getAttribute("type");
            Class<? extends Abstract_Cell> cellType = (Class<? extends Abstract_Cell>) Class.forName("CellSociety." + simulationType + "." + simulationType + "_Cell");
            //instantiate cells
            NodeList cells = file.getElementsByTagName("Cell");
            for (int i = 0; i < cells.getLength(); i++) {
                Element currentElement = (Element) cells.item(i);
                try {
                    if (!currentElement.hasAttribute("xPos") && !currentElement.hasAttribute("yPos")) {
                        for (int j = 0; j < grid.length; j++) {
                            for (int k = 0; k < grid[j].length; k++) {
                                grid[j][k] = getConstructorParamsFromXMLElement(currentElement, simulationType);
                            }
                        }
                    } else if (!currentElement.hasAttribute("xPos")) {
                        for (int j = 0; j < grid.length; j++) {
                            grid[j][Integer.parseInt(currentElement.getAttribute("yPos"))] = getConstructorParamsFromXMLElement(currentElement, simulationType);
                        }
                    } else if (!currentElement.hasAttribute("yPos")) {
                        for (int j = 0; j < grid[0].length; j++) {
                            grid[Integer.parseInt(currentElement.getAttribute("xPos"))][j] = getConstructorParamsFromXMLElement(currentElement, simulationType);
                        }
                    } else {
                        grid[Integer.parseInt(currentElement.getAttribute("xPos"))][Integer.parseInt(currentElement.getAttribute("yPos"))] = getConstructorParamsFromXMLElement(currentElement, simulationType);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Could not instantiate " + simulationType + "." + simulationType + "Cell");
                }
            }
            return new SimulationGrid<>(grid, (Class<Abstract_Cell>) cellType);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Could not read input file");
        }
    }

    private String[] getConstructorParamsFromXMLElement(Element currentElement, String simulationType) throws Exception {
        List<String> constructorParams = new ArrayList<>();
        for (int j = 0; j < currentElement.getElementsByTagName("*").getLength(); j++) {
            constructorParams.add(currentElement.getElementsByTagName("*").item(0).getTextContent());
        }
        return constructorParams.toArray(new String[constructorParams.size()]);
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
