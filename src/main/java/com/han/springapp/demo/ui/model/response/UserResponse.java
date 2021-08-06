package com.han.springapp.demo.ui.model.response;

/**
 * This class will contain only the data that we want to return in the response
 */
public class UserResponse {
    /**
     * This is not the auto-incremented ID generated from the database.
     * Reason: Do not expose information that malicious users can use to retrieve data
     */
    private String userId;
    private String firstName;
    private String lastName;
    private String email;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
