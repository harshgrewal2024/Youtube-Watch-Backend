package com.watchparty.model;

public class Participant {

    private String userId;
    private String username;
    private Role role;

    // Constructor
    public Participant(String userId, String username, Role role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public Participant() {}

    // Getters & Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}