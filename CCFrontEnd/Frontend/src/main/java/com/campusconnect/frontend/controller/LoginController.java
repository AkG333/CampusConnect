package com.campusconnect.frontend.controller;

import com.campusconnect.frontend.util.ApiUtil;
import com.campusconnect.frontend.dto.LoginRequest; // Import the new DTO
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.concurrent.Task;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpResponse;

/**
 * Controller for the Login View.
 * Handles user input from the login form and communicates with the backend API
 * for authentication. Also provides navigation to the signup page.
 */
public class LoginController {

    // FXML elements injected from login-view.fxml
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorMessageLabel;

    // --- IMPORTANT: CONFIGURE YOUR BACKEND ENDPOINTS HERE ---
    // Your backend UserController is mapped to "/api/users"
    // And your login method is mapped to "/login"
    // ApiUtil.BASE_API_URL already contains "/api"
    // So LOGIN_ENDPOINT should only contain the remainder of the path: "/users/login"
    private final String LOGIN_ENDPOINT = "/api/users/login"; // CORRECTED: Removed duplicate "/api"

    // ObjectMapper for JSON serialization/deserialization
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Initializes the controller after its root element has been completely processed.
     * This is called by the FXMLLoader.
     */
    @FXML
    public void initialize() {
        errorMessageLabel.setText(""); // Ensure the error message is clear initially
    }

    /**
     * Handles the action when the login button is clicked.
     * Performs input validation, makes an asynchronous call to the backend,
     * and updates the UI based on the backend's response.
     */
    @FXML
    private void handleLoginButton() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // Basic client-side validation
        if (username.isEmpty() || password.isEmpty()) {
            errorMessageLabel.setText("Username/Email and Password cannot be empty.");
            return;
        }

        errorMessageLabel.setText("Logging in, please wait..."); // Provide feedback to the user

        // Create a Task to perform the network operation in a background thread
        // This prevents the UI from freezing during the network request.
        Task<Void> loginTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    // Create an instance of the LoginRequest DTO
                    LoginRequest loginRequest = new LoginRequest(username, password);

                    // Send POST request using ApiUtil.
                    // The ApiUtil.post method now accepts an Object that Jackson can convert to JSON.
                    HttpResponse<String> response = ApiUtil.post(LOGIN_ENDPOINT, loginRequest).join(); // .join() blocks until the future completes

                    Platform.runLater(() -> {
                        // Code inside Platform.runLater runs on the JavaFX Application Thread
                        // This is safe for updating UI elements
                        if (response.statusCode() == HttpURLConnection.HTTP_OK) { // HTTP 200 OK
                            errorMessageLabel.setText("Login successful!");
                            System.out.println("Login successful. Response: " + response.body());

                            // --- IMPORTANT: Navigate to the main application view ---
                            navigateToMainAppView(); // Call the navigation method here

                        } else if (response.statusCode() == HttpURLConnection.HTTP_UNAUTHORIZED) { // 401 Unauthorized
                            errorMessageLabel.setText("Invalid username or password.");
                            System.err.println("Login failed. Status: " + response.statusCode() + ", Response: " + response.body());
                        } else if (response.statusCode() == HttpURLConnection.HTTP_BAD_REQUEST) { // 400 Bad Request
                            errorMessageLabel.setText("Bad request: Username and password must not be empty.");
                            System.err.println("Login failed. Status: " + response.statusCode() + ", Response: " + response.body());
                        }
                        else { // Handle other HTTP error responses
                            String errorMsg = "Login failed: ";
                            try {
                                JsonNode errorJson = objectMapper.readTree(response.body());
                                // Try to extract a specific error message from the backend's JSON response
                                if (errorJson.has("message")) {
                                    errorMsg += errorJson.get("message").asText();
                                } else if (errorJson.has("error")) {
                                    errorMsg += errorJson.get("error").asText();
                                }
                                else {
                                    errorMsg += "Status " + response.statusCode(); // Fallback if no specific message
                                }
                            } catch (IOException e) {
                                // If response body isn't valid JSON or other parsing error
                                errorMsg += "Status " + response.statusCode() + " (Could not parse error message)";
                            }
                            errorMessageLabel.setText(errorMsg);
                            System.err.println("Login failed. Status: " + response.statusCode() + ", Response: " + response.body());
                        }
                    });

                } catch (Exception e) { // Catch any other exceptions during task execution (e.g., network issues)
                    Platform.runLater(() -> {
                        errorMessageLabel.setText("An unexpected error occurred: " + e.getLocalizedMessage());
                        System.err.println("Task execution error: " + e.getMessage());
                        e.printStackTrace();
                    });
                }
                return null;
            }
        };

        new Thread(loginTask).start(); // Start the task in a new background thread
    }

    /**
     * Handles the action when the "Sign Up" button is clicked.
     * Navigates to the signup page.
     */
    @FXML
    private void handleSignUpButton() {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/campusconnect/frontend/signup-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 400, 550); // Adjust size for signup page
            stage.setScene(scene);
            stage.setTitle("CampusConnect - Sign Up");
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load signup view: " + e.getMessage());
            errorMessageLabel.setText("Error navigating to signup page.");
        }
    }

    /**
     * Helper method to navigate to the main application view after successful login.
     */
    private void navigateToMainAppView() {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow(); // Get current stage
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/campusconnect/frontend/main-app-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 700, 600); // Adjust size for main app view
            stage.setScene(scene);
            stage.setTitle("CampusConnect - Dashboard");
            stage.show();
            System.out.println("Navigated to main application view.");
        } catch (IOException e) {
            System.err.println("Failed to load main app view: " + e.getMessage());
            errorMessageLabel.setText("Error loading main application.");
        }
    }
}
