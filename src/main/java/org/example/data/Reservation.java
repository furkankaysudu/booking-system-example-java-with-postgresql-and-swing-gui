package org.example.data;

import org.example.gui.VehicleReservationGUI;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Reservation {

    public Reservation() {
    }
    public List<Integer> smth(java.sql.Date sqlDate, String cityName1){

        List<Integer> vehicleKeys = new ArrayList<>();
        String qry = "SELECT r.vehicle_key, r.route FROM route r "
                + "JOIN vehicles v ON r.vehicle_key = v.id "
                + "WHERE v.date = ? ";
        //COMPLETED ADD A CONDİTİON OF DEPARTURE DATE

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")) {
            PreparedStatement statement = connection.prepareStatement(qry);
            statement.setDate(1,sqlDate);
            ResultSet rSet = statement.executeQuery();

            while (rSet.next()){
                int vehicleKey = rSet.getInt("vehicle_key");
                String route = rSet.getString("route");
                if (route.contains(cityName1))
                    vehicleKeys.add(vehicleKey);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicleKeys;
    }
    public Object smth2(Boolean isOneWay, String cityName2, List<Integer> vehicleKeys){

        int vKey ;
        String vehicleType;
        int capacity ;
        Date departureDate;
        String companyName;

        String query = "SELECT v.id, v.type, v.capacity, v.date, c.name AS name, r.route AS route \n" +
                "FROM vehicles v\n" +
                "JOIN route r ON v.id = r.vehicle_key\n" +
                "JOIN companies c ON c.id = v.company_key\n" +
                "WHERE r.vehicle_key = ? AND r.is_one_way = ?\n";

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setBoolean(2,isOneWay);

            for (Integer vehicleKey : vehicleKeys) {
                statement.setInt(1, vehicleKey);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String route = resultSet.getString("route");
                    if (route.contains(cityName2)){
                        vKey = resultSet.getInt("id");
                        vehicleType = resultSet.getString("type");
                        capacity = resultSet.getInt("capacity");
                        departureDate = resultSet.getDate("date");
                        companyName = resultSet.getString("name");
                        return new Object[]{vKey, vehicleType, capacity, companyName, departureDate, "Book Seat"};
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Object[];
    }
}
