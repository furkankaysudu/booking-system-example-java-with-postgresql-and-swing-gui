package org.example.data;

import org.example.vehicles.Bus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Trip {

    private String selectedRoute;
    private int distance, selectedVehicleId;
    private Boolean isOneWay;

    public Trip(String selectedRoute, int selectedVehicleId, int distance, Boolean isOneWay) {
        this.selectedRoute = selectedRoute;
        this.selectedVehicleId = selectedVehicleId;
        this.distance = distance;
        this.isOneWay = isOneWay;
    }

    public String getSelectedRoute() {
        return selectedRoute;
    }

    public int getDistance() {
        return distance;
    }

    public int getSelectedVehicleId() {
        return selectedVehicleId;
    }

    public Boolean getOneWay() {
        return isOneWay;
    }

    public void insertDestination(){

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO route(route, is_one_way, distance, vehicle_key) VALUES (?, ?, ?, ?)");
            statement.setString(1, selectedRoute);
            statement.setBoolean(2, getOneWay());
            statement.setInt(3, distance);
            statement.setInt(4, getSelectedVehicleId());
            statement.executeUpdate();
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }
    public void updateDestination(){

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")) {
            PreparedStatement statement = connection.prepareStatement("UPDATE route SET route = ?, is_one_way = ?, distance = ? WHERE vehicle_key = ?");
            statement.setString(1, getSelectedRoute());
            statement.setBoolean(2, getOneWay());
            statement.setInt(3, getDistance());
            statement.setInt(4, getSelectedVehicleId());
            statement.executeUpdate();
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }
}
