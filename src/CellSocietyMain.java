/**
 * Created by th174 on 1/29/2017.
 */

import CellSociety.UI.CellSocietyView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CellSocietyMain extends Application {
	private Scene myScene;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        CellSocietyView myUI = new CellSocietyView();
        primaryStage.setResizable(false);
        primaryStage.setScene(myUI.myScene);
        primaryStage.sizeToScene();
        primaryStage.setTitle(myUI. myResources.getString("Title"));
        primaryStage.show();
    }
}
