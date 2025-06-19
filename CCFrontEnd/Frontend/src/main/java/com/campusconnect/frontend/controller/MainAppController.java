package com.campusconnect.frontend.controller;

import com.campusconnect.frontend.model.Question;
import com.campusconnect.frontend.util.ApiUtil;
import com.fasterxml.jackson.core.type.TypeReference;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Controller for the Main Application View.
 * Displays a list of questions fetched from the backend and handles navigation
 * to other parts of the application (e.g., posting a new question, logout, viewing answers).
 */
public class MainAppController {

    @FXML
    private ListView<Question> questionsListView;

    @FXML
    private Label statusLabel;

    private ObservableList<Question> questions = FXCollections.observableArrayList();
    private final ObjectMapper objectMapper = ApiUtil.getObjectMapper();

    private final String QUESTIONS_ENDPOINT = "/api/questions";

    /**
     * Initializes the controller. Sets up the ListView and loads questions.
     */
    @FXML
    public void initialize() {
        questionsListView.setItems(questions);
        statusLabel.setText("Loading questions...");
        loadQuestions();

        // Add listener for click events on the ListView
        questionsListView.setOnMouseClicked(event -> {
            System.out.println("DEBUG: Mouse clicked on ListView. Click count: " + event.getClickCount());
            if (event.getClickCount() == 2) { // Double-click to view answers
                Question selectedQuestion = questionsListView.getSelectionModel().getSelectedItem();
                System.out.println("DEBUG: Double-click detected.");
                if (selectedQuestion != null) {
                    System.out.println("DEBUG: Question selected: " + selectedQuestion.getTitle() + " (ID: " + selectedQuestion.getId() + ")");
                    navigateToAnswersView(selectedQuestion);
                } else {
                    System.out.println("DEBUG: No question selected in ListView.");
                    statusLabel.setText("Please select a question to view answers.");
                }
            }
        });
    }

    /**
     * Fetches all questions from the backend API asynchronously.
     */
    private void loadQuestions() {
        statusLabel.setText("Loading questions...");
        Task<Void> loadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    HttpResponse<String> response = ApiUtil.get(QUESTIONS_ENDPOINT).join();

                    Platform.runLater(() -> {
                        if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                            try {
                                List<Question> fetchedQuestions = objectMapper.readValue(
                                        response.body(),
                                        new TypeReference<List<Question>>() {}
                                );
                                questions.setAll(fetchedQuestions);

                                if (fetchedQuestions.isEmpty()) {
                                    statusLabel.setText("No questions available.");
                                } else {
                                    statusLabel.setText("Questions loaded successfully (" + fetchedQuestions.size() + ").");
                                }
                                System.out.println("Questions loaded: " + fetchedQuestions.size());
                            } catch (IOException e) {
                                statusLabel.setText("Failed to parse questions data: " + e.getLocalizedMessage());
                                System.err.println("Error parsing questions JSON: " + e.getMessage());
                                e.printStackTrace();
                            }
                        } else {
                            statusLabel.setText("Failed to load questions: Status " + response.statusCode());
                            System.err.println("Failed to load questions. Status: " + response.statusCode() + ", Response: " + response.body());
                        }
                    });

                } catch (Exception e) {
                    Platform.runLater(() -> {
                        statusLabel.setText("Network error: Could not load questions. " + e.getLocalizedMessage());
                        System.err.println("Error fetching questions: " + e.getMessage());
                        e.printStackTrace();
                    });
                }
                return null;
            }
        };

        new Thread(loadTask).start();
    }

    /**
     * Handles the action to refresh the list of questions.
     */
    @FXML
    private void handleRefreshQuestionsButton() {
        loadQuestions();
    }

    /**
     * Handles the action to post a new question.
     * Navigates to the "Post Question" view.
     */
    @FXML
    private void handlePostQuestionButton() {
        try {
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/campusconnect/frontend/post-question-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 500, 550);
            stage.setScene(scene);
            stage.setTitle("CampusConnect - Post Question");
            stage.show();
            System.out.println("Navigated to Post Question screen.");
        } catch (IOException e) {
            System.err.println("Failed to load Post Question view: " + e.getMessage());
            statusLabel.setText("Error navigating to post question page.");
            e.printStackTrace();
        }
    }

    /**
     * Handles the logout action, navigating back to the login page.
     */
    @FXML
    private void handleLogoutButton() {
        try {
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/campusconnect/frontend/login-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 400, 450);
            stage.setScene(scene);
            stage.setTitle("CampusConnect - Login");
            stage.show();
            System.out.println("Logged out. Navigating to login page.");
        } catch (IOException e) {
            System.err.println("Failed to load login view during logout: " + e.getMessage());
            statusLabel.setText("Error during logout navigation.");
        }
    }

    /**
     * Navigates to the Answers view for a selected question.
     * @param question The Question object to display answers for.
     */
    private void navigateToAnswersView(Question question) {
        System.out.println("DEBUG: Attempting to navigate to Answers view.");
        try {
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/campusconnect/frontend/answers-view.fxml"));
            Parent root = fxmlLoader.load(); // This line might throw an error if FXML is bad or path is wrong

            // Get the controller and pass the selected question
            AnswerController answersController = fxmlLoader.getController();
            if (answersController != null) {
                answersController.setQuestion(question);
                System.out.println("DEBUG: AnswersController setQuestion called successfully.");
            } else {
                System.err.println("ERROR: AnswersController is null after FXML load. Check answers-view.fxml fx:controller.");
            }

            Scene scene = new Scene(root, 700, 750); // Adjust size for answers view
            stage.setScene(scene);
            stage.setTitle("CampusConnect - Answers for: " + question.getTitle());
            stage.show();
            System.out.println("DEBUG: Navigated to Answers view for Question ID: " + question.getId());
        } catch (IOException e) {
            System.err.println("ERROR: Failed to load Answers view: " + e.getMessage());
            statusLabel.setText("Error navigating to answers page. See console for details.");
            e.printStackTrace(); // Crucial for seeing the full FXML loading error
        } catch (Exception e) {
            System.err.println("ERROR: An unexpected error occurred during answers view navigation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
