package org.FruitBlendVendor.model;

public abstract class InventoryItem {
    protected String name;
    protected double quantity;
    protected String unit;

    public InventoryItem(String name, double quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public boolean hasEnough(double required) {
        return this.quantity >= required;
    }

    public void reduce(double amount) {
        this.quantity -= amount;
    }

    public abstract double getCost();

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }
}
