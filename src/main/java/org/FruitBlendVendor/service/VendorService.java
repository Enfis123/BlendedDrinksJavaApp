package org.FruitBlendVendor.service;

import javafx.scene.control.TextArea;
import org.FruitBlendVendor.exception.InsufficientIngredientException;
import org.FruitBlendVendor.exception.InvalidDrinkException;
import org.FruitBlendVendor.model.Ingredient;
import org.FruitBlendVendor.utils.InputValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendorService {
    private final Map<String, Ingredient> inventory = new HashMap<>();  // Stores the inventory of ingredients
    private double revenue = 0;  // Tracks total revenue generated
    private static final double WARNING_THRESHOLD = 4;  // Threshold for low ingredient warning
    private final Map<String, Integer> dailySales = new HashMap<>();  // Tracks daily sales for each drink

    public VendorService() {
        // Initialize the inventory with predefined ingredients and their quantities
        inventory.put("strawberry", new Ingredient("Strawberry", 1000, "g", 0.50));
        inventory.put("banana", new Ingredient("Banana", 1000, "g", 0.70));
        inventory.put("mango", new Ingredient("Mango", 1000, "g", 1.0));
        inventory.put("ice", new Ingredient("Ice", 5000, "ml", 0.30));
        inventory.put("condensed milk", new Ingredient("Condensed Milk", 3000, "ml", 0.30));
        inventory.put("sugar", new Ingredient("Sugar", 1000, "g", 0.05));
    }

    // Lists current inventory, checks for ingredients running low based on the threshold
    public void listInventory(TextArea outputArea) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n🌟 === Current Inventory === 🌟\n");
        for (Ingredient ingredient : inventory.values()) {
            sb.append(String.format("🔹 %s: %.2f %s%n", ingredient.getName(), ingredient.getQuantity(), ingredient.getUnit()));
            // Checks if the quantity is below the warning threshold
            if (ingredient.getQuantity() < WARNING_THRESHOLD * getRequiredAmount(ingredient.getName().toLowerCase())) {
                sb.append("⚠ Warning: Running low on " + ingredient.getName() + "!\n");
            }
        }
        outputArea.appendText(sb.toString());  // Display in the TextArea
    }

    // Sells a drink by reducing the inventory and updating revenue
    public void sellDrink(String size, List<String> ingredients, TextArea outputArea) throws InvalidDrinkException, InsufficientIngredientException {
        if (ingredients.isEmpty()) {
            throw new InvalidDrinkException("No ingredients selected");  // Throws exception if no ingredients selected
        }

        // Validate each ingredient
        for (String ingredient : ingredients) {
            if (!InputValidator.isValidIngredient(ingredient.trim())) {
                throw new InvalidDrinkException("Invalid ingredient: " + ingredient);  // Invalid ingredient handling
            }
        }

        double sizeFactor = getSizeFactor(size);  // Determines the required ingredient quantities based on the size
        double fruitRequired = 50 * sizeFactor;  // Amount of blended fruit required based on size
        double iceRequired = 30 * sizeFactor;  // Amount of ice required
        double milkRequired = 20 * sizeFactor;  // Amount of condensed milk required
        double sugarRequired = 8 * sizeFactor;  // Amount of sugar required

        Map<String, Double> requiredIngredients = new HashMap<>();

        // Calculate required quantities for each selected ingredient
        for (String ingredient : ingredients) {
            requiredIngredients.put(ingredient, getRequiredAmount(ingredient) * fruitRequired / 100);
        }

        // Add common ingredients (ice, milk, sugar) to the required ingredients list
        requiredIngredients.put("ice", iceRequired);
        requiredIngredients.put("condensed milk", milkRequired);
        requiredIngredients.put("sugar", sugarRequired);

        // Checks if there are enough ingredients in inventory
        if (!hasEnoughIngredients(requiredIngredients)) {
            throw new InsufficientIngredientException("Not enough ingredients available");  // Insufficient ingredients handling
        }

        reduceIngredients(requiredIngredients);  // Reduces the quantity of ingredients in inventory

        double price = calculatePrice(ingredients, sizeFactor);  // Calculates the drink's price based on ingredients and size
        revenue += price;  // Updates total revenue
        recordSale(String.join(", ", ingredients));  // Records the sale for the daily report

        outputArea.appendText("\n✅ Enjoy your delicious " + String.join(", ", ingredients) + " (" + size + " size) drink! Price: $" + String.format("%.2f", price) + "\n");
    }

    // Checks if there are enough ingredients in inventory to make the drink
    private boolean hasEnoughIngredients(Map<String, Double> requiredIngredients) {
        for (Map.Entry<String, Double> entry : requiredIngredients.entrySet()) {
            if (inventory.get(entry.getKey()).getQuantity() < entry.getValue()) {
                return false;  // Not enough of the ingredient
            }
        }
        return true;
    }

    // Reduces the quantities of ingredients in the inventory after a sale
    private void reduceIngredients(Map<String, Double> requiredIngredients) {
        for (Map.Entry<String, Double> entry : requiredIngredients.entrySet()) {
            inventory.get(entry.getKey()).reduce(entry.getValue());  // Reduces the ingredient quantity
        }
    }

    // Determines the size factor (multiplier) for ingredient quantities based on drink size
    private double getSizeFactor(String size) {
        return switch (size) {
            case "small" -> 3.0;  // Small drink has a multiplier of 3
            case "medium" -> 6.0;  // Medium drink has a multiplier of 6
            case "large" -> 9.0;  // Large drink has a multiplier of 9
            default -> 3.0;  // Default to small if an invalid size is provided
        };
    }

    private double calculatePrice(List<String> ingredients, double sizeFactor) {
        double ingredientCost = 0;
        double iceRequired = 30 * sizeFactor;  // Total ice required for the drink size
        double milkRequired = 20 * sizeFactor;  // Total condensed milk required
        double sugarRequired = 8 * sizeFactor;  // Total sugar required

        // Loop through each ingredient and calculate the cost based on the size and ingredient cost
        for (String ingredient : ingredients) {
            double ingredientBaseAmount = getRequiredAmount(ingredient); // Get base amount per 100 ml (strawberry: 100g, banana: 120g, etc.)
            double ingredientAmount = ingredientBaseAmount * sizeFactor / 100; // Adjust for size
            ingredientCost += ingredientAmount * inventory.get(ingredient).getCost(); // Calculate cost
        }

        // Calculate the costs for the common ingredients (ice, milk, sugar)
        ingredientCost += (iceRequired / 100) * inventory.get("ice").getCost();
        ingredientCost += (milkRequired / 100) * inventory.get("condensed milk").getCost();
        ingredientCost += (sugarRequired / 100) * inventory.get("sugar").getCost();

        // Apply a profit of 50%
        return ingredientCost * 1.5; // Price markup for profit
    }

    // Displays the total revenue earned
    public void showRevenue(TextArea outputArea) {
        outputArea.appendText("\n💰 Total Revenue Earned: $" + String.format("%.2f", revenue) + "\n");
    }

    // Records the sale of a drink by updating the daily sales map
    private void recordSale(String drinkDescription) {
        dailySales.put(drinkDescription, dailySales.getOrDefault(drinkDescription, 0) + 1);  // Increments sale count for this drink
    }

    // Displays the daily sales report showing how many drinks of each type were sold
    public void showDailySalesReport(TextArea outputArea) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n📊 === Daily Sales Report === 📊\n");
        for (Map.Entry<String, Integer> entry : dailySales.entrySet()) {
            sb.append(String.format("🍹 %s: %d drinks sold%n", entry.getKey(), entry.getValue()));  // Prints sales for each drink
        }
        outputArea.appendText(sb.toString());  // Display in the TextArea
    }

    // Returns the base amount required for each ingredient (per 100 ml)
    private double getRequiredAmount(String ingredient) {
        return switch (ingredient) {
            case "strawberry" -> 100;  // Strawberry requires 100g per 100ml
            case "banana" -> 120;  // Banana requires 120g per 100ml
            case "mango" -> 140;  // Mango requires 140g per 100ml
            default -> 0;  // Return 0 for unknown ingredients
        };
    }
}
