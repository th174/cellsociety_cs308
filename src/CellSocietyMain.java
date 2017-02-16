import CellSociety.UI.CellSocietyView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main Application Class.
 * <p>
 * Created by th174 on 1/29/2017.
 */
public class CellSocietyMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        CellSocietyView myUI = new CellSocietyView();
        primaryStage.setResizable(false);
        primaryStage.setScene(myUI.getScene());
        primaryStage.sizeToScene();
        primaryStage.setTitle(myUI.getTitle());
        primaryStage.show();
    }
}
