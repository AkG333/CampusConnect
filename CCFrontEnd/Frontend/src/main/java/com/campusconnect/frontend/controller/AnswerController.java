package com.campusconnect.frontend.controller;

import com.campusconnect.frontend.model.Question; // To receive the question object
import com.campusconnect.frontend.model.Answer;   // For displaying answers
import com.campusconnect.frontend.dto.AnswerDTO; // For posting new answers
import com.campusconnect.frontend.util.ApiUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Controller for the Answers View.
 * Displays a specific question and its answers, and allows posting new answers.
 */
public class AnswerController { // RENAMED from AnswerController to AnswersController

    @FXML
    private Label questionTitleLabel;
    @FXML
    private Label questionDescriptionLabel;
    @FXML
    private Label questionMetaLabel;
    @FXML
    private ListView<Answer> answersListView;
    @FXML
    private TextArea answerContentArea;
    @FXML
    private Label answerStatusLabel;

    private Question currentQuestion; // To hold the question details for this page
    private ObservableList<Answer> answers = FXCollections.observableArrayList();
    private final ObjectMapper objectMapper = ApiUtil.getObjectMapper();

    // Backend Endpoints - Adjusted to include "/api" prefix as BASE_API_URL is "http://localhost:8080"
    private final String GET_ANSWERS_ENDPOINT = "/api/answers/question/"; // Corrected
    private final String POST_ANSWER_ENDPOINT = "/api/answers";           // Corrected

    /**
     * Initializes the controller. Called by FXMLLoader.
     */
    @FXML
    public void initialize() {
        answersListView.setItems(answers);
        answerStatusLabel.setText("");
        // No question set yet, so don't load answers here.
        // The setQuestion method will be called by MainAppController.
    }

    /**
     * Called by MainAppController to pass the selected question to this controller.
     * @param question The Question object selected from the dashboard.
     */
    public void setQuestion(Question question) {
        this.currentQuestion = question;
        if (currentQuestion != null) {
            questionTitleLabel.setText(currentQuestion.getTitle());
            questionDescriptionLabel.setText(currentQuestion.getDescription());
            questionMetaLabel.setText(String.format("Asked by User ID: %d on %s",
                    currentQuestion.getUserId(),
                    currentQuestion.getCreatedAt() != null ? currentQuestion.getCreatedAt().toLocalDate() : "N/A"));
            loadAnswers(); // Load answers for this specific question
        } else {
            questionTitleLabel.setText("Error: Question not found.");
            questionDescriptionLabel.setText("");
            questionMetaLabel.setText("");
            answerStatusLabel.setText("Error loading answers: Question details missing.");
        }
    }

