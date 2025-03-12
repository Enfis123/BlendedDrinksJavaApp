package org.FruitBlendVendor.utils;

public class InputValidator {

    public static String normalizeInput(String input) {
        return input.trim().toLowerCase();
    }

    public static boolean isValidSize(String size) {
        String normalizedSize = normalizeInput(size);
        return normalizedSize.equals("small") || normalizedSize.equals("medium") || normalizedSize.equals("large");
    }

    public static boolean isValidIngredient(String ingredient) {
        String normalizedIngredient = normalizeInput(ingredient);
        return normalizedIngredient.equals("strawberry") || normalizedIngredient.equals("banana") ||
                normalizedIngredient.equals("mango");
    }

    public static boolean isValidDrink(String... ingredients) {
        for (String ingredient : ingredients) {
            if (!isValidIngredient(ingredient)) {
                return false;
            }
        }
        return true;
    }
}
