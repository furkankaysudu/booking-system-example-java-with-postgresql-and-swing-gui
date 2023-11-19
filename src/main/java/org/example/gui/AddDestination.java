package org.example.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AddDestination extends JFrame {
    private JComboBox<String> vehicleComboBox;
    private JList<String> cityList;
    private DefaultListModel<String> cityListModel;
    private int company_key,id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AddDestination(int company_key) {
        this.company_key = company_key;

        setTitle("Vehicle City Selector");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize components
        vehicleComboBox = new JComboBox<>();
        cityListModel = new DefaultListModel<>();
        cityList = new JList<>(cityListModel);
        cityList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Fetch vehicle types from the database and populate the combo box
        fetchVehicleTypes();

        JButton showSelectedCitiesButton = new JButton("Show Selected Cities");
        showSelectedCitiesButton.addActionListener(e -> showSelectedCities());

        // Set layout manager
        setLayout(new BorderLayout());

        // Add components to the frame
        add(vehicleComboBox, BorderLayout.NORTH);
        add(new JScrollPane(cityList), BorderLayout.CENTER);
        add(showSelectedCitiesButton, BorderLayout.SOUTH);

        // Add action listener to the combo box
        vehicleComboBox.addActionListener(e -> updateCityList());

        setVisible(true);
    }

    private void fetchVehicleTypes() {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")) {
            String sql = "SELECT id, type FROM vehicles WHERE company_key= ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, company_key);
            ResultSet resultSet = statement.executeQuery();
            //LİST VEHİCLES İD AND TYPES
            while (resultSet.next()) {
                setId(resultSet.getInt("id"));
                String vehicle = resultSet.getString("type");
                vehicleComboBox.addItem(String.valueOf(id) + "-" + vehicle);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateCityList() {
        String selectedVehicle = (String) vehicleComboBox.getSelectedItem();
        cityListModel.clear();

        if (selectedVehicle != null) { //TODO CECK TYPE AT TOP THEN CHOOSE VEHİCLEID

            switch (selectedVehicle.substring(2,selectedVehicle.length())) {
                case "Bus":
                    String[] busCities = {"Istanbul", "Kocaeli", "Ankara", "Eskisehir", "Konya"};
                    for (String city : busCities) {
                        cityListModel.addElement(city);
                    }
                    break;
                case "Train":
                    String[] trainCities = {"Istanbul", "Kocaeli", "Bilecik", "Ankara", "Eskisehir", "Konya"};
                    for (String city : trainCities) {
                        cityListModel.addElement(city);
                    }
                    break;
                case "Airplane":
                    String[] airplaneCities = {"Istanbul", "Ankara", "Konya"};
                    for (String city : airplaneCities) {
                        cityListModel.addElement(city);
                    }
                    break;
                default:
                    break;
            }
        }
    }
    private void showSelectedCities() {
        String[] selectedCities = cityList.getSelectedValuesList().toArray(new String[0]);
        StringBuilder message = new StringBuilder("Selected Cities:\n");
        //TODO İNSERT CİTİES ACCORDİNG TO VEHİCLEID

        for (int i = 0; i< (selectedCities.length);i++){
            try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")){
                PreparedStatement st = connection.prepareStatement("INSERT INTO route(destination,vehicle_key) VALUES (?,?)");
                st.setString(1,selectedCities[i]);
                st.setInt(2,getId());
                st.executeUpdate();
                st.close();

            }catch (SQLException error){
                error.printStackTrace();
            }

        }

        for (String city : selectedCities) {
            message.append(city).append("\n");
        }
        JOptionPane.showMessageDialog(this, message.toString(), "Selected Cities", JOptionPane.INFORMATION_MESSAGE);
    }

}

