package org.example.vehicles;

import java.util.List;

public abstract class Vehicle {

    private int ıd;
    private String oilType;
    private int capasity;
    private int driverSalary;
    private  int employeeSalary;
    public void calculateFuelCost(){

    }

    public Vehicle(int ıd, String oilType, int capasity, int driverSalary, int employeeSalary) {
        this.ıd = ıd;
        this.oilType = oilType;
        this.capasity = capasity;
        this.driverSalary = driverSalary;
        this.employeeSalary = employeeSalary;
    }

    public int getId() {
        return ıd;
    }

    public void setId(int ıd) {
        this.ıd = ıd;
    }

    public String getOilType() {
        return oilType;
    }

    public void setOilType(String oilType) {
        this.oilType = oilType;
    }

    public int getCapasity() {
        return capasity;
    }

    public void setCapasity(int capasity) {
        this.capasity = capasity;
    }

    public int getDriverSalary() {
        return driverSalary;
    }

    public void setDriverSalary(int driverSalary) {
        this.driverSalary = driverSalary;
    }

    public int getEmployeeSalary() {
        return employeeSalary;
    }

    public void setEmployeeSalary(int employeeSalary) {
        this.employeeSalary = employeeSalary;
    }
}
