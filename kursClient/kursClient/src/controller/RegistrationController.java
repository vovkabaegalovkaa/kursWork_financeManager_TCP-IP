package controller;

import Client.Client;
import changeScene.ChangeSceneController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import view.AlertWindow;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationController extends ChangeSceneController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField nameField;
    @FXML
    private RadioButton maleRadioButton;
    @FXML
    private RadioButton femaleRadioButton;
    @FXML
    private TextField ageField;
    @FXML
    private Button registerButton;
    private String login;
    private String password;
    private String confirmPassword;
    private String name;
    private String gender;
    private String age;

    @FXML
    public void initialize() {
        ToggleGroup group = new ToggleGroup();
        maleRadioButton.setToggleGroup(group);
        femaleRadioButton.setToggleGroup(group);

        registerButton.setOnAction(event -> {
            login = usernameField.getText();
            password = passwordField.getText();
            confirmPassword = confirmPasswordField.getText();
            name = nameField.getText();
            age = ageField.getText();
            if(group.getSelectedToggle() == null)
                AlertWindow.showAlert("Ошибка получения данных", "Выберите пол!");
            else {
                gender = ((RadioButton) group.getSelectedToggle()).getText();
                try {
                    handleRegisterButton();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    public void handleRegisterButton() throws IOException {
        if (usernameField.getText().trim().isEmpty() || nameField.getText().trim().isEmpty() || ageField.getText().trim().isEmpty())
            AlertWindow.showAlert("Ошибка получения данных","Заполните все поля регистрации!");
        else {
            if(!ageField.getText().matches("\\d*"))
                AlertWindow.showAlert("Ошибка получения данных","Возраст должен иметь числовое значение!");
            else {
                if (password.length() < 4)
                    AlertWindow.showAlert("Придумайте другой пароль", "Пароль должен содержать не менее 4 символов!");
                else {
                    if (password.equals(confirmPassword)) {
                        Client.startConnection();
                        Map<String, String> map = new HashMap<>();
                        map.put("login", login);
                        map.put("password", password);
                        map.put("name", name);
                        map.put("gender", gender);
                        map.put("age", age);
                        List<Object> reciveData1 = List.of("CHECK_LOGIN",login);
                        Client.sendToServer(reciveData1);
                        List<Object> getData = Client.getFromServer();
                        Client.closeConnection();
                        if(!(boolean)getData.get(0))
                            AlertWindow.showAlert("Ошибка регистрации!", "Пользователь с таким логином уже существует!");
                        else {
                            List<Object> recievedData = List.of("REGISTR", map);
                            System.out.println("Пользователь зарегистрирован: " + login + ", " + name + ", " + gender + ", " + age);
                            Client.startConnection();
                            Client.sendToServer(recievedData);
                            Client.closeConnection();
                            this.changeScene(registerButton, "/view/Login.fxml");
                        }
                    } else {
                        // Пароли не совпадают
                        AlertWindow.showAlert("Пароли не совпадают!", "Проверьте правильность введенного пароля!");
                    }
                }
            }
        }
    }
}
