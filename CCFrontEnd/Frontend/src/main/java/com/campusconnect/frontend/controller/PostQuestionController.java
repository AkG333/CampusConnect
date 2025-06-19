package com.campusconnect.frontend.controller;

import com.campusconnect.frontend.dto.QuestionDTO;
import com.campusconnect.frontend.util.ApiUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpResponse;

/**
 * Controller for the Post Question View.
 * Handles input for a new question and sends it to the backend.
 */
public class PostQuestionController {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Label statusLabel;

    private final String POST_QUESTION_ENDPOINT = "/api/questions";
    private final ObjectMapper objectMapper = ApiUtil.getObjectMapper();

    @FXML
    public void initialize() {
        statusLabel.setText("");
    }

    /**
     * Handles the action when the "Post Question" button is clicked.
     * Validates input and sends the question to the backend.
     */
    @FXML
    private void handlePostQuestion() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();

        // Client-side validation
        if (title.isEmpty() || description.isEmpty()) {
            statusLabel.setText("Title and Description cannot be empty.");
            return;
        }

        // --- TEMPORARY FIX: Assign a hardcoded user ID for testing ---
        // !!! IMPORTANT: This MUST be an ID of a user that EXISTS in your 'users' table. !!!
        // You would typically get the logged-in user's ID from a session/token here.
        Long temporaryUserId = 1L; // <<<--- CHANGE THIS to an ID of an existing user in your DB

        // If you don't have any users with ID 1, please register one via your signup page
        // or manually insert a user into your 'users' table in your database.
        // For example, if you register a user and their ID is 5, then set temporaryUserId = 5L;

        statusLabel.setText("Posting question, please wait...");

        Task<Void> postQuestionTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    // Create QuestionDTO with userId
                    QuestionDTO newQuestion; // Pass userId
                    newQuestion = new QuestionDTO(title, description, temporaryUserId);

                    HttpResponse<String> response = ApiUtil.post(POST_QUESTION_ENDPOINT, newQuestion).join();

                    Platform.runLater(() -> {
                        if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                            statusLabel.setText("Question posted successfully!");
                            System.out.println("Question posted. Response: " + response.body());
                            clearFields();
                            navigateToMainAppView();
                        } else {
                            String errorMsg = "Failed to post question: ";
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
                            statusLabel.setText(errorMsg);
                            System.err.println("Post question failed. Status: " + response.statusCode() + ", Response: " + response.body());
                        }
                    });

                } catch (Exception e) {
                    Platform.runLater(() -> {
                        statusLabel.setText("Network error: Could not post question. " + e.getLocalizedMessage());
                        System.err.println("Error posting question: " + e.getMessage());
                        e.printStackTrace();
                    });
                }
                return null;
            }
        };

        new Thread(postQuestionTask).start();
    }

    /**
     * Handles the action when the "Cancel" button is clicked.
     * Navigates back to the main application dashboard.
     */
    @FXML
    private void handleCancel() {
        navigateToMainAppView();
    }

    /**
     * Helper method to clear input fields.
     */
    private void clearFields() {
        titleField.clear();
        descriptionArea.clear();
    }

    /**
     * Helper method to navigate back to the main application dashboard.
     */
    private void navigateToMainAppView() {
        try {
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/campusconnect/frontend/main-app-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 700, 600);
            stage.setScene(scene);
            stage.setTitle("CampusConnect - Dashboard");
            stage.show();
            System.out.println("Navigated back to main application view.");
        } catch (IOException e) {
            System.err.println("Failed to load main app view: " + e.getMessage());
            statusLabel.setText("Error navigating to dashboard.");
        }
    }
}
