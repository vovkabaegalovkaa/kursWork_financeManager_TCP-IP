package controller;

import Client.Client;
import changeScene.ChangeSceneController;
import com.sun.scenario.effect.impl.sw.java.JSWPerspectiveTransformPeer;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.*;
import view.AlertWindow;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.*;

public class WorkSceneController extends ChangeSceneController {

    @FXML
    private Button addConfirm;

    @FXML
    private Button changeRoleKid;

    @FXML
    private Button leaveButton;

    @FXML
    private Button addLimitConfirm;

    @FXML
    private Button removeLimitConfirm;

    @FXML
    private TextField sumText;

    @FXML
    private TextField limitSumText;

    @FXML
    private TableColumn<Member, String > ageColumn;

    @FXML
    private Text budget;

    @FXML
    private Button changeInfo;

    @FXML
    private TableColumn<Operation, String> descriptionColumn;

    @FXML
    private TableColumn<Member, String> genderColumn;

    @FXML
    private TableColumn<Member, String> memberNameColumn;

    @FXML
    private Button quitConfirm;

    @FXML
    private TableColumn<Member, String> relationColumn;

    @FXML
    private Button removeConfirm;

    @FXML
    private TableColumn<Operation, Double> sumColumn;

    @FXML
    private TableView<Operation> tableHistory;

    @FXML
    private TableView<Member> tableMembers;

    @FXML
    private Text userAge;

    @FXML
    private Text userGender;

    @FXML
    private Text userName;

    @FXML
    private Text userRole;

