package org.example.vehicles;

import java.util.List;

public class Bus extends Vehicle{

    @Override
    public void setOilType(String oilType) {
        super.setOilType("Benzin");
    }

    public Bus(int ıd, String oilType, int capasity, int driverSalary, int employeeSalary) {
        super(ıd, oilType, capasity, driverSalary, employeeSalary);
    }

}
