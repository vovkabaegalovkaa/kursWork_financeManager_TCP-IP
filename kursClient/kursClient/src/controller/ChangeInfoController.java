package controller;

import Client.Client;
import changeScene.ChangeSceneController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Family;
import model.GlobalData;
import model.Member;
import model.User;
import view.AlertWindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeInfoController extends ChangeSceneController {
    @FXML
    private TextField nameField;
    @FXML
    private RadioButton maleRadioButton;
    @FXML
    private RadioButton femaleRadioButton;
    @FXML
    private TextField ageField;
    @FXML
    private Button confirmButton;
    @FXML
    private Button backButton;
    private String name;
    private String gender;
    private String age;

    @FXML
    public void initialize() {
        ToggleGroup group = new ToggleGroup();
        maleRadioButton.setToggleGroup(group);
        femaleRadioButton.setToggleGroup(group);
        nameField.setText(GlobalData.getMember().getName());
        ageField.setText(GlobalData.getMember().getAge());
        confirmButton.setOnAction(event -> {
            name = nameField.getText();
            age = ageField.getText();
            if(group.getSelectedToggle() == null)
                AlertWindow.showAlert("Ошибка получения данных", "Выберите пол!");
            else {
                gender = ((RadioButton) group.getSelectedToggle()).getText();
                try {
                    handleConfirmButton();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    public void handleConfirmButton() throws IOException {
        if (nameField.getText().trim().isEmpty() || ageField.getText().trim().isEmpty())
            AlertWindow.showAlert("Ошибка получения данных","Заполните все поля регистрации!");
        else {
            if(!ageField.getText().matches("\\d*"))
                AlertWindow.showAlert("Ошибка получения данных","Возраст должен иметь числовое значение!");
            else {
                Client.startConnection();
                Map<String, String> map = new HashMap<>();
                map.put("member_id",Integer.toString(GlobalData.getMember().getIdMembers()));
                map.put("newName", name);
                map.put("newGender", gender);
                map.put("newAge", age);
                List<Object> recievedData = List.of("CHANGE_MEMBER_INFO", map);
                System.out.println("Пользователь зарегистрирован:" + name + ", " + gender + ", " + age);
                Client.sendToServer(recievedData);
                List<Object> getData = Client.getFromServer();
                GlobalData.setMember((Member) getData.get(0));
                GlobalData.setFamily((Family) getData.get(1));
                GlobalData.setFamilyMembers(FXCollections.observableArrayList((ArrayList<Member>) getData.get(2)));
                Client.closeConnection();
                this.changeScene(confirmButton,"/view/WorkScene.fxml");
            }
        }
    }
    public void handleBackButton() throws IOException {
        this.changeScene(backButton,"/view/WorkScene.fxml");
    }
}
