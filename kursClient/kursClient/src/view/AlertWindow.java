package view;

import javafx.scene.control.Alert;

public class AlertWindow {
    public static void showAlert(String title, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ошибка");
        alert.setHeaderText(title);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
