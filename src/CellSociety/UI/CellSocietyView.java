package CellSociety.UI;

import CellSociety.Abstract_Cell;
import CellSociety.Abstract_CellState;
import CellSociety.Grids.BoundsHandler;
import CellSociety.Grids.NeighborsGetter;
import CellSociety.Grids.SimulationGrid;
import CellSociety.Grids.SimulationGridImpl;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
    public static final double DEFAULT_SIZE = 900;
    private static final ResourceBundle myResources = ResourceBundle.getBundle(RESOURCES_LOCATION);
    private Timeline myAnimation;
    private Scene myScene;
    private SimulationGrid<? extends Abstract_Cell, ? extends Abstract_CellState> mySimulationGrid;
    private Collection<T> cellViews;
    private double windowWidth;
    private double windowHeight;
    private InputDataGetter myInputData;
    private double zoom;
    private LineChart myChart;
    private Collection<Series<Integer, Double>> mySeries;

    public CellSocietyView() {
        this(DEFAULT_SIZE, DEFAULT_SIZE);
    }

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
//        updateChart();
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
                myInputData = new xmlInput(xmlFile);
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
        simulationGroup.getChildren().addAll(cellViews.stream().map(T::getContent).collect(Collectors.toSet()));
        simulationPane.setContent(simulationGroup);
//        createChart();
        //simulationGroup.getChildren().add(myChart);
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

    private String generateXML() {
        final String[] xmlOutput = {"<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"};
        xmlOutput[0] += String.format("<Simulation type=\"%s\" width=\"%d\" height=\"%d\" fps=\"%f\" shape=\"%s\" neighbors_mode=\"%s\" bounds=\"%s\" outlines=\"%s\">\n",
                myInputData.getSimulationType(), mySimulationGrid.getColumns(), mySimulationGrid.getRows(), myInputData.getFramesPerSecond(), myInputData.getCellShape(), myInputData.getNeighborMode(), myInputData.getGridBoundType(), myInputData.getGridOutline());
        mySimulationGrid.forEach(e -> xmlOutput[0] += e.toString());
        xmlOutput[0] += "</Simulation>";
        return xmlOutput[0];
    }

//    private void createChart() {
//
//        final NumberAxis xAxis = new NumberAxis();
//        final NumberAxis yAxis = new NumberAxis();
//        xAxis.setAutoRanging(true);
//        xAxis.setLabel("Time");
//        yAxis.setLabel("Concentration of Cells");
//        ObservableMap<String, Double> cellConcentrations = mySimulationGrid.getCellConcentrations();
//        LineChart<Integer, Double> cellData = new LineChart(xAxis, yAxis);
//        cellData.setAnimated(true);
//
//        mySeries = new ArrayList<Series<Integer, Double>>();
//        System.out.println(cellConcentrations.keySet());
//        for (String state : cellConcentrations.keySet()) {
//
//            XYChart.Series<Integer, Double> series = new XYChart.Series<>();
//            series.setName(state);
//            cellData.getData().add(series);
//            mySeries.add(series);
//        }
//
//        myChart = cellData;
//    }

