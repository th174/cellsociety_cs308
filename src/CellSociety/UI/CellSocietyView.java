package CellSociety.UI;

import CellSociety.Abstract_Cell;
import CellSociety.Abstract_CellState;
import CellSociety.Grids.SimulationGrid;
import CellSociety.UI.CellView.Abstract_CellView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
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
import java.util.Collection;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class CellSocietyView<T extends Abstract_CellView> {
    public static final boolean SYSTEM_MENU_BAR = true;
    public static final double ANIMATION_RATE_STEP = 5.0 / 4;
    public static final double ANIMATION_RATE_CAP = 15;
    public static final double ANIMATION_FRAMERATE = 60;
    public static final String RESOURCES_LOCATION = "resources/Strings";
    private double framesPerSecond = 3;
    private ResourceBundle myResources;
    private Timeline myAnimation;
    private Scene myScene;
    private SimulationGrid<? extends Abstract_Cell> mySimulationGrid;
    private Collection<T> cellViews;
    private double windowWidth;
    private double windowHeight;

    public CellSocietyView(double width, double height) {
        windowWidth = width;
        windowHeight = height;
        BorderPane myBorderPane = new BorderPane();
        myScene = new Scene(myBorderPane);
        myResources = ResourceBundle.getBundle(RESOURCES_LOCATION);
        myBorderPane.setTop(initMenu());
        myBorderPane.setBottom(openNewFile());
        KeyFrame frame = new KeyFrame(Duration.seconds(1 / framesPerSecond), e -> update());
        myAnimation = new Timeline(ANIMATION_FRAMERATE, frame);
        myAnimation.setCycleCount(Timeline.INDEFINITE);
        myAnimation.play();
    }

    public Scene getScene() {
        return myScene;
    }

    public String getTitle() {
        return myResources.getString("Title");
    }

    private void update() {
        mySimulationGrid.update();
        cellViews.forEach(e -> e.updateView(mySimulationGrid.getColumns(), mySimulationGrid.getRows(), windowWidth, windowHeight));
    }

    private MenuBar initMenu() {
        String OS = System.getProperty("os.name").toLowerCase();
        Menu file = initFileMenu(OS);
        Menu simulation = initSimulationMenu(OS);
        Menu help = initHelpMenu(OS);
        MenuBar myMenu = new MenuBar(file, simulation, help);
        myMenu.setUseSystemMenuBar(SYSTEM_MENU_BAR);
        return myMenu;
    }

    private Menu initFileMenu(String OS) {
        MenuItem open = new MenuItem(myResources.getString("Open..."));
        open.setAccelerator(new KeyCodeCombination(KeyCode.O, OS.contains("mac") ? KeyCombination.META_DOWN : KeyCombination.CONTROL_DOWN));
        open.setOnAction(e -> ((BorderPane) myScene.getRoot()).setBottom(openNewFile()));
        MenuItem save = new MenuItem(myResources.getString("Save"));
        save.setAccelerator(new KeyCodeCombination(KeyCode.S, OS.contains("mac") ? KeyCombination.META_DOWN : KeyCombination.CONTROL_DOWN));
        save.setOnAction(s -> save());
        MenuItem exit = new MenuItem(myResources.getString("Exit"));
        exit.setAccelerator(new KeyCodeCombination(KeyCode.ESCAPE, OS.contains("mac") ? KeyCombination.META_DOWN : KeyCombination.CONTROL_DOWN));
        exit.setOnAction(s -> exit());
        return new Menu(myResources.getString("File"), null, open, save, exit);
    }

    private Menu initSimulationMenu(String OS) {
        MenuItem pause = new MenuItem(myResources.getString("Pause"));
        pause.setAccelerator(new KeyCodeCombination(KeyCode.SPACE));
        pause.setOnAction(e -> pause.setText(myResources.getString(pause() ? myResources.getString("Unpause") : myResources.getString("Pause"))));
        MenuItem restart = new MenuItem(myResources.getString("Restart"));
        restart.setAccelerator(new KeyCodeCombination(KeyCode.R, OS.contains("mac") ? KeyCombination.META_DOWN : KeyCombination.CONTROL_DOWN));
        restart.setOnAction(e -> seek(0));
        MenuItem speedUp = new MenuItem(myResources.getString("Speed_Up"));
        speedUp.setAccelerator(new KeyCodeCombination(KeyCode.EQUALS));
        speedUp.setOnAction(e -> speedUp());
        MenuItem slowDown = new MenuItem(myResources.getString("Slow_Down"));
        slowDown.setAccelerator(new KeyCodeCombination(KeyCode.MINUS));
        slowDown.setOnAction(e -> slowDown());
        MenuItem reverse = new MenuItem(myResources.getString("Reverse"));
        reverse.setAccelerator(new KeyCodeCombination(KeyCode.R));
        reverse.setOnAction((e -> reverse()));
        MenuItem seek = new MenuItem(myResources.getString("Seek..."));
        seek.setAccelerator(new KeyCodeCombination(KeyCode.S));
        seek.setOnAction(e -> seek());
        MenuItem stepForward = new MenuItem(myResources.getString("Step_Forward"));
        stepForward.setAccelerator(new KeyCodeCombination(KeyCode.EQUALS, OS.contains("mac") ? KeyCombination.META_DOWN : KeyCombination.CONTROL_DOWN));
        stepForward.setOnAction(e -> stepForward());
        MenuItem stepBackward = new MenuItem(myResources.getString("Step_Backward"));
        stepBackward.setAccelerator(new KeyCodeCombination(KeyCode.MINUS, OS.contains("mac") ? KeyCombination.META_DOWN : KeyCombination.CONTROL_DOWN));
        stepBackward.setOnAction(e -> stepBackward());
        return new Menu(myResources.getString("Simulation"), null, pause, speedUp, slowDown, reverse, seek, stepForward, stepBackward, restart);
    }

    private Menu initHelpMenu(String OS) {
        MenuItem viewHelp = new MenuItem(myResources.getString("View_Help"));
        viewHelp.setAccelerator(new KeyCodeCombination(KeyCode.F1));
        viewHelp.setOnAction(e -> openHelp());
        MenuItem about = new MenuItem(myResources.getString("About"));
        about.setAccelerator(new KeyCodeCombination(KeyCode.F1, OS.contains("mac") ? KeyCombination.META_DOWN : KeyCombination.CONTROL_DOWN));
        about.setOnAction(e -> about());
        return new Menu(myResources.getString("Help"), null, viewHelp, about);
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
        if (myAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            double rate = myAnimation.getCurrentRate();
            if (rate * ANIMATION_RATE_STEP < ANIMATION_RATE_CAP) {
                myAnimation.setRate(rate * ANIMATION_RATE_STEP);
            }
            System.out.printf("New animation speed:\t%.2f\n", myAnimation.getCurrentRate());
        }
    }

    private void slowDown() {
        if (myAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            myAnimation.setRate(myAnimation.getCurrentRate() / ANIMATION_RATE_STEP);
            System.out.printf("New animation speed:\t%.2f\n", myAnimation.getCurrentRate());
        }
    }

    private void stepForward() {
        update();
        myAnimation.pause();
    }

    private void stepBackward() {
        reverse();
        update();
        reverse();
        myAnimation.pause();
    }

    private void seek() {
        TextInputDialog dbox = new TextInputDialog();
        dbox.setHeaderText(myResources.getString("Seek..."));
        dbox.setContentText(myResources.getString("SeekContent"));
        seek(Integer.parseInt(dbox.showAndWait().orElse(-1 + "")));
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
        do {
            File xmlInput;
            while (Objects.isNull(xmlInput = fileChooser.showOpenDialog(null)) && Objects.isNull(myAnimation)) ;
            mySimulationGrid = readXML(xmlInput);
        } while (Objects.isNull(mySimulationGrid));
        cellViews = mySimulationGrid.parallelStream().map(this::instantiateCellView).filter(Objects::nonNull).collect(Collectors.toSet());
        simulationPane.getChildren().addAll(cellViews.stream().map(T::getView).collect(Collectors.toSet()));
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
            new Alert(Alert.AlertType.ERROR, myResources.getString("ErrorSave")).show();
        }
    }

    private void openHelp() {
        try {
            Desktop.getDesktop().browse(new URI("http://coursework.cs.duke.edu/CompSci308_2017Spring/cellsociety_team21"));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, myResources.getString("ErrorHelp")).show();
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
            String[][][] grid = new String[Integer.parseInt(root.getAttribute("width"))][Integer.parseInt(root.getAttribute("height"))][0];
            if (root.hasAttribute("fps")) {
                framesPerSecond = Double.parseDouble(root.getAttribute("fps"));
            }
            String simulationType = root.getAttribute("type");
            String shape = root.hasAttribute("shape") ? root.getAttribute("shape") : "Rectangle";
            String boundsType = root.hasAttribute("bounds") ? root.getAttribute("bounds") : "finite";
            Class<? extends Abstract_Cell> cellType = (Class<? extends Abstract_Cell>) Class.forName("CellSociety." + simulationType + "." + simulationType + "_Cell");
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
                    new Alert(Alert.AlertType.ERROR, myResources.getString("ErrorInstantiateClass") + " " + simulationType).show();
                    return null;
                }
            }
            return (SimulationGrid<? extends Abstract_Cell>) Class.forName("CellSociety.Grids." + shape + "_SimulationGrid").getConstructor(String[][][].class, Class.class, String.class).newInstance(grid, cellType, boundsType);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, myResources.getString("ErrorReadXML")).show();
            e.printStackTrace();
            return null;
        }
    }

    private T instantiateCellView(Abstract_Cell<? extends Abstract_Cell, ? extends Abstract_CellState> cell) {
        try {
            return (T) Class.forName("CellSociety.UI.CellView." + mySimulationGrid.getGridType() + "_CellView").getConstructor(Abstract_Cell.class).newInstance(cell);
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
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
