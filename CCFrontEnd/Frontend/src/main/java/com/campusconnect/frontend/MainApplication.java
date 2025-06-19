package com.campusconnect.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main entry point for the CampusConnect JavaFX Frontend application.
 * This class extends {@link javafx.application.Application} and is responsible for
 * setting up the primary stage and loading the initial UI (the login page).
 */
public class MainApplication extends Application {

    /**
     * The primary stage of the application.
     * Can be used for scene management if needed globally, but often managed per controller.
     */
    private static Stage primaryStage; // Keep a static reference for easier scene switching if needed

    /**
     * The start method is the main entry point for all JavaFX applications.
     * The JavaFX runtime calls this method after `init()` method returns
     * and after the system is ready for the application to begin running.
     *
     * @param stage The primary stage for this application, onto which
     * the application scene can be set. The stage is created by the
     * platform.
     * @throws IOException If the FXML file cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage; // Store the primary stage reference

        // Load the FXML file for the login view
        // The path uses the same convention as your resource folder structure:
        // /com/campusconnect/frontend/login-view.fxml
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/campusconnect/frontend/login-view.fxml"));

        // Create a Scene from the loaded FXML layout
        Scene scene = new Scene(fxmlLoader.load(), 400, 450); // Consistent size with login-view

        // Set the title of the application window
        stage.setTitle("CampusConnect - Login");

        // Set the created scene on the primary stage
        stage.setScene(scene);

        // Display the stage
        stage.show();
    }

    /**
     * Retrieves the primary stage of the application.
     * This can be useful for external classes (like controllers) to switch scenes.
     * @return The primary Stage.
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * The main method is the standard entry point for a Java application.
     * In a JavaFX application, `launch()` is typically called here to start
     * the JavaFX runtime and subsequently invoke the `start()` method.
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(); // Launches the JavaFX application
    }
}
