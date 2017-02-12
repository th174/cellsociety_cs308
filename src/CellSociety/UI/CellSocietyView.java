package CellSociety.UI;

import CellSociety.Abstract_Cell;
import CellSociety.Abstract_CellState;
import CellSociety.Grids.BoundsHandler;
import CellSociety.Grids.NeighborsGetter;
import CellSociety.Grids.SimulationGrid;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
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

import static javafx.application.Platform.exit;


public class CellSocietyView<T extends Abstract_CellView> {
    public static final boolean SYSTEM_MENU_BAR = true;
    public static final double ANIMATION_RATE_STEP = 5.0 / 4;
    public static final double ANIMATION_RATE_CAP = 15;
    public static final double ANIMATION_FRAMERATE = 60;
    public static final String RESOURCES_LOCATION = "resources/Strings";
    public static final double ZOOM_STEP = 11.0 / 10;
    private static final ResourceBundle myResources = ResourceBundle.getBundle(RESOURCES_LOCATION);
    private Timeline myAnimation;
    private Scene myScene;
    private SimulationGrid<? extends Abstract_Cell, ? extends Abstract_CellState> mySimulationGrid;
    private Collection<T> cellViews;
    private double windowWidth;
    private double windowHeight;
    private InputDataGetter myInputData;
    private double zoom;

    public CellSocietyView(double width, double height) {
        this(width, height, null);
    }

    public CellSocietyView(double width, double height, InputDataGetter inputData) {
        myInputData = inputData;
        windowWidth = width;
        windowHeight = height;
        zoom = 1;
        BorderPane myBorderPane = new BorderPane();
        myScene = new Scene(myBorderPane);
        myBorderPane.setTop(new CellSocietyMenu().getMenuBar());
        myBorderPane.setBottom(openNewFile());
        KeyFrame frame = new KeyFrame(Duration.seconds(1 / myInputData.getFramesPerSecond()), e -> update());
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
        cellViews.forEach(e -> e.updateView(mySimulationGrid.getColumns() * zoom, mySimulationGrid.getRows() * zoom, windowWidth, windowHeight));
    }

