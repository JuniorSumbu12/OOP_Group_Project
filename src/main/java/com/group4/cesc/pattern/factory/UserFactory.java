package com.group4.cesc.pattern.factory;

import com.group4.cesc.model.*;

/**
 * Factory Pattern implementation for creating different types of users.
 * <p>
 * Depending on the role selected in the registration form,
 * this class creates a User, Sponsor, or Maintainer object.
 */
public class UserFactory {

    /**
     * Creates a specific User type based on the role provided.
     *
     * @param role     the role string( "user", "sponsor", "maintainer")
     * @param name     user's full name
     * @param email    user's email address
     * @param password user's password
     * @return a User, Sponsor, or Maintainer instance
     */
    public static User createUser(String role, String name, String email, String password) {

        if (role == null) {
            return new User(name, email, password);
        }

        role = role.toLowerCase();

        if (role.equals("sponsor")) {
            return new Sponsor(name, email, password);
        }

        if (role.equals("maintainer")) {
            return new Maintainer(name, email, password);
        }

        return new User(name, email, password);
    }
}