    /**
     * Fetches answers for the current question from the backend API asynchronously.
     */
    private void loadAnswers() {
        if (currentQuestion == null || currentQuestion.getId() == null) {
            answerStatusLabel.setText("Cannot load answers: Question ID is missing.");
            return;
        }

        answerStatusLabel.setText("Loading answers...");
        Task<Void> loadAnswersTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    String url = GET_ANSWERS_ENDPOINT + currentQuestion.getId();
                    System.out.println("DEBUG AnswersController: Fetching answers from URL: " + ApiUtil.BASE_API_URL + url); // Debug print
                    HttpResponse<String> response = ApiUtil.get(url).join();

                    Platform.runLater(() -> {
                        if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                            try {
                                List<Answer> fetchedAnswers = objectMapper.readValue(
                                        response.body(),
                                        new TypeReference<List<Answer>>() {}
                                );
                                answers.setAll(fetchedAnswers);
                                if (fetchedAnswers.isEmpty()) {
                                    answerStatusLabel.setText("No answers yet. Be the first to answer!");
                                } else {
                                    answerStatusLabel.setText("Answers loaded successfully (" + fetchedAnswers.size() + ").");
                                }
                                System.out.println("Answers loaded for Question ID " + currentQuestion.getId() + ": " + fetchedAnswers.size());
                            } catch (IOException e) {
                                answerStatusLabel.setText("Failed to parse answers data: " + e.getLocalizedMessage());
                                System.err.println("Error parsing answers JSON: " + e.getMessage());
                                e.printStackTrace();
                            }
                        } else {
                            answerStatusLabel.setText("Failed to load answers: Status " + response.statusCode());
                            System.err.println("Failed to load answers. Status: " + response.statusCode() + ", Response: " + response.body());
                        }
                    });

                } catch (Exception e) {
                    Platform.runLater(() -> {
                        answerStatusLabel.setText("Network error: Could not load answers. " + e.getLocalizedMessage());
                        System.err.println("Error fetching answers: " + e.getMessage());
                        e.printStackTrace();
                    });
                }
                return null;
            }
        };

        new Thread(loadAnswersTask).start();
    }

    /**
     * Handles the action when the "Submit Answer" button is clicked.
     * Validates input and sends the answer to the backend.
     */
    @FXML
    private void handleSubmitAnswer() {
        String content = answerContentArea.getText().trim();

        if (content.isEmpty()) {
            answerStatusLabel.setText("Answer cannot be empty.");
            return;
        }
        if (currentQuestion == null || currentQuestion.getId() == null) {
            answerStatusLabel.setText("Cannot post answer: Question details missing.");
            return;
        }

        // --- TEMPORARY FIX: Assign a hardcoded user ID for testing ---
        // !!! IMPORTANT: This MUST be an ID of a user that EXISTS in your 'users' table. !!!
        // In a real application, this userId would come from the authenticated user's context
        Long temporaryUserId = 1L; // <<<--- CHANGE THIS to an ID of an existing user in your DB

        answerStatusLabel.setText("Submitting answer, please wait...");

        Task<Void> submitAnswerTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    AnswerDTO newAnswer = new AnswerDTO(currentQuestion.getId(), content, temporaryUserId);

                    System.out.println("DEBUG AnswersController: Posting answer to URL: " + ApiUtil.BASE_API_URL + POST_ANSWER_ENDPOINT); // Debug print
                    HttpResponse<String> response = ApiUtil.post(POST_ANSWER_ENDPOINT, newAnswer).join();

                    Platform.runLater(() -> {
                        if (response.statusCode() == HttpURLConnection.HTTP_OK) { // Backend returns 200 OK
                            answerStatusLabel.setText("Answer posted successfully!");
                            System.out.println("Answer posted. Response: " + response.body());
                            answerContentArea.clear(); // Clear the text area
                            loadAnswers(); // Refresh the answer list to show the new answer
                        } else {
                            String errorMsg = "Failed to post answer: ";
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
                            answerStatusLabel.setText(errorMsg);
                            System.err.println("Post answer failed. Status: " + response.statusCode() + ", Response: " + response.body());
                        }
                    });

                } catch (Exception e) {
                    Platform.runLater(() -> {
                        answerStatusLabel.setText("Network error: Could not submit answer. " + e.getLocalizedMessage());
                        System.err.println("Error submitting answer: " + e.getMessage());
                        e.printStackTrace();
                    });
                }
                return null;
            }
        };

        new Thread(submitAnswerTask).start();
    }

    /**
     * Handles the "Back to Questions" button, navigating back to the main dashboard.
     */
    @FXML
    private void handleBackToQuestions() {
        try {
            Stage stage = (Stage) questionTitleLabel.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/campusconnect/frontend/main-app-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 700, 600); // Consistent size with main app view
            stage.setScene(scene);
            stage.setTitle("CampusConnect - Dashboard");
            stage.show();
            System.out.println("Navigated back to main dashboard.");
        } catch (IOException e) {
            System.err.println("Failed to load main app view: " + e.getMessage());
            answerStatusLabel.setText("Error navigating back to dashboard.");
        }
    }
}
