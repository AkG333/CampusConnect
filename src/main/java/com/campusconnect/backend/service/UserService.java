package com.campusconnect.backend.service;

import com.campusconnect.backend.model.User;
import java.util.List;

public interface UserService {
    void registerUser(User user);
    User getUserById(int id);
    List<User> getAllUsers();
    void deleteUser(int id);
    User getUserByUsername(String username);

    // Add loginUser method declaration
    boolean loginUser(String username, String password);
}
