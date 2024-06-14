package db;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBAdapter {
    static Connection c;
    static Statement statement;



    public static void getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        c = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb","root","*******");
        statement = c.createStatement();

    }
    static {
        try {
            getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void createUser(String userName, String password, String role) throws SQLException, ClassNotFoundException {
        ArrayList<Member> members = getMembers();
        int Members_id = members.get(members.size()-1).getIdMembers();
        String insert = "INSERT INTO users (login, password, role, Members_id) VALUES ('"+userName+"', '"+password+"', '"+role+"', '"+Members_id+"')";
        int rowsInserted = statement.executeUpdate(insert);
        if (rowsInserted > 0) {
            System.out.println("A new user was inserted successfully!");
        }
    }
    public void createMember(String memberName, String gender, String age, String relation) throws SQLException, ClassNotFoundException {
        ArrayList<Family> families = getFamilies();
        int Families_id = families.get(families.size()-1).getIdFamilies();
        ArrayList<Limit> limits = getLimits();
        int limit_id = limits.get(limits.size()-1).getIdLimits();
        String insert = "INSERT INTO members (name, gender, age, relation, Families_id, Limits_id) VALUES ('"+memberName+"', '"+gender+"', '"+Integer.parseInt(age)+"', '"+relation+"', '"+Families_id+"', '"+limit_id+"')";
        int rowsInserted = statement.executeUpdate(insert);
        if (rowsInserted > 0) {
            System.out.println("A new user was inserted successfully!");
        }
    }
    public void createFamily(String familyName) throws SQLException {
        String insert = "INSERT INTO families (name, budget) VALUES ('"+familyName+"', '"+0+"')";
        int rowsInserted = statement.executeUpdate(insert);
        if (rowsInserted > 0) {
            System.out.println("A new family was inserted successfully!");
        }
    }
    public void createOperation(int id_Members, double sum, String category) throws SQLException {
        String insert = "INSERT INTO operations (quantity, category, id_Members) VALUES ('"+sum+"', '"+category+"', '"+id_Members+"')";
        int rowsInserted = statement.executeUpdate(insert);
        if (rowsInserted > 0) {
            System.out.println("A new operation was inserted successfully!");
        }
    }
    public void deleteOperation(int id_Members) throws SQLException {
        String insert = "DELETE FROM operations WHERE id_Members = '"+id_Members+"';";
        int rowsInserted = statement.executeUpdate(insert);
        if (rowsInserted > 0) {
            System.out.println("A operation was deleted successfully!");
        }
    }
    public void deleteFamily(int id_family) throws SQLException {
        String insert = "DELETE FROM families WHERE idFamilies = '"+id_family+"';";
        int rowsInserted = statement.executeUpdate(insert);
        if (rowsInserted > 0) {
            System.out.println("A family was deleted successfully!");
        }
    }
    public void createLimit(double sum) throws SQLException {
        String insert = "INSERT INTO limits (quantity) VALUES ('"+sum+"')";
        int rowsInserted = statement.executeUpdate(insert);
        if (rowsInserted > 0) {
            System.out.println("A new limit was inserted successfully!");
        }
    }
    public void searchOperation(int member_Id, ArrayList<Operation> operations) throws SQLException {
        String sql = "SELECT * FROM operations WHERE id_Members='"+member_Id+"';";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            int operation_id = rs.getInt("idOperations");
            double quantity = rs.getDouble("quantity");
            String category = rs.getString("category");
            int id_Members = rs.getInt("id_Members");
            Operation operation = new Operation(operation_id, quantity, category, id_Members);
            operations.add(operation);
        }
    }
    public void searchLimit(int limit_Id, ArrayList<Limit> limits) throws SQLException {
        String sql = "SELECT * FROM limits WHERE idLimits='"+limit_Id+"';";
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()) {
            double quantity = rs.getDouble("quantity");
            Limit limit = new Limit(limit_Id, quantity);
            limits.add(limit);
        }
    }
    public void updateLimit(int limit_id, double quantity) throws SQLException {
        String sql = "UPDATE limits SET quantity = '"+quantity+"' WHERE idLimits = "+limit_id+";";
        int rowsUpdated = statement.executeUpdate(sql);
        if (rowsUpdated > 0) {
            System.out.println("Обновление успешно выполнено!");
        }
    }
    public ArrayList<User> getUsers() throws SQLException, ClassNotFoundException {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()){
            int idUsers = rs.getInt("idUsers");
            String login = rs.getString("login");
            String password = rs.getString("password");
            String role = rs.getString("role");
            int Members_Id = rs.getInt("Members_Id");
            User user = new User(idUsers, login, password, role, Members_Id);
            users.add(user);
        }
        return users;
    }
    public ArrayList<Family> getFamilies() throws SQLException, ClassNotFoundException {
        ArrayList<Family> families = new ArrayList<>();
        String sql = "SELECT * FROM families";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()){
            int idFamily = rs.getInt("idFamilies");
            String name = rs.getString("name");
            double budget = rs.getDouble("budget");
            Family family = new Family(idFamily, name, budget);
            families.add(family);
        }
        return families;
    }
    public ArrayList<Limit> getLimits() throws SQLException, ClassNotFoundException {
        ArrayList<Limit> limits = new ArrayList<>();
        String sql = "SELECT * FROM limits";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()){
            int idLimit = rs.getInt("idLimits");
            double quantity = rs.getDouble("quantity");
            Limit limit = new Limit(idLimit, quantity);
            limits.add(limit);
        }
        return limits;
    }
    public ArrayList<Member> getMembers() throws SQLException, ClassNotFoundException {
        ArrayList<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()){
            int idMembers = rs.getInt("idMembers");
            String name = rs.getString("name");
            String gender = rs.getString("gender");
            int  age = rs.getInt("age");
            String relation = rs.getString("relation");
            int Families_id = rs.getInt("Families_id");
            int Limits_id = rs.getInt("Limits_id");
            Member member = new Member(idMembers, name, gender, Integer.toString(age), relation, Families_id, Limits_id);
            members.add(member);
        }
        return members;
    }
    public Member searchMember(int memberId) throws SQLException {
        String sql = "SELECT * FROM members WHERE idMembers='"+memberId+"';";
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()) {
            String name = rs.getString("name");
            String gender = rs.getString("gender");
            int age = rs.getInt("age");
            String relation = rs.getString("relation");
            int Families_id = rs.getInt("Families_id");
            int Limits_id = rs.getInt("Limits_id");
            Member member = new Member(memberId, name, gender, Integer.toString(age), relation, Families_id, Limits_id);
            return member;
        } else {
            return null; // или выбросить исключение, если член с таким ID не найден
        }
    }
    public Family searchFamily(int Family_id) throws SQLException {
        String sql = "SELECT * FROM families WHERE idFamilies='"+Family_id+"';";
        ResultSet rs = statement.executeQuery(sql);
        if(rs.next()) {
            int idFamily = rs.getInt("idFamilies");
            String name = rs.getString("name");
            double budget = rs.getDouble("budget");
            Family family = new Family(idFamily, name, budget);
            return family;
        }
        else{
            return null;
        }
    }
    public Family searchFamily(String f_name) throws SQLException {
        String sql = "SELECT * FROM families WHERE name='"+f_name+"';";
        ResultSet rs = statement.executeQuery(sql);
        if(rs.next()) {
            int idFamily = rs.getInt("idFamilies");
            String name = rs.getString("name");
            double budget = rs.getDouble("budget");
            Family family = new Family(idFamily, name, budget);
            return family;
        }
        else{
            return null;
        }
    }
    public Limit searchLimit(int Limit_id) throws SQLException {
        String sql = "SELECT * FROM limits WHERE idLimits='"+Limit_id+"';";
        ResultSet rs = statement.executeQuery(sql);
        if(rs.next()) {
            double quantity = rs.getDouble("quantity");
            Limit limit = new Limit(Limit_id, quantity);
            return limit;
        }
        else{
            return null;
        }
    }
    public ArrayList<Member> FamilyMembers(int Family_id) throws SQLException {
        ArrayList<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE Families_id='"+Family_id+"';";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()){
            int idMembers = rs.getInt("idMembers");
            String name = rs.getString("name");
            String gender = rs.getString("gender");
            int  age = rs.getInt("age");
            String relation = rs.getString("relation");
            int Families_id = rs.getInt("Families_id");
            int Limits_id = rs.getInt("Limits_id");
            Member member = new Member(idMembers, name, gender, Integer.toString(age), relation, Families_id, Limits_id);
            members.add(member);
        }
        return members;
    }
    public void updateMemberInfo(int member_id, String newName, String newGender, String newAge) throws SQLException {
        String sql = "UPDATE members SET name = '"+newName+"', gender = '"+newGender+"', age = '"+newAge+"' WHERE idMembers = "+member_id+";";
        int rowsUpdated = statement.executeUpdate(sql);
        if (rowsUpdated > 0) {
            System.out.println("Обновление успешно выполнено!");
        }
    }
    public void updateMemberInfo(int member_id, int family_id) throws SQLException {
        String sql = "UPDATE members SET Families_id = '"+family_id+"', relation = 'Родитель' WHERE idMembers = "+member_id+";";
        int rowsUpdated = statement.executeUpdate(sql);
        if (rowsUpdated > 0) {
            System.out.println("Обновление успешно выполнено!");
        }
    }

    public void updateMemberInfo(int member_id, String newRole) throws SQLException {
        String sql = "UPDATE members SET relation = '"+newRole+"' WHERE idMembers = "+member_id+";";
        int rowsUpdated = statement.executeUpdate(sql);
        if (rowsUpdated > 0) {
            System.out.println("Обновление успешно выполнено!");
        }
    }
    public void updateMemberInfoLimit(int limit_id, int member_id) throws SQLException {
        String sql = "UPDATE members SET Limits_id = '"+limit_id+"' WHERE idMembers = "+member_id+";";
        int rowsUpdated = statement.executeUpdate(sql);
        if (rowsUpdated > 0) {
            System.out.println("Обновление успешно выполнено!");
        }
    }
    public void updateFamilyInfoAdd(int family_id, double sum) throws SQLException {
        double oldSum = searchFamily(family_id).getBudget();
        double newSum = oldSum + sum;
        String sql = "UPDATE families SET budget = '"+newSum+"' WHERE idFamilies = "+family_id+";";
        int rowsUpdated = statement.executeUpdate(sql);
        if (rowsUpdated > 0) {
            System.out.println("Обновление успешно выполнено!");
        }
    }
    public void updateFamilyInfoRemove(int family_id, double sum) throws SQLException {
        double oldSum = searchFamily(family_id).getBudget();
        double newSum = oldSum - sum;
        String sql = "UPDATE families SET budget = '"+newSum+"' WHERE idFamilies = "+family_id+";";
        int rowsUpdated = statement.executeUpdate(sql);
        if (rowsUpdated > 0) {
            System.out.println("Обновление успешно выполнено!");
        }
    }
}



