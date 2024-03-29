package model;

import java.io.Serializable;

public class User implements Serializable {
    private int idUsers;
    private String login;
    private String password;
    private String role;
    private int Members_Id;

    public User() {

    }

    public int getMembers_Id() {
        return Members_Id;
    }

    public void setMembers_Id(int members_Id) {
        Members_Id = members_Id;
    }

    public User(int idUsers, String login, String password, String role, int Members_Id) {
        this.idUsers = idUsers;
        this.login = login;
        this.password = password;
        this.role = role;
        this.Members_Id = Members_Id;
    }

    public int getIdUsers() {
        return idUsers;
    }

    public void setIdUsers(int idUsers) {
        this.idUsers = idUsers;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUsers=" + idUsers +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", Members_Id=" + Members_Id +
                '}';
    }
}
