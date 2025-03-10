package org.FruitBlendVendor.model;

public class Ingredient extends InventoryItem {
    private  double cost;

    public Ingredient(String name, double quantity, String unit, double cost) {
        super(name, quantity, unit);
        this.cost = cost;
    }

    @Override
    public double getCost() {
        return cost;
    }
}
