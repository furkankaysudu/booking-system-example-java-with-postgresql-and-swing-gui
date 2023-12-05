package org.example.users;

public class Admin extends User {


    public Admin(String userName, StringBuilder password, int servicePrice) {
        super(userName, password);
        this.servicePrice = servicePrice;
    }

    private int servicePrice;

}
