package model;

import java.io.Serializable;

public class Family implements Serializable {
    private int idFamilies;
    private String name;
    private double budget;

    public Family(int idFamilies, String name, double budget) {
        this.idFamilies = idFamilies;
        this.name = name;
        this.budget = budget;
    }

    public int getIdFamilies() {
        return idFamilies;
    }

    public void setIdFamilies(int idFamilies) {
        this.idFamilies = idFamilies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    @Override
    public String toString() {
        return "Family{" +
                "idFamilies=" + idFamilies +
                ", name='" + name + '\'' +
                ", budget=" + budget +
                '}';
    }
}