    @FXML
    public void initialize() throws IOException {
        addConfirm.setDisable(true);
        removeConfirm.setDisable(true);
        removeLimitConfirm.setDisable(true);
        addLimitConfirm.setDisable(true);
        if(GlobalData.getMember().getRelation().equals("Родитель")){
            changeRoleKid.setDisable(false);
        }
        else{
            changeRoleKid.setDisable(true);
        }
        if(GlobalData.getUser().getLogin().equals(GlobalData.getFamily().getName())){
            leaveButton.setText("Найти семью");
        }
        else{
            leaveButton.setText("Покинуть семью");
        }
        budget.setText(Double.toString(GlobalData.getFamily().getBudget()));
        userName.setText(GlobalData.getMember().getName());
        userAge.setText(GlobalData.getMember().getAge());
        userRole.setText(GlobalData.getMember().getRelation());
        userGender.setText(GlobalData.getMember().getGender());
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        ObservableList<Operation> operations = GlobalData.getOperations();
        FXCollections.reverse(operations);
        tableHistory.setItems(operations);
        memberNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        relationColumn.setCellValueFactory(new PropertyValueFactory<>("relation"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        ObservableList<Member> members = GlobalData.getFamilyMembers();
        Iterator<Member> iterator = members.iterator();
        while (iterator.hasNext()) {
            Member m = iterator.next();
            if (GlobalData.getMember().equals(m)) {
                iterator.remove();
            }
        }
        tableMembers.setItems(members);
        TextFormatter<Double> textFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty()) {
                return change;
            }

            try {
                Double.parseDouble(newText);
                addConfirm.setDisable(false);
                removeConfirm.setDisable(false);
                return change;
            } catch (NumberFormatException e) {
                return null;
            }
        });
        sumText.setTextFormatter(textFormatter);
        TextFormatter<Double> textFormatter1 = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty()) {
                return change;
            }

            try {
                Double.parseDouble(newText);
                addLimitConfirm.setDisable(false);
                removeLimitConfirm.setDisable(false);
                return change;
            } catch (NumberFormatException e) {
                return null;
            }
        });
        limitSumText.setTextFormatter(textFormatter1);
    }
    public void handleQuitButton() throws IOException {
        this.changeScene(quitConfirm,"/view/Login.fxml");
    }
    public void handleChangeInfoButton() throws IOException {
        this.changeScene(quitConfirm,"/view/ChangeInfo.fxml");
    }
    public void handleAddConfirmButton() throws IOException {
        double sum = Double.parseDouble(sumText.getText());
        Client.startConnection();
        Map<String, String> map = new HashMap<>();
        map.put("member_id", Integer.toString(GlobalData.getMember().getIdMembers()));
        map.put("family_id", Integer.toString(GlobalData.getFamily().getIdFamilies()));
        map.put("sum", Double.toString(sum));
        map.put("category", "Пополнение");
        List<Object> sendData = List.of("ADD_MONEY", map);
        Client.sendToServer(sendData);
        Client.closeConnection();
        double newBudget = GlobalData.getFamily().getBudget() + sum;
        GlobalData.getFamily().setBudget(newBudget);
        budget.setText(Double.toString(newBudget));
        GlobalData.getOperations().add(new Operation(0, sum,"Пополнение",0));
        ObservableList<Operation> operations = GlobalData.getOperations();
        tableHistory.setItems(operations);
    }
    public void handleRemoveConfirmButton() throws IOException {
        double sum = Double.parseDouble(sumText.getText());
        if(GlobalData.getLimit().getQuantity()!=0 && sum > GlobalData.getLimit().getQuantity())
            AlertWindow.showAlert("Ошибка!", "Cумма затрат превышает лимит!");
        else {
            Client.startConnection();
            Map<String, String> map = new HashMap<>();
            map.put("member_id", Integer.toString(GlobalData.getMember().getIdMembers()));
            map.put("family_id", Integer.toString(GlobalData.getFamily().getIdFamilies()));
            map.put("sum", Double.toString(sum));
            map.put("category", "Траты");
            List<Object> sendData = List.of("REMOVE_MONEY", map);
            Client.sendToServer(sendData);
            Client.closeConnection();
            double newBudget = GlobalData.getFamily().getBudget() - sum;
            GlobalData.getFamily().setBudget(newBudget);
            budget.setText(Double.toString(newBudget));
            GlobalData.getOperations().add(new Operation(0, sum,"Траты",0));
            ObservableList<Operation> operations = GlobalData.getOperations();
            tableHistory.setItems(operations);
        }
    }
    public void sceneMouseClicked(javafx.scene.input.MouseEvent mouseEvent) {
        tableMembers.getSelectionModel().clearSelection();
        addLimitConfirm.setDisable(true);
        removeLimitConfirm.setDisable(true);
    }
    public void tableMouseClicked(javafx.scene.input.MouseEvent mouseEvent) {
        if (tableMembers.getSelectionModel().getSelectedItem() != null) {
            addLimitConfirm.setDisable(false);
            removeLimitConfirm.setDisable(false);
        }
    }
    public void handleAddLimit() throws IOException {
        if(tableMembers.getSelectionModel().getSelectedItem().getRelation().equals("Родитель")){
            AlertWindow.showAlert("Ошибка!", "Нельзя добавить лимит родителю!");
        }
        else {
            if (limitSumText.getText().trim().isEmpty()) {
                AlertWindow.showAlert("Ошибка!", "Введите сумму лимита!");
            } else {
                Map<String, String> map = new HashMap<>();
                map.put("limit_id", Integer.toString(tableMembers.getSelectionModel().getSelectedItem().getLimits_id()));
                map.put("quantity", limitSumText.getText());
                for (Limit l : GlobalData.getLimits()) {
                    if (tableMembers.getSelectionModel().getSelectedItem().getLimits_id() == l.getIdLimits())
                        l.setQuantity(Double.parseDouble(limitSumText.getText()));
                }
                List<Object> sendData = List.of("ADD_LIMIT", map);
                Client.startConnection();
                Client.sendToServer(sendData);
                Client.closeConnection();
                GlobalData.getLimit().setQuantity(Double.parseDouble(limitSumText.getText()));
                limitSumText.clear();
            }
        }
    }
    public void handleRemoveLimit() throws IOException {
        for (Limit l : GlobalData.getLimits()){
            if(tableMembers.getSelectionModel().getSelectedItem().getLimits_id() == l.getIdLimits()){
                if(l.getQuantity() == 0)
                    AlertWindow.showAlert("Ошибка!", "Для этого члена лимит не установлен!");
                else{
                    l.setQuantity(0);
                    Client.startConnection();
                    Map<String, String> map = new HashMap<>();
                    map.put("Limit_id", Integer.toString(l.getIdLimits()));
                    List<Object> sendData = List.of("DELETE_LIMIT", map);
                    Client.sendToServer(sendData);
                    Client.closeConnection();
                }
            }
        }
    }
    public void handleChangeRole() throws IOException {
        if (tableMembers.getSelectionModel().getSelectedItem() != null) {
            if (tableMembers.getSelectionModel().getSelectedItem().getRelation().equals("Родитель"))
                AlertWindow.showAlert("Ошибка!", "Вы не можете изменить роль родителя!");
            else {
                for(Limit l : GlobalData.getLimits()) {
                    if (tableMembers.getSelectionModel().getSelectedItem().getLimits_id() == l.getIdLimits())
                        if (l.getQuantity() != 0)
                            AlertWindow.showAlert("Ошибка!", "Удалите установленный лимит, прежде чем менять роль пользователя!");
                    else {
                        tableMembers.getSelectionModel().getSelectedItem().setRelation("Родитель");
                        tableMembers.refresh();
                        Map<String, String> map = new HashMap<>();
                        map.put("id_member", Integer.toString(tableMembers.getSelectionModel().getSelectedItem().getIdMembers()));
                        map.put("new_role", "Родитель");
                        List<Object> sendData = List.of("CHANGE_ROLE_MEMBER", map);
                        Client.startConnection();
                        Client.sendToServer(sendData);
                        Client.closeConnection();
                        }
                }
            }
        }
        else{
            GlobalData.getMember().setRelation("Ребенок");
            userRole.setText("Ребенок");
            for (Member m : GlobalData.getFamilyMembers()) {
                if (m.getIdMembers() == GlobalData.getMember().getIdMembers()) {
                    m.setRelation("Ребенок");
                }
            }
            Map<String, String> map = new HashMap<>();
            map.put("id_member", Integer.toString(GlobalData.getMember().getIdMembers()));
            map.put("new_role", "Ребенок");
            List<Object> sendData = List.of("CHANGE_ROLE_MEMBER", map);
            Client.startConnection();
            Client.sendToServer(sendData);
            Client.closeConnection();
            changeRoleKid.setDisable(true);
        }
    }
    public void handleLeaveButton() throws IOException {
        if(leaveButton.getText().equals("Покинуть семью")){
            for (Limit l : GlobalData.getLimits()){
                if(GlobalData.getMember().getLimits_id() == l.getIdLimits()){
                    l.setQuantity(0);
                    Client.startConnection();
                    Map<String, String> map = new HashMap<>();
                    map.put("Limit_id", Integer.toString(l.getIdLimits()));
                    List<Object> sendData = List.of("DELETE_LIMIT", map);
                    Client.sendToServer(sendData);
                    Client.closeConnection();
                }
            }
            Map<String, String> map = new HashMap<>();
            map.put("member_id", Integer.toString(GlobalData.getMember().getIdMembers()));
            map.put("new_family_name", GlobalData.getUser().getLogin());
            List<Object> sendData = List.of("LEAVE_FAMILY", map);
            Client.startConnection();
            Client.sendToServer(sendData);
            List<Object> getData = Client.getFromServer();
            GlobalData.setMember((Member) getData.get(0));
            GlobalData.setFamily((Family) getData.get(1));
            GlobalData.setFamilyMembers(FXCollections.observableArrayList((ArrayList<Member>) getData.get(2)));
            GlobalData.setOperations(FXCollections.observableArrayList((ArrayList<Operation>) getData.get(3)));
            GlobalData.setLimit((Limit) getData.get(4));
            GlobalData.setLimits((ArrayList<Limit>) getData.get(5));
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
            this.changeScene(leaveButton, "/view/WorkScene.fxml");
        }
        else{
            for (Limit l : GlobalData.getLimits()){
                if(GlobalData.getMember().getLimits_id() == l.getIdLimits()){
                    l.setQuantity(0);
                    Client.startConnection();
                    Map<String, String> map = new HashMap<>();
                    map.put("Limit_id", Integer.toString(l.getIdLimits()));
                    List<Object> sendData = List.of("DELETE_LIMIT", map);
                    Client.sendToServer(sendData);
                    Client.closeConnection();
                }
            }
            Map<String, String> map = new HashMap<>();
            map.put("member_id", Integer.toString(GlobalData.getMember().getIdMembers()));
            map.put("family_to_delete", Integer.toString(GlobalData.getFamily().getIdFamilies()));
            map.put("family_name", "vova");
            List<Object> sendData = List.of("JOIN_FAMILY", map);
            Client.startConnection();
            Client.sendToServer(sendData);
            List<Object> getData = Client.getFromServer();
            GlobalData.setMember((Member) getData.get(0));
            GlobalData.setFamily((Family) getData.get(1));
            GlobalData.setFamilyMembers(FXCollections.observableArrayList((ArrayList<Member>) getData.get(2)));
            GlobalData.setOperations(FXCollections.observableArrayList((ArrayList<Operation>) getData.get(3)));
            GlobalData.setLimit((Limit) getData.get(4));
            GlobalData.setLimits((ArrayList<Limit>) getData.get(5));
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
            this.changeScene(leaveButton, "/view/WorkScene.fxml");
        }
    }
}
