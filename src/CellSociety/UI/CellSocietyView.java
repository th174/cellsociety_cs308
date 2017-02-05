package CellSociety.UI;

import CellSociety.Abstract_Cell;
import CellSociety.SimulationGrid;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;


public class CellSocietyView {
    public static final boolean SYSTEM_MENU_BAR = true;
    public static final double ANIMATION_RATE_STEP = 5.0 / 4;
    public static final double ANIMATION_RATE_CAP = 12;
    public static final double ANIMATION_FRAMERATE = 60;
    public static final String RESOURCES_LOCATION = "resources/Menu";
    public static final String TITLE = "Cell Society";
    public static final int MENU_HEIGHT = 12;
    private double framesPerSecond = 3;
    private ResourceBundle myResources;
    private Timeline myAnimation;
    private Scene myScene;
    private SimulationGrid<? extends Abstract_Cell> mySimulationGrid;
    private Set<CellView> cellViews;
    private double windowWidth;
    private double windowHeight;

    public CellSocietyView(double width, double height) {
        windowWidth = width;
        windowHeight = height;
        Pane simulationPane = new Pane();
        simulationPane.setPrefSize(windowWidth, windowHeight);
        simulationPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        BorderPane myBorderPane = new BorderPane();
        myScene = new Scene(myBorderPane);
        myResources = ResourceBundle.getBundle(RESOURCES_LOCATION);
        myBorderPane.setTop(initMenu());
        myBorderPane.setBottom(openNewFile());
        myScene.setOnKeyPressed(this::handleKeyPress);
        KeyFrame frame = new KeyFrame(Duration.seconds(1 / framesPerSecond), e -> update());
        myAnimation = new Timeline(ANIMATION_FRAMERATE, frame);
        myAnimation.setCycleCount(Timeline.INDEFINITE);
        myAnimation.play();
    }

    public Scene getScene() {
        return myScene;
    }

    public String getTitle() {
        return TITLE;
    }

    private void update() {
        mySimulationGrid.update();
        cellViews.forEach(e -> e.updateView(mySimulationGrid.getColumns(), mySimulationGrid.getRows(), windowWidth, windowHeight));
    }

    /**
     * Handle Keyboard user keyboard input
     *
     * @param k
     */
    private void handleKeyPress(KeyEvent k) {
        if (k.getCode() == KeyCode.SPACE) {
            pause();
        } else if (k.getCode() == KeyCode.EQUALS) {
            speedUp();
        } else if (k.getCode() == KeyCode.MINUS) {
            slowDown();
        } else if (k.getCode() == KeyCode.R) {
            reverse();
        } else if (k.getCode() == KeyCode.S) {
            seek(10);
        }
    }

    private MenuBar initMenu() {
        Menu file = new Menu(myResources.getString("File"));
        MenuItem open = new MenuItem(myResources.getString("Open..."));
        open.setOnAction(e -> ((BorderPane) myScene.getRoot()).setBottom(openNewFile()));
        MenuItem save = new MenuItem(myResources.getString("Save"));
        save.setOnAction(s -> save());
        MenuItem exit = new MenuItem(myResources.getString("Exit"));
        exit.setOnAction(s -> exit());
        file.getItems().addAll(open, save, exit);
        Menu simulation = new Menu(myResources.getString("Simulation"));
        MenuItem pause = new MenuItem(myResources.getString("Pause"));
        pause.setOnAction(e -> pause.setText(myResources.getString(pause() ? myResources.getString("Unpause") : myResources.getString("Pause"))));
        MenuItem restart = new MenuItem(myResources.getString("Restart"));
        restart.setOnAction(e -> seek(0));
        MenuItem speedUp = new MenuItem(myResources.getString("Speed_Up"));
        speedUp.setOnAction(e -> speedUp());
        MenuItem slowDown = new MenuItem(myResources.getString("Slow_Down"));
        slowDown.setOnAction(e -> slowDown());
        MenuItem reverse = new MenuItem(myResources.getString("Reverse"));
        reverse.setOnAction((e -> reverse()));
        simulation.getItems().addAll(pause, restart, speedUp, slowDown);
        Menu help = new Menu(myResources.getString("Help"));
        MenuItem viewHelp = new MenuItem(myResources.getString("View_Help"));
        viewHelp.setOnAction(e -> openHelp());
        MenuItem about = new MenuItem(myResources.getString("About"));
        about.setOnAction(e -> about());
        help.getItems().addAll(viewHelp, about);
        MenuBar myMenu = new MenuBar(file, simulation, help);
        myMenu.setUseSystemMenuBar(SYSTEM_MENU_BAR);
        return myMenu;
    }

