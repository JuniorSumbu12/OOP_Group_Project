package com.group4.cesc.controller;

import com.group4.cesc.model.User;
import com.group4.cesc.pattern.factory.UserFactory;
import com.group4.cesc.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Handles the registration of new users.
 * <p>
 * This servlet processes registration form submissions,
 * creates the appropriate User type using the Factory Pattern,
 * and stores the user in the database through {@link UserService}.
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserService userService;

    /**
     * Initializes the UserService used to handle registration logic.
     */
    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    /**
     * Handles POST requests from register.jsp.
     * <p>
     * Retrieves form fields, creates a User using {@link UserFactory},
     * checks for duplicate emails, and inserts the user into the database.
     *
     * @param request  HTTP request containing registration details
     * @param response HTTP response for redirection or error display
     * @throws ServletException if servlet execution fails
     * @throws IOException      if I/O failures occur
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String role = request.getParameter("role");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = UserFactory.createUser(role, name, email, password);

        boolean success = userService.registerUser(user);

        if (success) {
            response.sendRedirect("login.jsp?msg=registered");
        } else {
            request.setAttribute("error", "Email already exists!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
