package org.example.data;

import java.sql.*;

public class Personel extends Person{
    public Personel(String name, String surName, int driverSalary, int employeeSalary) {
        super(name, surName);
        this.driverSalary = driverSalary;
        this.employeeSalary = employeeSalary;
    }

    private int driverSalary, employeeSalary;

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

    public int personelInfo(int vehicleId){


        String query = "SELECT driversalary, employeesalary FROM vehicles WHERE id = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")){
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,vehicleId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                setDriverSalary(resultSet.getInt("driversalary"));
                setEmployeeSalary(resultSet.getInt("employeesalary"));
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return getDriverSalary()+getEmployeeSalary();
    }
}
