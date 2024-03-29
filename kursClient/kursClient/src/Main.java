import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application{

    private Stage primaryStage;
    private BorderPane rootLayout;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        this.primaryStage.setTitle("MyFinance");

        // Инициализация rootLayout
        rootLayout = new BorderPane();

        // Создание сцены с rootLayout
        Scene scene = new Scene(rootLayout, 600, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setHeight(600);
        primaryStage.setWidth(800);
        primaryStage.setResizable(false);

        showAuthorization();
    }
    public void showAuthorization(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/view/Login.fxml"));
            AnchorPane authorization = (AnchorPane) loader.load();
            rootLayout.setCenter(authorization);
            LoginController controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}