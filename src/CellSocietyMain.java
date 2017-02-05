/**
 * Created by th174 on 1/29/2017.
 */

import CellSociety.Abstract_Cell;
import CellSociety.SimulationGrid;
import CellSociety.UI.CellSocietyView;
import javafx.application.Application;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

public class CellSocietyMain extends Application {
    public static final double SIZE = 900;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        CellSocietyView myUI = new CellSocietyView(SIZE, SIZE);
        primaryStage.setResizable(false);
        primaryStage.setScene(myUI.getScene());
        primaryStage.sizeToScene();
        primaryStage.setTitle(myUI.getTitle());
        primaryStage.show();
    }
}
