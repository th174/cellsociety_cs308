package CellSociety.UI;

import CellSociety.AbstractDiscrete_CellState;
import CellSociety.Abstract_Cell;
import CellSociety.Grids.SimulationGrid;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static javafx.application.Platform.exit;

/**
 * This class handles all graphical UI/UX for the CellSociety Simulation. It draws each of the cell views to the screen, updates the simulation state on a timeline, and allows for zooming and panning of the the simulation.
 * Additionally, the CellSocietyView.CellSocietyMenu class handles all user program interaction through the Menu bar.
 * <p>
 * Created by th174 on 1/31/2016.
 *
 * @param <T> The type of CellView to be drawn to be the simulation window
 */
public class CellSocietyView<T extends Abstract_CellView> {
    public static final boolean SYSTEM_MENU_BAR = true;
    public static final double ANIMATION_RATE_STEP = 5.0 / 4;
    public static final double ANIMATION_RATE_CAP = 15;
    public static final double ANIMATION_FRAMERATE = 60;
    public static final String RESOURCES_LOCATION = "resources/Strings";
    public static final double ZOOM_STEP = 11.0 / 10;
    public static final double DEFAULT_SIZE = 900;
    private static final ResourceBundle myResources = ResourceBundle.getBundle(RESOURCES_LOCATION);
    private Timeline myAnimation;
    private Scene myScene;
    private SimulationGrid<? extends Abstract_Cell, ? extends AbstractDiscrete_CellState> mySimulationGrid;
    private Collection<T> cellViews;
    private double windowWidth;
    private double windowHeight;
    private InputDataGetter myInputData;
    private double zoom;
    private Map<Enum, HashSet<Pair<Integer, Integer>>> cellFrequencyData;

    /**
     * Constructs a new CellSocietyView according to default size.
     */
    public CellSocietyView() {
        this(DEFAULT_SIZE, DEFAULT_SIZE);
    }

    /**
     * Constructs CellSocietyView according to the given parameters
     * Zoom is set to 1 by default.
     * Initializes and plays the Timeline.
     *
     * @param width  of the window
     * @param height of the window
     */
    public CellSocietyView(double width, double height) {
        windowWidth = width;
        windowHeight = height;
        zoom = 1;
        BorderPane myBorderPane = new BorderPane();
        myScene = new Scene(myBorderPane);
        myBorderPane.setTop(new CellSocietyMenu().getMenuBar());
        myBorderPane.setBottom(openNewFile());
        KeyFrame frame = new KeyFrame(Duration.seconds(1 / myInputData.getFramesPerSecond()), e -> update());
        cellFrequencyData = new HashMap<>();
        myAnimation = new Timeline(ANIMATION_FRAMERATE, frame);
        myAnimation.setCycleCount(Timeline.INDEFINITE);
        myAnimation.play();
    }

    /**
     * @return scene of the animation
     */
    public Scene getScene() {
        return myScene;
    }

    /**
     * @return Title of myResources
     */
    public String getTitle() {
        return myResources.getString("Title");
    }

    /**
     * Updates simulation state
     */
    private void update() {
        mySimulationGrid.update();
        cellViews.forEach(e -> e.updateView(mySimulationGrid.getColumns() * zoom, mySimulationGrid.getRows() * zoom, windowWidth, windowHeight));
        updateData();
    }

    private Node openNewFile() {
        ScrollPane simulationPane = new ScrollPane();
        simulationPane.setPrefSize(windowWidth, windowHeight);
        simulationPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(myResources.getString("Open_File"));
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Cell Society Data Files", "*.xml"));
        int attemptsRemaining = 3;
        do {
            try {
                File xmlFile = fileChooser.showOpenDialog(null);
                myInputData = new InputDataGetterXML(xmlFile, myResources);
            } catch (Exception e) {
                attemptsRemaining--;
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            }
            if (attemptsRemaining < 0) {
                exit();
                System.exit(-1);
            }
        } while (Objects.isNull(myInputData) && Objects.isNull(myAnimation));
        mySimulationGrid = myInputData.getSimulationGrid();
        cellViews = mySimulationGrid.parallelStream().map(this::instantiateCellView).filter(Objects::nonNull).collect(Collectors.toSet());
        Group simulationGroup = new Group();
        simulationGroup.getChildren().addAll(cellViews.stream().map(T::getView).collect(Collectors.toSet()));
        simulationPane.setContent(simulationGroup);
        return simulationPane;
    }

