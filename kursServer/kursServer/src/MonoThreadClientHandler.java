

import db.DBAdapter;
import model.*;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonoThreadClientHandler extends Thread {
    Socket clientDialog;
    DBAdapter dbAdapter = new DBAdapter();
    public MonoThreadClientHandler(Socket client) {
        this.clientDialog = client;
    }
    @Override
    public void run() {
        super.run();
        System.out.println("Подключение принято от " +
                clientDialog.getInetAddress().toString().substring(1) + ":"
                + clientDialog.getPort());
        Server.numConnections++;
        System.out.println("Клиентов подключено: " + Server.numConnections);
        try {

            ObjectOutputStream outputStream = new ObjectOutputStream(clientDialog.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(clientDialog.getInputStream());
            treatment(inputStream, outputStream);
            } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void treatment(ObjectInputStream inputStream, ObjectOutputStream outputStream) throws IOException, ClassNotFoundException, SQLException {
        List<Object> getData = (List<Object>) inputStream.readObject();
        String comand = getData.get(0).toString();
        System.out.println(comand);
        switch(comand){
            case "LOGIN":{
                Map<String, String> map = (Map<String, String>) getData.get(1);
                ArrayList<User> users = dbAdapter.getUsers();
                ArrayList<Member> members = new ArrayList<>();
                ArrayList<Operation> operations = new ArrayList<>();
                ArrayList<Limit> limits = new ArrayList<>();
                int count = 0;
                for(User u : users){
                    if(map.get("login").equals(u.getLogin()) && map.get("password").equals(u.getPassword())){
                        System.out.println(u.toString());
                        count++;
                        Member member = dbAdapter.searchMember(u.getMembers_Id());
                        System.out.println(member.toString());
                        Family family = dbAdapter.searchFamily(member.getFamilies_id());
                        System.out.println(family.toString());
                        members = dbAdapter.FamilyMembers(family.getIdFamilies());
                        Limit limit = dbAdapter.searchLimit(member.getLimits_id());
                        System.out.println(limit.toString());
                        for(int i = 0; i < members.size(); i++){
                            dbAdapter.searchOperation(members.get(i).getIdMembers(), operations);
                        }
                        for(Member m : members){
                            dbAdapter.searchLimit(m.getLimits_id(), limits);
                        }
                        List<Object> sendData = List.of(u, member, family, members, operations, limit, limits);
                        outputStream.writeObject(sendData);
                        outputStream.flush();
                        System.out.println("Data sent!");
                    }
                }
                if(count==0) {
                    List<Object> sendData = List.of("User doesnt exist!");
                    outputStream.writeObject(sendData);
                    outputStream.flush();
                    System.out.println("Data sent!");
                }
                clientDialog.close();
                Server.numConnections--;
                System.out.println("Клиент отключился: " +
                        clientDialog.getInetAddress().toString().substring(1) + ":"
                        + clientDialog.getPort());
                System.out.println("Клиентов подключено: " +
                        Server.numConnections);
                break;
            }
            case "REGISTR":{
                Map<String, String> map = (Map<String, String>) getData.get(1);
                Family family = new Family(0,map.get("login"),0);
                Member member = new Member(0,map.get("name"),map.get("gender"),map.get("age"),"Родитель",0,0);
                User user = new User(0,map.get("login"),map.get("password"),"Пользователь",0);
                dbAdapter.createLimit(0);
                dbAdapter.createFamily(family.getName());
                dbAdapter.createMember(member.getName(), member.getGender(), member.getAge(), member.getRelation());
                dbAdapter.createUser(user.getLogin(), user.getPassword(), user.getRole());
                clientDialog.close();
                Server.numConnections--;
                System.out.println("Клиент отключился: " +
                        clientDialog.getInetAddress().toString().substring(1) + ":"
                        + clientDialog.getPort());
                System.out.println("Клиентов подключено: " +
                        Server.numConnections);
                break;
            }
            case "CHANGE_MEMBER_INFO":{
                Map<String, String> map = (Map<String, String>) getData.get(1);
                Member member = new Member(Integer.parseInt(map.get("member_id")),map.get("newName"),map.get("newGender"),map.get("newAge"),"Родитель",0,0);
                dbAdapter.updateMemberInfo(member.getIdMembers(), member.getName(), member.getGender(), member.getAge());
                member = dbAdapter.searchMember(Integer.parseInt(map.get("member_id")));
                Family family = dbAdapter.searchFamily(member.getFamilies_id());
                ArrayList<Member> members = dbAdapter.FamilyMembers(family.getIdFamilies());
                List<Object> sendData = List.of(member, family, members);
                outputStream.writeObject(sendData);
                outputStream.flush();
                System.out.println("Data sent!");
                clientDialog.close();
                Server.numConnections--;
                System.out.println("Клиент отключился: " +
                        clientDialog.getInetAddress().toString().substring(1) + ":"
                        + clientDialog.getPort());
                System.out.println("Клиентов подключено: " +
                        Server.numConnections);
                break;
            }
            case "CHECK_LOGIN":{
                String login = (String) getData.get(1);
                List<Object> sendData = List.of(true);
                ArrayList<User> users = dbAdapter.getUsers();
                for(User u : users){
                    if(login.equals(u.getLogin()))
                        sendData = List.of(false);
                }
                outputStream.writeObject(sendData);
                outputStream.flush();
                System.out.println("Data sent!");
                clientDialog.close();
                Server.numConnections--;
                System.out.println("Клиент отключился: " +
                        clientDialog.getInetAddress().toString().substring(1) + ":"
                        + clientDialog.getPort());
                System.out.println("Клиентов подключено: " +
                        Server.numConnections);
                break;
            }
            case "ADD_MONEY":{
                Map<String, String> map = (Map<String, String>) getData.get(1);
                dbAdapter.createOperation(Integer.parseInt(map.get("member_id")), Double.parseDouble(map.get("sum")), map.get("category"));
                dbAdapter.updateFamilyInfoAdd(Integer.parseInt(map.get("family_id")), Double.parseDouble(map.get("sum")));
                clientDialog.close();
                Server.numConnections--;
                System.out.println("Клиент отключился: " +
                        clientDialog.getInetAddress().toString().substring(1) + ":"
                        + clientDialog.getPort());
                System.out.println("Клиентов подключено: " +
                        Server.numConnections);
                break;
            }
            case "REMOVE_MONEY":{
                Map<String, String> map = (Map<String, String>) getData.get(1);
                dbAdapter.createOperation(Integer.parseInt(map.get("member_id")), Double.parseDouble(map.get("sum")), map.get("category"));
                dbAdapter.updateFamilyInfoRemove(Integer.parseInt(map.get("family_id")), Double.parseDouble(map.get("sum")));
                clientDialog.close();
                Server.numConnections--;
                System.out.println("Клиент отключился: " +
                        clientDialog.getInetAddress().toString().substring(1) + ":"
                        + clientDialog.getPort());
                System.out.println("Клиентов подключено: " +
                        Server.numConnections);
                break;
            }
            case "ADD_LIMIT":{
                Map<String, String> map = (Map<String, String>) getData.get(1);
                dbAdapter.updateLimit(Integer.parseInt(map.get("limit_id")), Double.parseDouble(map.get("quantity")));
                clientDialog.close();
                Server.numConnections--;
                System.out.println("Клиент отключился: " +
                        clientDialog.getInetAddress().toString().substring(1) + ":"
                        + clientDialog.getPort());
                System.out.println("Клиентов подключено: " +
                        Server.numConnections);
                break;
            }
            case "DELETE_LIMIT":{
                Map<String, String> map = (Map<String, String>) getData.get(1);
                dbAdapter.updateLimit(Integer.parseInt(map.get("Limit_id")), 0);
                clientDialog.close();
                Server.numConnections--;
                System.out.println("Клиент отключился: " +
                        clientDialog.getInetAddress().toString().substring(1) + ":"
                        + clientDialog.getPort());
                System.out.println("Клиентов подключено: " +
                        Server.numConnections);
                break;
            }
            case "CHANGE_ROLE_MEMBER":{
                Map<String, String> map = (Map<String, String>) getData.get(1);
                dbAdapter.updateMemberInfo(Integer.parseInt(map.get("id_member")), map.get("new_role"));
                clientDialog.close();
                Server.numConnections--;
                System.out.println("Клиент отключился: " +
                        clientDialog.getInetAddress().toString().substring(1) + ":"
                        + clientDialog.getPort());
                System.out.println("Клиентов подключено: " +
                        Server.numConnections);
                break;
            }
            case "LEAVE_FAMILY":{
                Map<String, String> map = (Map<String, String>) getData.get(1);
                dbAdapter.createFamily(map.get("new_family_name"));
                ArrayList<Family> families = dbAdapter.getFamilies();
                int Families_id = families.get(families.size()-1).getIdFamilies();
                dbAdapter.updateMemberInfo(Integer.parseInt(map.get("member_id")), Families_id);
                Family family = dbAdapter.searchFamily(Families_id);
                Member member = dbAdapter.searchMember(Integer.parseInt(map.get("member_id")));
                ArrayList<Member> members = dbAdapter.FamilyMembers(Families_id);
                Limit limit = dbAdapter.searchLimit(member.getLimits_id());
                ArrayList<Limit> limits = new ArrayList<>();
                ArrayList<Operation> operations = new ArrayList<>();
                dbAdapter.deleteOperation(Integer.parseInt(map.get("member_id")));
                for(int i = 0; i < members.size(); i++){
                    dbAdapter.searchOperation(members.get(i).getIdMembers(), operations);
                }
                for(Member m : members){
                    dbAdapter.searchLimit(m.getLimits_id(), limits);
                }
                List<Object> sendData = List.of( member, family, members, operations, limit, limits);
                outputStream.writeObject(sendData);
                outputStream.flush();
                System.out.println("Data sent!");
                clientDialog.close();
                Server.numConnections--;
                System.out.println("Клиент отключился: " +
                        clientDialog.getInetAddress().toString().substring(1) + ":"
                        + clientDialog.getPort());
                System.out.println("Клиентов подключено: " +
                        Server.numConnections);
                break;
            }
            case "JOIN_FAMILY":{
                Map<String, String> map = (Map<String, String>) getData.get(1);
                Family family = dbAdapter.searchFamily(map.get("family_name"));
                dbAdapter.updateMemberInfo(Integer.parseInt(map.get("member_id")), family.getIdFamilies());
                dbAdapter.deleteFamily(Integer.parseInt(map.get("family_to_delete")));
                Member member = dbAdapter.searchMember(Integer.parseInt(map.get("member_id")));
                ArrayList<Member> members = dbAdapter.FamilyMembers(family.getIdFamilies());
                Limit limit = dbAdapter.searchLimit(member.getLimits_id());
                ArrayList<Limit> limits = new ArrayList<>();
                ArrayList<Operation> operations = new ArrayList<>();
                dbAdapter.deleteOperation(Integer.parseInt(map.get("member_id")));
                for(int i = 0; i < members.size(); i++){
                    dbAdapter.searchOperation(members.get(i).getIdMembers(), operations);
                }
                for(Member m : members){
                    dbAdapter.searchLimit(m.getLimits_id(), limits);
                }
                List<Object> sendData = List.of( member, family, members, operations, limit, limits);
                outputStream.writeObject(sendData);
                outputStream.flush();
                System.out.println("Data sent!");
                clientDialog.close();
                Server.numConnections--;
                System.out.println("Клиент отключился: " +
                        clientDialog.getInetAddress().toString().substring(1) + ":"
                        + clientDialog.getPort());
                System.out.println("Клиентов подключено: " +
                        Server.numConnections);
                break;
            }
        }
    }
}