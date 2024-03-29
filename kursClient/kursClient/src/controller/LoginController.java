package controller;

import Client.Client;
import changeScene.ChangeSceneController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.*;
import view.AlertWindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginController extends ChangeSceneController {

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;
    @FXML
    private Button registrButton;

    private String login;
    private String password;

    @FXML
    public void initialize() throws IOException {
        loginButton.setOnAction(event -> {
            login = loginField.getText();
            password = passwordField.getText();
            try {
                handleLoginButton();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void handleLoginButton() throws IOException {
        Client.startConnection();
        Map<String, String> map = new HashMap<>();
        map.put("login", login);
        map.put("password", password);
        List<Object> recievedData = List.of("LOGIN", map);
        try {
            Client.sendToServer(recievedData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Object> getData = Client.getFromServer();
        if(getData.size()==1){
            AlertWindow.showAlert("Ошибка","Такого пользователя не существует");
            System.out.println(getData.get(0));
            Client.closeConnection();
        }
        else {
            GlobalData.setUser((User) getData.get(0));
            GlobalData.setMember((Member) getData.get(1));
            GlobalData.setFamily((Family) getData.get(2));
            GlobalData.setFamilyMembers(FXCollections.observableArrayList((ArrayList<Member>) getData.get(3)));
            GlobalData.setOperations(FXCollections.observableArrayList((ArrayList<Operation>) getData.get(4)));
            GlobalData.setLimit((Limit) getData.get(5));
            GlobalData.setLimits((ArrayList<Limit>) getData.get(6));
            System.out.println(GlobalData.getMember().toString());
            System.out.println(GlobalData.getUser().toString());
            System.out.println(GlobalData.getFamily().toString());
            for(Member m : GlobalData.getFamilyMembers()){
                System.out.println(m.toString());
            }
            for(Operation p : GlobalData.getOperations()){
                System.out.println(p.toString());
            }
            for(Limit l :GlobalData.getLimits()){
                System.out.println(l.toString()+"-------------");
            }
            System.out.println(GlobalData.getLimit().toString());
            Client.closeConnection();
            this.changeScene(loginButton,"/view/WorkScene.fxml");
        }

    }
    public void handleRegistrButton() throws IOException {
        this.changeScene(registrButton, "/view/Registration.fxml");
    }
}