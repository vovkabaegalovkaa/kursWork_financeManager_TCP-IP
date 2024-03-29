package model;

import javafx.collections.ObservableList;

import java.util.ArrayList;

public class GlobalData {
    private static User user;
    private static Member member;
    private static Family family;
    private static Limit limit;
    private static ObservableList<Member> familyMembers;
    private static ObservableList<Operation> operations;
    private static ArrayList<Limit> limits;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        GlobalData.user = user;
    }

    public static Member getMember() {
        return member;
    }

    public static void setMember(Member member) {
        GlobalData.member = member;
    }

    public static Family getFamily() {
        return family;
    }

    public static void setFamily(Family family) {
        GlobalData.family = family;
    }

    public static Limit getLimit() {
        return limit;
    }

    public static void setLimit(Limit limit) {
        GlobalData.limit = limit;
    }

    public static ObservableList<Member> getFamilyMembers() {
        return familyMembers;
    }

    public static void setFamilyMembers(ObservableList<Member> familyMembers) {
        GlobalData.familyMembers = familyMembers;
    }

    public static ObservableList<Operation> getOperations() {
        return operations;
    }

    public static void setOperations(ObservableList<Operation> operations) {
        GlobalData.operations = operations;
    }

    public static ArrayList<Limit> getLimits() {
        return limits;
    }

    public static void setLimits(ArrayList<Limit> limits) {
        GlobalData.limits = limits;
    }
}
