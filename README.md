tore Manager - Smoothie Shop

🥤 Overview

Store Manager is a JavaFX-based application designed to manage a smoothie shop efficiently. It follows an MVC (Model-View-Service) architecture, ensuring a clean and maintainable code structure. The application provides essential functionalities such as inventory tracking, daily sales reports, and order management.

✨ Features

📋 View Inventory – Check available ingredients and their quantities.

📊 Daily Sales Report – Generate reports on daily sales and revenue.

💰 Sales Management – Process smoothie sales, deduct ingredients, and calculate revenue.

🚨 Low Stock Alerts – Notify when ingredients are running low.

📦 Multiple Drink Sizes – Small, Medium, and Large options with dynamic pricing.

🛠️ Technologies Used

Java 17+

JavaFX (for the user interface)

MVC Architecture

OOP Principles

📁 Project Structure

StoreManager/
├── src/
│   ├── model/         # Business logic (Ingredient, Smoothie, Sale, etc.)
│   ├── service/       # Handles operations (InventoryService, SalesService)
│   ├── view/          # JavaFX UI components (FXML views, controllers)
│   ├── Main.java      # Application entry point
└── resources/
    ├── fxml/          # UI Layouts
    ├── images/        # App images/icons

🚀 How to Run

Ensure you have Java 17+ installed.

Clone the repository:

git clone https://github.com/your-username/store-manager.git
cd store-manager

Checkout the master branch:

git checkout master

Compile and run the application:

mvn clean javafx:run  # If using Maven

OR (if running manually)

javac -d bin src/**/*.java
java -cp bin Main

📌 Future Enhancements

✅ Database Integration (Persist sales & inventory data)

✅ User Authentication (Admin & Employee roles)

✅ Export Reports (CSV/PDF format)

🚀 Built with Java & JavaFX to streamline smoothie shop management!

