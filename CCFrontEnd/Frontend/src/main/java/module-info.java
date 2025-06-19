module com.campusconnect.frontend {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.net.http; // For HttpClient

    // Requires Jackson for JSON processing
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;

    // !!! CRITICAL: ADD THIS LINE if it's missing or commented out !!!
    requires com.fasterxml.jackson.datatype.jsr310;

    // Open packages for FXML reflection (required for @FXML annotations)
    opens com.campusconnect.frontend to javafx.fxml;
    opens com.campusconnect.frontend.controller to javafx.fxml;

    // Export DTO and Model packages so Jackson can access them for serialization/deserialization
    exports com.campusconnect.frontend;
    exports com.campusconnect.frontend.dto;
    exports com.campusconnect.frontend.model;
}
