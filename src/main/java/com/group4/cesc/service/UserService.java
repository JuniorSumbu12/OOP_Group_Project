package com.group4.cesc.service;

import com.group4.cesc.dao.UserDAO;
import com.group4.cesc.dao.impl.MySQLUserDAO;
import com.group4.cesc.model.User;

/**
 * Service layer handling all user-related business logic.
 * <p>
 * This class acts as an intermediary between servlets and the database.
 * It validates inputs and calls the DAO implementation.
 */
public class UserService {

    private UserDAO userDAO;

    /**
     * Creates a new UserService and initializes the DAO implementation.
     */
    public UserService() {
        userDAO = new MySQLUserDAO();
    }

    /**
     * Registers a new user after checking for duplicate emails.
     *
     * @param user the user to register
     * @return true if registration succeeded; false if email already exists
     */
    public boolean registerUser(User user) {
        if (userDAO.emailExists(user.getEmail())) {
            return false;
        }
        return userDAO.register(user);
    }

    /**
     * Validates login credentials.
     *
     * @param email    user email
     * @param password user password
     * @return a User object if login is successful; null otherwise
     */
    public User login(String email, String password) {
        return userDAO.login(email, password);
    }
}
