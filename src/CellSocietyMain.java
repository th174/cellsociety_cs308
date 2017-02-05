/**
 * Created by th174 on 1/29/2017.
 */

import CellSociety.UI.CellSocietyView;
import javafx.application.Application;
import javafx.stage.Stage;

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
