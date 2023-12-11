package org.example.gui;

import org.example.data.Route;
import org.example.data.Trip;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddDestination extends JFrame {
    private JComboBox<String> vehicleComboBox;
    private JRadioButton[] routeOptions;
    private JCheckBox oneWayCheckBox;
    private String busRouteOne = "Istanbul - Kocaeli - Ankara";
    private String busRouteTwo = "Istanbul - Kocaeli - Eskişehir - Konya";
    private String trainRouteOne = "Istanbul - Kocaeli - Bilecik - Eskişehir - Ankara";
    private String trainRouteTwo = "Istanbul - Kocaeli - Bilecik - Eskişehir - Konya";
    private String airplaneRouteOne = "Istanbul - Konya";
    private String airplaneRouteTwo = "Istanbul - Ankara";
    private int company_key, id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AddDestination(int company_key) {
        this.company_key = company_key;

        setTitle("Vehicle Route Selector");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize components
        vehicleComboBox = new JComboBox<>();
        routeOptions = new JRadioButton[2]; // Two route options
        oneWayCheckBox = new JCheckBox("One Way");

        // Fetch vehicle types from the database and populate the combo box
        fetchVehicleTypes();

        // Set layout manager
        setLayout(new BorderLayout());

        // Add components to the frame
        add(vehicleComboBox, BorderLayout.NORTH);

        JPanel routePanel = new JPanel(new GridLayout(5, 1)); // Two route options and "One Way" checkbox
        routePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ButtonGroup routeGroup = new ButtonGroup();

        for (int i = 0; i < routeOptions.length; i++) {
            routeOptions[i] = new JRadioButton();
            routePanel.add(routeOptions[i]);
            routeGroup.add(routeOptions[i]);
        }

        routePanel.add(oneWayCheckBox);

        add(routePanel, BorderLayout.CENTER);

        // Add action listener to the combo box
        vehicleComboBox.addActionListener(e -> updateRouteOptions());

        for (JRadioButton routeOption : routeOptions) {
            routeOption.addActionListener(e -> handleRouteSelection());
        }
        oneWayCheckBox.addActionListener(e -> handleOneWaySelection());

        setDefaultCloseOperation(AddDestination.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void fetchVehicleTypes() {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")) {
            String sql = "SELECT id, type FROM vehicles WHERE company_key = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, company_key);
            ResultSet resultSet = statement.executeQuery();
            //LIST VEHICLES ID AND TYPES
            while (resultSet.next()) {
                setId(resultSet.getInt("id"));
                String vehicle = resultSet.getString("type");
                vehicleComboBox.addItem(String.valueOf(id) + "-" + vehicle);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateRouteOptions() {
        String selectedVehicle = (String) vehicleComboBox.getSelectedItem();
        clearRouteOptions();

        if (selectedVehicle != null) {
            switch (selectedVehicle.substring(3)) {
                case "Bus":
                    createRouteOptions(busRouteOne, busRouteTwo);
                    break;
                case "Train":
                    createRouteOptions(trainRouteOne, trainRouteTwo);
                    break;
                case "Airplane":
                    createRouteOptions(airplaneRouteOne, airplaneRouteTwo);
                    break;
                default:
                    break;
            }
        }
    }

    private void clearRouteOptions() {
        for (JRadioButton radio : routeOptions) {
            radio.setText("");
            radio.setVisible(false);
        }
        oneWayCheckBox.setVisible(false);
    }

    private void createRouteOptions(String routeOption1, String routeOption2) {
        routeOptions[0].setText(routeOption1);
        routeOptions[0].setVisible(true);

        routeOptions[1].setText(routeOption2);
        routeOptions[1].setVisible(true);

        oneWayCheckBox.setVisible(true);
    }
    private void handleRouteSelection() {
        String selectedRoute = getSelectedRoute();
        System.out.println("Selected Route: " + selectedRoute);
        Route route = new Route(selectedRoute);
        // Insert the selected route into the database
        insertRoute(selectedRoute, route.getDistance());
    }

    private void handleOneWaySelection() {
        boolean isOneWay = oneWayCheckBox.isSelected();
        String selectedRoute = getSelectedRoute();
        System.out.println("Is One Way: " + isOneWay);
        Route route = new Route(selectedRoute);
        // Update the One Way information in the database
        updateOneWayInfo(isOneWay, route.getDistance(), selectedRoute);
    }

    private String getSelectedRoute() {
        for (JRadioButton routeOption : routeOptions) {
            if (routeOption.isSelected()) {
                return routeOption.getText();
            }
        }
        return "";
    }

    private void insertRoute(String selectedRoute, int distance) {
        // Insert the selected route into the database
        String selectedVehicle = (String) vehicleComboBox.getSelectedItem();
        int selectedId = Integer.parseInt(selectedVehicle.substring(0,2));
        Boolean isoneWay = oneWayCheckBox.isSelected();
        System.out.println(selectedId);
        Trip trip = new Trip(selectedRoute, selectedId, distance, isoneWay);
        trip.insertDestination();
    }

    private void updateOneWayInfo(boolean isOneWay, int distance, String selectedRoute) {
        // Update the One Way information in the database
        String selectedVehicle = (String) vehicleComboBox.getSelectedItem();
        int selectedId = Integer.parseInt(selectedVehicle.substring(0,2));
        System.out.println(selectedId);
        Trip trip = new Trip(selectedRoute, selectedId, distance, isOneWay);
        trip.updateDestination();
    }

}
