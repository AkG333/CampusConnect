package com.campusconnect.backend.dao;

import com.campusconnect.backend.model.User;
import java.util.List;

public interface UserDao {
    void save(User user);
    User findById(int id);
    List<User> findAll();
    void delete(int id);
    User findByUsername(String username);
}