    private void exit() {
        Platform.exit();
        System.exit(0);
    }

    private boolean pause() {
        if (myAnimation.getStatus().equals(Animation.Status.PAUSED)) {
            myAnimation.play();
            return true;
        } else {
            myAnimation.pause();
            return false;
        }
    }

    private void speedUp() {
        double rate = myAnimation.getCurrentRate();
        if (rate * ANIMATION_RATE_STEP < ANIMATION_RATE_CAP) {
            myAnimation.setRate(rate * ANIMATION_RATE_STEP);
        }
        System.out.printf("New animation speed:\t%.2f\n", myAnimation.getCurrentRate());
    }

    private void slowDown() {
        myAnimation.setRate(myAnimation.getCurrentRate() / ANIMATION_RATE_STEP);
        System.out.printf("New animation speed:\t%.2f\n", myAnimation.getCurrentRate());
    }

    private void seek(int time) {
        mySimulationGrid.forEach(e -> e.seek(time));
    }

    private void reverse() {
        mySimulationGrid.forEach(Abstract_Cell::reverse);
    }

    private Pane openNewFile() {
        if (Objects.nonNull(myAnimation)) {
            pause();
        }
        Pane simulationPane = new Pane();
        simulationPane.setPrefSize(windowWidth, windowHeight);
        simulationPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(myResources.getString("Open_File"));
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Cell Society Data Files", "*.xml"));
        mySimulationGrid = readXML(fileChooser.showOpenDialog(null));
        cellViews = mySimulationGrid.asCollection().stream().map(e -> new CellView(e, this)).collect(Collectors.toSet());
        simulationPane.getChildren().addAll(cellViews);
        if (Objects.nonNull(myAnimation)) {
            pause();
        }
        return simulationPane;
    }

    private void save() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(myResources.getString("Save_File"));
        fileChooser.setInitialFileName("CellSocietyScreenshot1.png");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Portable Network Graphics", "*.png"));
        File output = fileChooser.showSaveDialog(null);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(myScene.snapshot(null), null), "png", output);
        } catch (Exception e) {
            System.out.println("Could not open file for saving!");
            e.printStackTrace();
        }
    }

    private void openHelp() {
        try {
            Desktop.getDesktop().browse(new URI("http://coursework.cs.duke.edu/CompSci308_2017Spring/cellsociety_team21"));
        } catch (Exception e) {
            System.out.println("Could not open help");
        }
    }

    private void about() {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setHeaderText(myResources.getString("CellSociety"));
        about.setTitle(myResources.getString("About"));
        about.setContentText(myResources.getString("AboutContent"));
        about.show();
    }

    /**
     * Read and parse xml file using DOM
     *
     * @param XMLFile name of XML file
     * @return generated SimulationGrid of cells based on XML input
     */
    private SimulationGrid<? extends Abstract_Cell> readXML(File XMLFile) {
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
                                grid[j][k] = getConstructorParamsFromXMLElement(currentElement);
                            }
                        }
                    } else if (!currentElement.hasAttribute("xPos")) {
                        for (int j = 0; j < grid.length; j++) {
                            grid[j][Integer.parseInt(currentElement.getAttribute("yPos"))] = getConstructorParamsFromXMLElement(currentElement);
                        }
                    } else if (!currentElement.hasAttribute("yPos")) {
                        for (int j = 0; j < grid[0].length; j++) {
                            grid[Integer.parseInt(currentElement.getAttribute("xPos"))][j] = getConstructorParamsFromXMLElement(currentElement);
                        }
                    } else {
                        grid[Integer.parseInt(currentElement.getAttribute("xPos"))][Integer.parseInt(currentElement.getAttribute("yPos"))] = getConstructorParamsFromXMLElement(currentElement);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Could not instantiate " + simulationType + "." + simulationType + "Cell");
                }
            }
            return new SimulationGrid<>(grid, cellType);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Could not read input file");
        }
    }

    private String[] getConstructorParamsFromXMLElement(Element currentElement) throws Exception {
        java.util.List<String> constructorParams = new ArrayList<>();
        for (int j = 0; j < currentElement.getElementsByTagName("*").getLength(); j++) {
            constructorParams.add(currentElement.getElementsByTagName("*").item(j).getTextContent());
        }
        return constructorParams.toArray(new String[constructorParams.size()]);
    }
}