package org.example.vehicles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AirPlane extends Vehicle{

    public AirPlane(int capasity, int driverSalary, int employeeSalary, int companyKey, LocalDate departureDate, int ticketPrice, int fuelPerKm) {
        super(capasity, driverSalary, employeeSalary, companyKey, departureDate, ticketPrice, fuelPerKm);
    }

    public void insertVehicle(){
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")){

            // Convert LocalDate to java.sql.Date
            java.sql.Date sqlDate = java.sql.Date.valueOf(getDepartureDate());

            PreparedStatement st = connection.prepareStatement("INSERT INTO vehicles(capacity,driversalary,employeesalary, type, company_key, date, ticket_price, fuel_per_km) VALUES (?,?,?,?,?,?,?,?)");
            st.setInt(1, getCapasity());
            st.setInt(2, getDriverSalary());
            st.setInt(3, getEmployeeSalary());
            st.setString(4, "Airplane");
            st.setInt(5, getCompanyKey());
            st.setDate(6, sqlDate);
            st.setInt(7, getTicketPrice());
            st.setInt(8, getFuelPerKm());
            st.executeUpdate();
            st.close();
        }catch (SQLException error){
            error.printStackTrace();
        }
    }
}