//    private void updateChart() {
//        ObservableMap<String, Double> cellConcentrations = mySimulationGrid.getCellConcentrations();
//        for (String state : cellConcentrations.keySet()) {
//            for (Series series : mySeries) {
//                if (state.equals(series.getName())) {//found a match
//                    series.getData().add(new XYChart.Data<Integer, Double>(myAnimation.getKeyFrames().size(),
//                            cellConcentrations.get(state)));
//                    //System.out.println("new x val  for state "+ state + " " +myAnimation.getKeyFrames().size());
//                    //System.out.println("new y val for state "+ state + " " + cellConcentrations.get(state));
//                }
//            }
//
//        }
//    }

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
        public String getSimulationType() {
            return simulationType;
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
                String[][][] grid;
                Class<? extends Abstract_Cell> cellType;
                try {
                    int width = root.hasAttribute("width") ? Integer.parseInt(root.getAttribute("width")) : 50;
                    int height = root.hasAttribute("height") ? Integer.parseInt(root.getAttribute("height")) : 50;
                    grid = new String[width][height][0];
                    framesPerSecond = root.hasAttribute("fps") ? Double.parseDouble(root.getAttribute("fps")) : 3;
                    simulationType = root.hasAttribute("type") ? root.getAttribute("type") : "GameOfLife";
                    cellShape = root.hasAttribute("shape") ? root.getAttribute("shape") : "Square";
                    boundsType = root.hasAttribute("bounds") ? root.getAttribute("bounds") : "Finite";
                    neighborMode = root.hasAttribute("neighbors") ? root.getAttribute("neighbors") : "";
                    gridOutlineStyle = root.hasAttribute("outlines") ? root.getAttribute("outlines") : "";
                    try {
                        cellType = (Class<? extends Abstract_Cell>) Class.forName("CellSociety." + simulationType + "." + simulationType + "_Cell");
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
                            } catch (IndexOutOfBoundsException e) {
                                e.printStackTrace();
                                throw new Exception(myResources.getString("IndexOutOfBoundsException") + "(x,y)=(" + currentElement.getAttribute("xPos") + "," + currentElement.getAttribute("yPos") + ")");
                            } catch (NumberFormatException e) {
                                throw new Exception(myResources.getString("CoordinateFormatException"));
                            }
                        }
                        try {
                            simulationGrid = new SimulationGridImpl<>(grid, cellType);
                            NeighborsGetter gridShape;
                            try {
                                gridShape = (NeighborsGetter) Class.forName("CellSociety.Grids.SimulationGridImpl$" + neighborMode + cellShape + "sGrid").getConstructor(SimulationGridImpl.class).newInstance(simulationGrid);
                                try {
                                    BoundsHandler gridBounds = (BoundsHandler) Class.forName("CellSociety.Grids.SimulationGridImpl$" + boundsType + "Bounds").getConstructor(SimulationGridImpl.class).newInstance(simulationGrid);
                                    simulationGrid.setShapeType(gridShape).setBoundsType(gridBounds);
                                } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
                                    throw new Exception(myResources.getString("BoundsTypeException") + boundsType);
                                }
                            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
                                e.printStackTrace();
                                throw new Exception(myResources.getString("ShapeException") + neighborMode + cellShape);
                            }
                        } catch (SimulationGrid.CellInstantiationException e) {
                            throw new Exception(myResources.getString("CellInstantiationException") + e.getMessage());
                        }
                    } catch (ClassNotFoundException e) {
                        throw new Exception(myResources.getString("ClassNotFoundException") + simulationType);
                    }
                } catch (NumberFormatException e) {
                    throw new Exception(myResources.getString("ConfigException"));
                }
            } catch (SAXException e) {
                throw new Exception(myResources.getString("XMLException"));
            } catch (IOException e) {
                throw new Exception(myResources.getString("IOException") + xmlInputFile.getName());
            }
        }

        private String[] getConstructorParamsFromXMLElement(Element currentElement) {
            java.util.List<String> constructorParams = new ArrayList<>();
            for (int j = 0; j < currentElement.getElementsByTagName("*").getLength(); j++) {
                constructorParams.add(currentElement.getElementsByTagName("*").item(j).getTextContent().toUpperCase());
            }
            return constructorParams.toArray(new String[constructorParams.size()]);
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

        private void setHueShift() {
            Dialog dbox = new Dialog();
            dbox.setTitle(myResources.getString("Set_Color"));
            dbox.setHeaderText(myResources.getString("Set_Color_Content"));
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
            Slider seekBar = new Slider(0, mySimulationGrid.getCurrentIndex(), mySimulationGrid.getMaxIndex());
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
            about.showAndWait();
        }
    }
}

