package com.campusconnect.frontend.controller;

import com.campusconnect.frontend.model.User; // Import the frontend User DTO
import com.campusconnect.frontend.util.ApiUtil;
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
 * Controller for the Sign Up View.
 * Handles user input for registration and communicates with the backend API
 * to create a new user account.
 */
public class SignupController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label errorMessageLabel;

    // --- IMPORTANT: CONFIGURE YOUR BACKEND ENDPOINT HERE ---
    // Your backend UserController is mapped to "/api/users"
    // And your register method is mapped to "/register"
    // So the full endpoint will be /api/users/register
    private final String REGISTER_ENDPOINT = "/users/register"; // This is appended to ApiUtil.BASE_API_URL

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        errorMessageLabel.setText(""); // Clear error message on init
    }

    /**
     * Handles the action when the Sign Up button is clicked.
     * Performs client-side validation and sends a registration request to the backend.
     */
    @FXML
    private void handleSignUpButton() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Client-side validation
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorMessageLabel.setText("All fields are required.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorMessageLabel.setText("Passwords do not match.");
            return;
        }

        if (password.length() < 6) { // Example: Minimum password length
            errorMessageLabel.setText("Password must be at least 6 characters long.");
            return;
        }
        // Add more complex email or username validation if needed

        errorMessageLabel.setText("Registering, please wait...");

        Task<Void> signupTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    // Create User DTO for registration
                    User newUser = new User(username, email, password);

                    // Send POST request using ApiUtil
                    HttpResponse<String> response = ApiUtil.post(REGISTER_ENDPOINT, newUser).join();

                    Platform.runLater(() -> {
                        if (response.statusCode() == HttpURLConnection.HTTP_CREATED) { // 201 Created
                            errorMessageLabel.setText("Registration successful! You can now login.");
                            System.out.println("Registration successful. Response: " + response.body());
                            // Optionally, clear fields or automatically navigate to login page
                            clearFields();
                            // Navigate back to login page after successful registration
                            navigateToLoginPage();
                        } else {
                            String errorMsg = "Registration failed: ";
                            try {
                                JsonNode errorJson = objectMapper.readTree(response.body());
                                if (errorJson.has("message")) {
                                    errorMsg += errorJson.get("message").asText();
                                } else if (errorJson.has("error")) {
                                    errorMsg += errorJson.get("error").asText();
                                } else {
                                    errorMsg += "Status " + response.statusCode();
                                }
                            } catch (IOException e) {
                                errorMsg += "Status " + response.statusCode() + " (Could not parse error message)";
                            }
                            errorMessageLabel.setText(errorMsg);
                            System.err.println("Registration failed. Status: " + response.statusCode() + ", Response: " + response.body());
                        }
                    });

                } catch (Exception e) {
                    Platform.runLater(() -> {
                        errorMessageLabel.setText("An unexpected error occurred: " + e.getLocalizedMessage());
                        System.err.println("Signup communication error: " + e.getMessage());
                        e.printStackTrace();
                    });
                }
                return null;
            }
        };

        new Thread(signupTask).start();
    }

    /**
     * Handles the action when the "Login" button (on the signup page) is clicked.
     * Navigates back to the login page.
     */
    @FXML
    private void handleLoginButton() {
        navigateToLoginPage();
    }

    /**
     * Helper method to clear all input fields after successful registration.
     */
    private void clearFields() {
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }

    /**
     * Helper method to navigate to the login page.
     */
    private void navigateToLoginPage() {
        try {
            Stage stage = (Stage) errorMessageLabel.getScene().getWindow(); // Get current stage
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/campusconnect/frontend/login-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 400, 450); // Keep scene size consistent
            stage.setScene(scene);
            stage.setTitle("CampusConnect - Login");
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load login view: " + e.getMessage());
            errorMessageLabel.setText("Error navigating to login page.");
        }
    }
}
