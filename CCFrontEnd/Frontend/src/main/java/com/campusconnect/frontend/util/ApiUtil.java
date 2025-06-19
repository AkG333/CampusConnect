package com.campusconnect.frontend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
// import com.fasterxml.jackson.databind.SerializationFeature; // Optional, only if needed for specific formatting

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * Utility class for making HTTP requests to the backend API.
 * Uses Java 11's HttpClient for asynchronous communication.
 */
public class ApiUtil {

    // Singleton HttpClient instance
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    // ObjectMapper for JSON serialization/deserialization - NOW CENTRALIZED
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // Static initializer block to configure ObjectMapper once
    static {
        // Register the JavaTimeModule to enable handling of java.time.* types (like LocalDateTime)
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        // Optional: If your backend sends dates without milliseconds or timezones,
        // and you face parsing issues, you might need to adjust formatting.
        // For example, to disable writing dates as timestamps when sending data:
        // OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    // --- IMPORTANT: CONFIGURE YOUR BACKEND BASE URL HERE ---
    public static final String BASE_API_URL = "http://localhost:8080"; // !!! CHANGE THIS TO YOUR ACTUAL BACKEND URL !!!

    // Private constructor to prevent direct instantiation
    private ApiUtil() {
        // Utility classes should not be instantiated
    }

    /**
     * Returns the singleton ObjectMapper instance configured for JSON processing.
     * @return The configured ObjectMapper.
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * Sends an HTTP POST request to the specified API endpoint with a JSON request body.
     *
     * @param endpoint The specific API path (e.g., "/users/login", "/products").
     * This will be appended to BASE_API_URL.
     * @param requestBodyObject An Object representing the data to be sent in the JSON body.
     * This object will be serialized to JSON by ObjectMapper.
     * @return A CompletableFuture that will complete with the HttpResponse<String> from the server.
     * @throws IOException If there's an error during JSON serialization.
     */
    public static CompletableFuture<HttpResponse<String>> post(String endpoint, Object requestBodyObject) throws IOException {
        String jsonRequestBody = OBJECT_MAPPER.writeValueAsString(requestBodyObject); // Use the centralized ObjectMapper

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_API_URL + endpoint)) // Construct the full URI
                .header("Content-Type", "application/json") // Set content type for JSON
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody)) // Set POST method and body
                .build();

        // Send the request asynchronously and return the CompletableFuture
        return HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Sends an HTTP GET request to the specified API endpoint.
     *
     * @param endpoint The specific API path (e.g., "/users", "/products/1").
     * This will be appended to BASE_API_URL.
     * @return A CompletableFuture that will complete with the HttpResponse<String> from the server.
     */
    public static CompletableFuture<HttpResponse<String>> get(String endpoint) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_API_URL + endpoint)) // Construct the full URI
                .header("Accept", "application/json") // Request JSON response
                .GET() // Set GET method
                .build();

        return HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }
}