    private Node openNewFile() {
        ScrollPane simulationPane = new ScrollPane();
        simulationPane.setPrefSize(windowWidth, windowHeight);
        simulationPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(myResources.getString("Open_File"));
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Cell Society Data Files", "*.xml"));
        File xmlFile = fileChooser.showOpenDialog(null);
        if (Objects.nonNull(xmlFile)) {
            try {
                myInputData = new xmlInput(xmlFile);
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, myResources.getString("ErrorReadXML")).show();
                try {
                    myInputData = new xmlInput(xmlFile);
                } catch (Exception e1) {
                    new Alert(Alert.AlertType.ERROR, myResources.getString("ErrorReadXML")).show();
                    try {
                        myInputData = new xmlInput(xmlFile);
                    } catch (Exception e2) {
                        new Alert(Alert.AlertType.ERROR, myResources.getString("ErrorReadXML")).show();
                        exit();
                    }
                }
            }
            mySimulationGrid = myInputData.getSimulationGrid();
            cellViews = mySimulationGrid.parallelStream().map(this::instantiateCellView).filter(Objects::nonNull).collect(Collectors.toSet());
        }
        Group simulationGroup = new Group();
        simulationGroup.getChildren().addAll(cellViews.stream().map(T::getView).collect(Collectors.toSet()));
        simulationPane.setContent(simulationGroup);
        return simulationPane;
    }

    private T instantiateCellView(Abstract_Cell<? extends Abstract_Cell, ? extends Abstract_CellState> cell) {
        try {
            return (T) Class.forName("CellSociety.UI.Shapes." + myInputData.getCellShape()).getConstructor(Abstract_Cell.class, String.class).newInstance(cell, myInputData.getGridOutline());
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
    }

    private class CellSocietyMenu {
        private MenuBar myMenu;

        public CellSocietyMenu() {
            Menu file = initFileMenu();
            Menu simulation = initSimulationMenu();
            Menu help = initHelpMenu();
            Menu view = initViewMenu();
            myMenu = new MenuBar(file, view, simulation, help);
            myMenu.setUseSystemMenuBar(SYSTEM_MENU_BAR);
        }

        public MenuBar getMenuBar() {
            return myMenu;
        }

        private Menu initFileMenu() {
            MenuItem open = new MenuItem(myResources.getString("Open..."));
            open.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));
            open.setOnAction(e -> ((BorderPane) myScene.getRoot()).setBottom(openNewFile()));
            MenuItem save = new MenuItem(myResources.getString("Save"));
            save.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));
            save.setOnAction(s -> save());
            MenuItem exit = new MenuItem(myResources.getString("Exit"));
            exit.setAccelerator(new KeyCodeCombination(KeyCode.ESCAPE, KeyCombination.SHORTCUT_DOWN));
            exit.setOnAction(s -> exit());
            return new Menu(myResources.getString("File"), null, open, save, exit);
        }

        private Menu initSimulationMenu() {
            MenuItem pause = new MenuItem(myResources.getString("Pause"));
            pause.setAccelerator(new KeyCodeCombination(KeyCode.SPACE));
            pause.setOnAction(e -> pause.setText(myResources.getString(pause() ? myResources.getString("Unpause") : myResources.getString("Pause"))));
            MenuItem restart = new MenuItem(myResources.getString("Restart"));
            restart.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN));
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
            stepForward.setAccelerator(new KeyCodeCombination(KeyCode.EQUALS, KeyCombination.SHORTCUT_DOWN));
            stepForward.setOnAction(e -> stepForward());
            MenuItem stepBackward = new MenuItem(myResources.getString("Step_Backward"));
            stepBackward.setAccelerator(new KeyCodeCombination(KeyCode.MINUS, KeyCombination.SHORTCUT_DOWN));
            stepBackward.setOnAction(e -> stepBackward());
            return new Menu(myResources.getString("Simulation"), null, pause, speedUp, slowDown, reverse, seek, stepForward, stepBackward, restart);
        }

        private Menu initHelpMenu() {
            MenuItem viewHelp = new MenuItem(myResources.getString("View_Help"));
            viewHelp.setAccelerator(new KeyCodeCombination(KeyCode.F1));
            viewHelp.setOnAction(e -> openHelp());
            MenuItem about = new MenuItem(myResources.getString("About"));
            about.setAccelerator(new KeyCodeCombination(KeyCode.F1, KeyCombination.SHORTCUT_DOWN));
            about.setOnAction(e -> about());
            return new Menu(myResources.getString("Help"), null, viewHelp, about);
        }

        private Menu initViewMenu() {
            MenuItem zoomAuto = new MenuItem(myResources.getString("Zoom_Auto"));
            zoomAuto.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT0, KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN));
            zoomAuto.setOnAction(e -> zoomAuto());
            MenuItem zoomIn = new MenuItem(myResources.getString("Zoom_In"));
            zoomIn.setAccelerator(new KeyCodeCombination(KeyCode.EQUALS, KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN));
            zoomIn.setOnAction(e -> zoomIn());
            MenuItem zoomOut = new MenuItem(myResources.getString("Zoom_Out"));
            zoomOut.setAccelerator(new KeyCodeCombination(KeyCode.MINUS, KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN));
            zoomOut.setOnAction(e -> zoomOut());
            MenuItem colorShift = new MenuItem(myResources.getString("Set_Color"));
            colorShift.setOnAction(e -> setHueShift());
            return new Menu(myResources.getString("View"), null, zoomAuto, zoomIn, zoomOut,colorShift);
        }

        private void exit() {
            exit();
            System.exit(0);
        }

        private void zoomIn() {
            zoom /= ZOOM_STEP;
        }

        private void zoomOut() {
            zoom *= ZOOM_STEP;
        }

        private void zoomAuto() {
            zoom = 1;
        }

        private void setHueShift(){
            TextInputDialog dbox = new TextInputDialog();
            dbox.setHeaderText(myResources.getString("Set_Color"));
            dbox.setContentText(myResources.getString("Set_Color_Content"));
            int shift = Integer.parseInt(dbox.showAndWait().orElse(0 + ""));
            cellViews.forEach(e->e.setHueShift(shift));
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
            dbox.setContentText(myResources.getString("Seek_Content"));
            seek(Integer.parseInt(dbox.showAndWait().orElse(-1 + "")));
        }

        private void seek(int time) {
            mySimulationGrid.forEach(e -> e.seek(time));
        }

        private void reverse() {
            mySimulationGrid.forEach(Abstract_Cell::reverse);
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
    }

    private static class xmlInput implements InputDataGetter {
        private double framesPerSecond;
        private String simulationType;
        private String cellShape;
        private String boundsType;
        private String neighborMode;
        private String gridOutlineStyle;
        private SimulationGrid<? extends Abstract_Cell, ? extends Abstract_CellState> simulationGrid;

        public xmlInput(File xmlFile) throws Exception {
            readXML(xmlFile);
        }

        @Override
        public SimulationGrid<? extends Abstract_Cell, ? extends Abstract_CellState> getSimulationGrid() {
            return simulationGrid;
        }

        @Override
        public double getFramesPerSecond() {
            return framesPerSecond;
        }

        @Override
        public String getCellShape() {
            return cellShape;
        }

        @Override
        public String getGridBoundType() {
            return boundsType;
        }

        @Override
        public String getNeighborMode() {
            return neighborMode;
        }

        @Override
        public String getGridOutline() {
            return gridOutlineStyle;
        }

        private void readXML(File xmlInputFile) throws Exception {
            try {
                Document file = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlInputFile);
                Element root = file.getDocumentElement();
                int width = root.hasAttribute("width") ? Integer.parseInt(root.getAttribute("width")) : 50;
                int height = root.hasAttribute("height") ? Integer.parseInt(root.getAttribute("height")) : 50;
                framesPerSecond = root.hasAttribute("fps") ? Double.parseDouble(root.getAttribute("fps")) : 3;
                simulationType = root.hasAttribute("type") ? root.getAttribute("type") : "GameOfLife";
                cellShape = root.hasAttribute("shape") ? root.getAttribute("shape") : "Square";
                boundsType = root.hasAttribute("bounds") ? root.getAttribute("bounds") : "Finite";
                neighborMode = root.hasAttribute("neighbors_mode") ? root.getAttribute("neighbors_mode") : "";
                gridOutlineStyle = root.hasAttribute("outlines") ? root.getAttribute("outlines") : "";
                String[][][] grid = new String[width][height][0];
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
                        throw new Exception(myResources.getString("ErrorReadXML"));
                    }
                }
                simulationGrid = new SimulationGrid<>(grid, cellType);
                NeighborsGetter gridShape = (NeighborsGetter) Class.forName("CellSociety.Grids.SimulationGrid$" + neighborMode + cellShape + "sGrid").getConstructor(SimulationGrid.class).newInstance(simulationGrid);
                BoundsHandler gridBounds = (BoundsHandler) Class.forName("CellSociety.Grids.SimulationGrid$" + boundsType + "Bounds").getConstructor(SimulationGrid.class).newInstance(simulationGrid);
                simulationGrid.setShapeType(gridShape).setBoundsType(gridBounds);
            } catch (Exception e) {
                throw new Exception(myResources.getString("ErrorReadXML"));
            }
        }

        private String[] getConstructorParamsFromXMLElement(Element currentElement) throws Exception {
            java.util.List<String> constructorParams = new ArrayList<>();
            for (int j = 0; j < currentElement.getElementsByTagName("*").getLength(); j++) {
                constructorParams.add(currentElement.getElementsByTagName("*").item(j).getTextContent().toUpperCase());
            }
            return constructorParams.toArray(new String[constructorParams.size()]);
        }
    }
}
