package com.campusconnect.backend.service;

import com.campusconnect.backend.dao.UserDao;
import com.campusconnect.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public void registerUser(User user) {
        // No password encoding for now
        userDao.save(user);
    }

    @Override
    public User getUserById(int id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public void deleteUser(int id) {
        userDao.delete(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    // Login Method - Using String Matching for Password Verification
    @Override
    public boolean loginUser(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return true;  // Successful login
        }
        return false;  // Invalid credentials
    }
}
