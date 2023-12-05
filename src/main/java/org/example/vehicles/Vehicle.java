package org.example.vehicles;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public abstract class Vehicle {


    private int capasity, driverSalary, employeeSalary, companyKey, ticketPrice, fuelPerKm;
    private LocalDate departureDate;


    public Vehicle(int capasity, int driverSalary, int employeeSalary, int companyKey, LocalDate departureDate, int ticketPrice, int fuelPerKm) {
        this.capasity = capasity;
        this.driverSalary = driverSalary;
        this.employeeSalary = employeeSalary;
        this.companyKey = companyKey;
        this.departureDate = departureDate;
        this.ticketPrice = ticketPrice;
        this.fuelPerKm = fuelPerKm;

    }

    public int getCapasity() {
        return capasity;
    }

    public int getDriverSalary() {
        return driverSalary;
    }

    public int getEmployeeSalary() {
        return employeeSalary;
    }

    public int getCompanyKey() {
        return companyKey;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public int getFuelPerKm() {
        return fuelPerKm;
    }
}