    private T instantiateCellView(Abstract_Cell<? extends Abstract_Cell, ? extends AbstractDiscrete_CellState> cell) {
        try {
            return (T) Class.forName("CellSociety.UI.Shapes." + myInputData.getCellShape()).getConstructor(Abstract_Cell.class, String.class).newInstance(cell, myInputData.getGridOutline());
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
    }

    private LineChart createChart() {
        int maxTime = mySimulationGrid.getMaxTimelineIndex();
        NumberAxis xAxis = new NumberAxis("Time", 0, maxTime, maxTime / 10);
        NumberAxis yAxis = new NumberAxis(0, mySimulationGrid.size(), mySimulationGrid.getColumns());
        xAxis.setLabel("Time");
        yAxis.setLabel("Number of cells");
        LineChart<Number, Number> cellSocietyChart = new LineChart<>(xAxis, yAxis);
        cellSocietyChart.setTitle("CellState frequency over time");
        for (Enum e : mySimulationGrid.getDistinctCellStates()) {
            Series<Number, Number> cellSeries = new Series<>();
            cellSeries.setName(e.name());
            cellSeries.getData().addAll(cellFrequencyData.get(e).stream().map(xy -> new XYChart.Data<Number, Number>(xy.getKey(), xy.getValue())).collect(Collectors.toSet()));
            cellSocietyChart.getData().add(cellSeries);
        }
        cellSocietyChart.setPrefHeight(600);
        cellSocietyChart.setPrefWidth(900);
        return cellSocietyChart;
    }

    private void updateData() {
        int currentTime = mySimulationGrid.getCurrentTimelineIndex();
        for (Enum e : mySimulationGrid.getDistinctCellStates()) {
            cellFrequencyData.putIfAbsent(e, new HashSet<>());
            cellFrequencyData.get(e).add(new Pair<>(currentTime, mySimulationGrid.countTotalOfState(e)));
        }
    }

    /**
     * Class representing the Menubar at the top of the window which contains all user interaction with the simulation.
     * This class handles all supported menu functions:
     * File
     * -New
     * -Open
     * -Save as XML
     * -Save as PNG
     * -Exit
     * View
     * -Zoom In
     * -Zoom Out
     * -Zoom Auto
     * -Adjust Color
     * Simulation
     * -Pause
     * -Speed Up
     * -Slow Down
     * -Reverse
     * -Step Forward
     * -Step Backward
     * -Seek
     * Help
     * -View Readme
     * -About
     *
     * @see javafx.scene.control.MenuBar
     */
    private class CellSocietyMenu {
        private MenuBar myMenu;

        /**
         * Constructs MenuBar used for the UI
         */
        public CellSocietyMenu() {
            Menu file = initFileMenu();
            Menu simulation = initSimulationMenu();
            Menu help = initHelpMenu();
            Menu view = initViewMenu();
            myMenu = new MenuBar(file, view, simulation, help);
            myMenu.setUseSystemMenuBar(SYSTEM_MENU_BAR);
        }

        /**
         * @return MenuBar
         * @see javafx.scene.control.MenuBar
         */
        public MenuBar getMenuBar() {
            return myMenu;
        }

        private Menu initFileMenu() {
            MenuItem newWindow = new MenuItem(myResources.getString("New..."));
            newWindow.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN));
            newWindow.setOnAction(e -> newSimulation());
            MenuItem open = new MenuItem(myResources.getString("Open..."));
            open.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));
            open.setOnAction(e -> ((BorderPane) myScene.getRoot()).setBottom(openNewFile()));
            MenuItem saveAs = new MenuItem(myResources.getString("Save_As"));
            saveAs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));
            saveAs.setOnAction(s -> saveAs());
            MenuItem exportPNG = new MenuItem(myResources.getString("Export_As"));
            exportPNG.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN));
            exportPNG.setOnAction(s -> exportPNG());
            MenuItem exit = new MenuItem(myResources.getString("Exit"));
            exit.setAccelerator(new KeyCodeCombination(KeyCode.ESCAPE, KeyCombination.SHORTCUT_DOWN));
            exit.setOnAction(s -> exit());
            return new Menu(myResources.getString("File"), null, newWindow, open, saveAs, exportPNG, exit);
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
            MenuItem viewGraph = new MenuItem(myResources.getString("View_Graph"));
            viewGraph.setAccelerator(new KeyCodeCombination(KeyCode.F12));
            viewGraph.setOnAction(e -> viewGraph());
            return new Menu(myResources.getString("Simulation"), null, viewGraph, pause, speedUp, slowDown, reverse, seek, stepForward, stepBackward, restart);
        }

        private Menu initHelpMenu() {
            MenuItem viewHelp = new MenuItem(myResources.getString("View_Readme"));
            viewHelp.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN));
            viewHelp.setOnAction(e -> openReadme());
            MenuItem viewJavadoc = new MenuItem(myResources.getString("View_Documentation"));
            viewJavadoc.setAccelerator(new KeyCodeCombination(KeyCode.J, KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN));
            viewJavadoc.setOnAction(e -> openJavadoc());
            MenuItem about = new MenuItem(myResources.getString("About"));
            about.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN));
            about.setOnAction(e -> about());
            return new Menu(myResources.getString("Help"), null, viewHelp, viewJavadoc, about);
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
            MenuItem colorShift = new MenuItem(myResources.getString("Adjust_Color"));
            colorShift.setOnAction(e -> colorShift());
            return new Menu(myResources.getString("View"), null, zoomAuto, zoomIn, zoomOut, colorShift);
        }

        private void newSimulation() {
            Stage newStage = new Stage();
            CellSocietyView newUI = new CellSocietyView();
            newStage.setResizable(false);
            newStage.setScene(newUI.getScene());
            newStage.sizeToScene();
            newStage.setTitle(newUI.getTitle());
            newStage.show();
        }

        private void exit() {
            Platform.exit();
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

        private void colorShift() {
            Dialog dbox = new Dialog();
            dbox.setTitle(myResources.getString("Adjust_Color"));
            dbox.setHeaderText(myResources.getString("Adjust_Color_Content"));
            dbox.getDialogPane().getButtonTypes().add(new ButtonType(myResources.getString("Okay"), ButtonBar.ButtonData.CANCEL_CLOSE));
            GridPane content = new GridPane();
            content.getColumnConstraints().add(new ColumnConstraints(90));
            ColumnConstraints slidersColumn = new ColumnConstraints();
            slidersColumn.setHgrow(Priority.ALWAYS);
            content.getColumnConstraints().add(slidersColumn);
            Slider hSlider = new Slider(0, 360, 0);
            hSlider.valueProperty().addListener(e -> cellViews.forEach(f -> f.setHueShift(hSlider.getValue())));
            content.add(new Label(myResources.getString("Hue")), 0, 0);
            content.add(hSlider, 1, 0);
            Slider sSlider = new Slider(0, 1, 1);
            sSlider.valueProperty().addListener(e -> cellViews.forEach(f -> f.setSaturationShift(sSlider.getValue())));
            content.add(new Label(myResources.getString("Saturation")), 0, 1);
            content.add(sSlider, 1, 1);
            Slider lSlider = new Slider(0, 2, 1);
            lSlider.valueProperty().addListener(e -> cellViews.forEach(f -> f.setLightnessShift(lSlider.getValue())));
            content.add(new Label(myResources.getString("Lightness")), 0, 2);
            content.add(lSlider, 1, 2);
            dbox.getDialogPane().setContent(content);
            dbox.showAndWait();
        }

        private void viewGraph() {
            Dialog dbox = new Dialog();
            dbox.setTitle(myResources.getString("View_Graph"));
            dbox.setHeaderText(myResources.getString("View_Graph_Content"));
            dbox.getDialogPane().getButtonTypes().add(new ButtonType(myResources.getString("Okay"), ButtonBar.ButtonData.CANCEL_CLOSE));
            dbox.getDialogPane().setContent(createChart());
            dbox.showAndWait();
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
            Dialog dbox = new Dialog();
            dbox.setTitle(myResources.getString("Seek..."));
            dbox.setHeaderText(myResources.getString("Seek_Content"));
            dbox.getDialogPane().getButtonTypes().add(new ButtonType(myResources.getString("Okay"), ButtonBar.ButtonData.CANCEL_CLOSE));
            Slider seekBar = new Slider(0, mySimulationGrid.getCurrentTimelineIndex(), mySimulationGrid.getMaxTimelineIndex());
            seekBar.setSnapToTicks(true);
            seekBar.setMajorTickUnit(1);
            seekBar.valueProperty().addListener(e -> {
                seek((int) seekBar.getValue());
            });
            dbox.getDialogPane().setContent(seekBar);
            dbox.showAndWait();
        }

        private void seek(int time) {
            mySimulationGrid.forEach(e -> e.seek(time));
        }

        private void reverse() {
            mySimulationGrid.forEach(Abstract_Cell::reverse);
        }

        private void saveAs() {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(myResources.getString("Export_File"));
            fileChooser.setInitialFileName("CellSociety_" + myInputData.getSimulationType() + "_State1.xml");
            fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Extensible Markup Language File", "*.xml"));
            File output = fileChooser.showSaveDialog(null);
            try (FileWriter writer = new FileWriter(output)) {
                writer.write(generateXML());
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, myResources.getString("ErrorSave")).show();
            }
        }

        private String generateXML() {
            final String[] xmlOutput = {"<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"};
            xmlOutput[0] += String.format("<Simulation type=\"%s\" width=\"%d\" height=\"%d\" fps=\"%f\" shape=\"%s\" neighbors_mode=\"%s\" bounds=\"%s\" outlines=\"%s\">\n",
                    myInputData.getSimulationType(), mySimulationGrid.getColumns(), mySimulationGrid.getRows(), myInputData.getFramesPerSecond(), myInputData.getCellShape(), myInputData.getNeighborMode(), myInputData.getGridBoundType(), myInputData.getGridOutline());
            mySimulationGrid.forEach(e -> xmlOutput[0] += e.toString());
            xmlOutput[0] += "</Simulation>";
            return xmlOutput[0];
        }

        private void exportPNG() {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(myResources.getString("Export_File"));
            fileChooser.setInitialFileName("CellSocietyScreenshot1.png");
            fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Portable Network Graphics", "*.png"));
            File output = fileChooser.showSaveDialog(null);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(myScene.snapshot(null), null), "png", output);
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, myResources.getString("ErrorSave")).show();
            }
        }

        private void openReadme() {
            try {
                Desktop.getDesktop().browse(new URI("http://coursework.cs.duke.edu/CompSci308_2017Spring/cellsociety_team21"));
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, myResources.getString("ErrorReadme")).show();
            }
        }

        private void openJavadoc() {
            try {
                Desktop.getDesktop().browse(new URI("file://" + System.getProperty("user.dir").replace('\\', '/') + "/doc/index.html"));
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, myResources.getString("ErrorDocumentation")).show();
            }
        }

        private void about() {
            Alert about = new Alert(Alert.AlertType.INFORMATION);
            about.setHeaderText(myResources.getString("CellSociety"));
            about.setTitle(myResources.getString("About"));
            about.setContentText(myResources.getString("AboutContent"));
            about.showAndWait();
        }
    }
}

