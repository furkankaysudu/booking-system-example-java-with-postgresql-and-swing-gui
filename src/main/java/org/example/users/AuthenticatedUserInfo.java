package org.example.users;

public class AuthenticatedUserInfo {
    public static enum UserType{
        ADMIN, NONE, COMPANY
    }
    private Boolean isAuthanticated;
    private UserType userType;

    public AuthenticatedUserInfo(Boolean isAuthanticated, UserType userType, int id) {
        this.isAuthanticated = isAuthanticated;
        this.userType = userType;
        this.id = id;
    }

    public Boolean getAuthanticated() {
        return isAuthanticated;
    }

    public UserType getUserType() {
        return userType;
    }

    private int id;

    public int getId() {
        return id;
    }
}
