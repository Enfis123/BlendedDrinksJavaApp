package org.FruitBlendVendor.exception;


public class InsufficientIngredientException extends GeneralVendorException {
    public InsufficientIngredientException(String ingredient) {
        super("❌ Not enough " + ingredient + " to prepare the drink!");
    }
}
