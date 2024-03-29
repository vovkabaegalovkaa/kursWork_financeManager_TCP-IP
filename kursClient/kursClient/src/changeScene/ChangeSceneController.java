package changeScene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChangeSceneController {
    public void changeScene(Node node, String path) throws IOException {
        Stage window = (Stage) node.getScene().getWindow();
        Parent parent = FXMLLoader.load(getClass().getResource(path));
        Scene scene = new Scene(parent);
        window.setScene(scene);     }
}
