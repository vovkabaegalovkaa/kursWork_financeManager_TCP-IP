package model;

import java.io.Serializable;

public class Limit implements Serializable {
    int idLimits;
    double quantity;

    public Limit(int idLimits, double quantity) {
        this.idLimits = idLimits;
        this.quantity = quantity;
    }

    public int getIdLimits() {
        return idLimits;
    }

    public void setIdLimits(int idLimits) {
        this.idLimits = idLimits;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Limit{" +
                "idLimits=" + idLimits +
                ", quantity=" + quantity +
                '}';
    }
}
