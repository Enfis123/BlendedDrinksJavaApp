package org.FruitBlendVendor.exception;

public class InvalidDrinkException extends GeneralVendorException {
    public InvalidDrinkException(String drink) {
        super("🚫 '" + drink + "' is not a valid drink option! Please choose Strawberry, Banana, or Mango.");
    }
}