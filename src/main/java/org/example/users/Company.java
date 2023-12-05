package org.example.users;

import org.example.vehicles.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

public class Company extends User implements IProfitable{

    public Company(String userName, StringBuilder password) {
        super(userName, password);
    }

    @Override
    public int dailyBalance(int income, int expense) {
            return income - expense;
    }

    @Override
    public int totalBalance(int totalIncome, int totalExpense) {
        return totalIncome - totalExpense;
    }

    @Override
    public int calculateIncome(int id, int companyKey) {
        // Reset values before calculations
        int income = 0, i;

        List<Vehicle> info = getVehicleInfo(companyKey);


        String query = "SELECT is_one_way FROM route WHERE vehicle_key = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Boolean is_one_way = resultSet.getBoolean("is_one_way");


                for (i = 0; i < info.size(); i++) {
                    Company.Vehicle vehicle = info.get(i);
                    if (vehicle.getId() == id){
                        int capacity = vehicle.getCapacity();
                        int ticketPrice = vehicle.getTicket_price();
                        income += (!is_one_way) ? ((capacity * ticketPrice) * 2) : (capacity * ticketPrice);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return income;
    }

    @Override
    public int calculateExpense(int id, int companyKey) {
        int expense = 0, i;


        List<Vehicle> info = getVehicleInfo(companyKey);

        String query = "SELECT distance, is_one_way FROM route WHERE vehicle_key = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int distance = resultSet.getInt("distance");
                Boolean is_one_way = resultSet.getBoolean("is_one_way");

                for (i = 0; i < info.size(); i++) {
                    Company.Vehicle vehicle = info.get(i);
                    if (vehicle.getId() == id) {
                        int driverSalary = vehicle.getDriversalary();
                        int employeeSalary = vehicle.getEmployeesalary();
                        int fuelPerKm = vehicle.getFuelPerKm();

                        expense += driverSalary + employeeSalary;
                        expense += (!is_one_way) ? ((distance * fuelPerKm) * 2) : (distance * fuelPerKm);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expense;
    }

    @Override
    public int calculateTotalIncome() {
        return 0;
    }

    @Override
    public int calculateTotalExpense() {
        return 0;
    }
    public class Vehicle {
        private int id;
        private String type;
        private int capacity;
        private int driversalary;
        private int employeesalary;
        private int ticket_price ;
        private int fuelPerKm ;
        private Date depatureDate;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getCapacity() {
            return capacity;
        }

        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        public int getDriversalary() {
            return driversalary;
        }

        public void setDriversalary(int driversalary) {
            this.driversalary = driversalary;
        }

        public int getEmployeesalary() {
            return employeesalary;
        }

        public void setEmployeesalary(int employeesalary) {
            this.employeesalary = employeesalary;
        }

        public int getTicket_price() {
            return ticket_price;
        }

        public void setTicket_price(int ticket_price) {
            this.ticket_price = ticket_price;
        }

        public int getFuelPerKm() {
            return fuelPerKm;
        }

        public void setFuelPerKm(int fuelPerKm) {
            this.fuelPerKm = fuelPerKm;
        }

        public Date getDepatureDate() {
            return depatureDate;
        }

        public void setDepatureDate(Date depatureDate) {
            this.depatureDate = depatureDate;
        }
    }

    public List<Vehicle> getVehicleInfo(int companyId) {

        List<Vehicle> vehicles = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")) {
            String sql = "SELECT v.id, v.capacity, v.type, v.driversalary, v.employeesalary, v.type, v.ticket_price, v.fuel_per_km, v.date"+
                    " FROM vehicles v WHERE company_key = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, companyId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setId(resultSet.getInt("id"));
                vehicle.setType(resultSet.getString("type"));
                vehicle.setCapacity(resultSet.getInt("capacity"));
                vehicle.setDriversalary(resultSet.getInt("driversalary"));
                vehicle.setEmployeesalary(resultSet.getInt("employeesalary"));
                vehicle.setTicket_price(resultSet.getInt("ticket_price"));
                vehicle.setFuelPerKm(resultSet.getInt("fuel_per_km"));
                vehicle.setDepatureDate(resultSet.getDate("date"));


                vehicles.add(vehicle);
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }
        return vehicles;
    }

}
