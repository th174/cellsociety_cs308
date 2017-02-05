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

import javax.imageio.ImageIO;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;


public class CellSocietyView {
    public static final boolean SYSTEM_MENU_BAR = true;
    public static final double ANIMATION_RATE_STEP = 5.0 / 4;
    public static final double ANIMATION_RATE_CAP = 12;
    public static final double ANIMATION_FRAMERATE = 60;
    public static final String RESOURCES_LOCATION = "resources/Menu";
    public static final int MENU_HEIGHT = 12;
    private ResourceBundle myResources;
    private Timeline myAnimation;
    private Scene myScene;
    private SimulationGrid<? extends Abstract_Cell> mySimulationGrid;
    private double windowWidth;
    private double windowHeight;

    public CellSocietyView(SimulationGrid<? extends Abstract_Cell> simulationGrid, double framesPerSecond, double width, double height) {
        mySimulationGrid = simulationGrid;
        windowWidth = width;
        windowHeight = height;
        myResources = ResourceBundle.getBundle(RESOURCES_LOCATION);
        Pane simulationPane = new Pane();
        simulationPane.setPrefSize(windowWidth, windowHeight);
        simulationPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        BorderPane myBorderPane = new BorderPane();
        myBorderPane.setTop(initMenu());
        myBorderPane.setBottom(simulationPane);
        myScene = new Scene(myBorderPane);
        Set<CellView> myCellViews = mySimulationGrid.asCollection().stream().map(e -> new CellView(e, this)).collect(Collectors.toSet());
        simulationPane.getChildren().addAll(myCellViews);
        myScene.setOnKeyPressed(this::handleKeyPress);
        KeyFrame frame = new KeyFrame(Duration.seconds(1 / framesPerSecond), e -> update(myCellViews));
        myAnimation = new Timeline(ANIMATION_FRAMERATE, frame);
        myAnimation.setCycleCount(Timeline.INDEFINITE);
        myAnimation.play();
    }

    public Scene getScene() {
        return myScene;
    }

    private void update(Set<CellView> cellViews) {
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
        open.setOnAction(e -> open());
        MenuItem save = new MenuItem(myResources.getString("Save"));
        save.setOnAction(s -> save());
        MenuItem exit = new MenuItem(myResources.getString("Exit"));
        exit.setOnAction(s -> exit());
        file.getItems().addAll(open, save, exit);
        Menu simulation = new Menu(myResources.getString("Simulation"));
        MenuItem pause = new MenuItem(myResources.getString("Pause"));
        pause.setOnAction(e -> {
            pause();
            pause.setText(myResources.getString("Unpause"));
        });
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

    private void pause() {
        if (myAnimation.getStatus().equals(Animation.Status.PAUSED)) {
            myAnimation.play();
        } else {
            myAnimation.pause();
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

    private void open() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open XML");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Cell Society Data Files", "*.xml"));
        File newXML = fileChooser.showOpenDialog(null);
        //TODO: this
    }

    private void save() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Simulation State");
        fileChooser.setInitialFileName("CellSocietyScreenshot1.png");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Portable Network Graphics", "*.png"));
        File output = fileChooser.showSaveDialog(null);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(myScene.snapshot(null), null), "png", output);
        } catch (IOException e) {
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
}
