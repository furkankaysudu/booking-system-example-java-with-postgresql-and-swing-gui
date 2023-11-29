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
    private JCheckBox[] cityCheckBoxes;
    private String[] busCities = {"Istanbul", "Kocaeli", "Ankara", "Eskisehir", "Konya"};
    private String[] trainCities = {"Istanbul", "Kocaeli", "Bilecik", "Ankara", "Eskisehir", "Konya"};
    private String[] airplaneCities = {"Istanbul", "Ankara", "Konya"};
    private int company_key, id;

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

        // Set layout manager
        setLayout(new BorderLayout());

        // Add components to the frame
        add(vehicleComboBox, BorderLayout.NORTH);
        add(new JScrollPane(cityList), BorderLayout.CENTER);

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

            switch (selectedVehicle.substring(2, selectedVehicle.length())) {
                case "Bus":
                    createCheckBoxes(busCities);
                    break;
                case "Train":
                    createCheckBoxes(trainCities);
                    break;
                case "Airplane":
                    createCheckBoxes(airplaneCities);
                    break;
                default:
                    break;
            }
        }
    }

    private void createCheckBoxes(String[] cities) {
        JPanel cityPanel = new JPanel(new GridLayout(cities.length + 1, 1)); // +1 for the button
        cityCheckBoxes = new JCheckBox[cities.length];

        for (int i = 0; i < cities.length; i++) {
            cityCheckBoxes[i] = new JCheckBox(cities[i]);
            cityCheckBoxes[i].setSelected(false);
            cityCheckBoxes[i].setActionCommand(cities[i]);
            cityPanel.add(cityCheckBoxes[i]);
        }

        JButton showSelectedCitiesButton = new JButton("Show Selected Cities");
        showSelectedCitiesButton.addActionListener(e -> showSelectedCities());
        cityPanel.add(showSelectedCitiesButton);

        // Remove the existing components in the content pane
        getContentPane().removeAll();

        // Add the panel containing checkboxes to the content pane
        getContentPane().add(cityPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private void showSelectedCities() {
        StringBuilder message = new StringBuilder("Selected Cities:\n");

        for (JCheckBox checkBox : cityCheckBoxes) {
            if (checkBox.isSelected()) {
                String city = checkBox.getActionCommand();
                message.append(city).append("\n");
                try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")) {
                    PreparedStatement st = connection.prepareStatement("INSERT INTO route(destination,vehicle_key) VALUES (?,?)");
                    st.setString(1, city);
                    st.setInt(2, getId());
                    st.executeUpdate();
                    st.close();
                } catch (SQLException error) {
                    error.printStackTrace();
                }
            }
        }

        // Display selected cities in a dialog
        JOptionPane.showMessageDialog(this, message.toString(), "Selected Cities", JOptionPane.INFORMATION_MESSAGE);
    }
}

