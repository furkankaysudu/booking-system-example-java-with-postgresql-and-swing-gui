package org.example.users;

import org.example.vehicles.Vehicle;

import java.util.List;

public class Company extends User {

    public Company(String userName, StringBuilder password, int totalIncome) {
        super(userName, password);
        this.totalIncome = totalIncome;
    }

    @Override
    public String getUserName() {
        return super.getUserName();
    }

    @Override
    public void setUserName(String userName) {
        super.setUserName(userName);
    }

    @Override
    public StringBuilder getPassword() {
        return super.getPassword();
    }

    @Override
    public void setPassword(StringBuilder password) {
        super.setPassword(password);
    }

    private int totalIncome;


    public int getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(int totalIncome) {
        this.totalIncome = totalIncome;
    }


    public void addVehicle(){

    }

    public void addTrip(){

    }
    public void calculateIncome(){

    }
    public void calculateExpense(){

    }

}
