package model;

import java.io.Serializable;

public class Operation implements Serializable {
    private int idOperations;
    private double quantity;
    private String category;
    private int id_Members;

    public Operation(int idOperations, double quantity, String category, int id_Members) {
        this.idOperations = idOperations;
        this.quantity = quantity;
        this.category = category;
        this.id_Members = id_Members;
    }

    public int getIdOperations() {
        return idOperations;
    }

    public void setIdOperations(int idOperations) {
        this.idOperations = idOperations;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId_Members() {
        return id_Members;
    }

    public void setId_Members(int id_Members) {
        this.id_Members = id_Members;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "idOperations=" + idOperations +
                ", quantity=" + quantity +
                ", category='" + category + '\'' +
                ", id_Members=" + id_Members +
                '}';
    }
}
