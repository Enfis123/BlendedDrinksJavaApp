package org.FruitBlendVendor.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.FruitBlendVendor.service.VendorService;

import java.util.ArrayList;
import java.util.List;

public class MainController {
    private final VendorService vendorService = new VendorService();

    @FXML
    private Button listInventoryButton, sellDrinkButton, showRevenueButton, showReportButton, exitButton;

    @FXML
    private Label statusLabel;

    @FXML
    private TextArea outputArea;

    @FXML
    public void initialize() {
        listInventoryButton.setOnAction(e -> listInventory());
        sellDrinkButton.setOnAction(e -> sellDrink());
        showRevenueButton.setOnAction(e -> showRevenue());
        showReportButton.setOnAction(e -> showReport());
        exitButton.setOnAction(e -> exitApplication());
    }

    private void listInventory() {
        try {
            vendorService.listInventory(outputArea);
        } catch (Exception e) {
            handleError("Error listing inventory", e);
        }
    }

    private void sellDrink() {

        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Sell Drink");
        dialog.setHeaderText("Select the size and ingredients");


        ComboBox<String> sizeComboBox = new ComboBox<>();
        sizeComboBox.getItems().addAll("small", "medium", "large");
        sizeComboBox.setValue("small");

        VBox ingredientBox = new VBox();
        CheckBox strawberry = new CheckBox("Strawberry");
        CheckBox banana = new CheckBox("Banana");
        CheckBox mango = new CheckBox("Mango");
        ingredientBox.getChildren().addAll(strawberry, banana, mango);


        dialog.getDialogPane().setContent(new VBox(sizeComboBox, ingredientBox));


        ButtonType okButton = new ButtonType("Sell", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);


        dialog.setResultConverter(button -> {
            if (button == okButton) {
                List<String> ingredients = new ArrayList<>();
                if (strawberry.isSelected()) ingredients.add("strawberry");
                if (banana.isSelected()) ingredients.add("banana");
                if (mango.isSelected()) ingredients.add("mango");

                String size = sizeComboBox.getValue();
                try {
                    vendorService.sellDrink(size, ingredients, outputArea);
                } catch (Exception e) {
                    handleError("Error selling drink", e);
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showRevenue() {
        try {
            vendorService.showRevenue(outputArea);
        } catch (Exception e) {
            handleError("Error showing revenue", e);
        }
    }

    private void showReport() {
        try {
            vendorService.showDailySalesReport(outputArea);
        } catch (Exception e) {
            handleError("Error showing report", e);
        }
    }

    private void exitApplication() {
        System.exit(0);
    }

    private void showMessage(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void handleError(String title, Exception e) {
        showError(title, e.getMessage());

        outputArea.appendText("\n" + title + ": " + e.getMessage() + "\n");
    }
}


