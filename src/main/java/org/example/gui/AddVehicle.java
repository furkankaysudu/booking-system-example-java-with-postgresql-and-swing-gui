package org.example.gui;

import org.example.vehicles.AirPlane;
import org.example.vehicles.Bus;
import org.example.vehicles.Train;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddVehicle extends JFrame {

    private JComboBox<String> vehicleTypeComboBox;
    private JTextField capacityField, driverSalaryField, employeeSalaryField, cityNameField;
    private JTextArea cityNamesArea;
    private List<String> cityList;
    private int companyKey;

    public AddVehicle(int companyKey) {
        this.companyKey = companyKey;

        setTitle("Vehicle Information");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 2));

        JLabel vehicleTypeLabel = new JLabel("Vehicle Type:");
        String[] vehicleTypes = {"Bus", "Train", "Airplane"};
        vehicleTypeComboBox = new JComboBox<>(vehicleTypes);

        JLabel capacityLabel = new JLabel("Capacity:");
        capacityField = new JTextField();

        JLabel driverSalaryLabel = new JLabel("Driver Salary:");
        driverSalaryField = new JTextField();

        JLabel employeeSalaryLabel = new JLabel("Employee Salary:");
        employeeSalaryField = new JTextField();

        JLabel cityNameLabel = new JLabel("City Name:");
        cityNameField = new JTextField();
        JButton addCityButton = new JButton("Add City");
        addCityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCityName();
            }
        });

        JLabel cityNamesLabel = new JLabel("City Names:");
        cityNamesArea = new JTextArea();
        cityNamesArea.setEditable(false);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform actions when OK button is clicked
                // For example, retrieve entered values and process them
                String selectedVehicleType = (String) vehicleTypeComboBox.getSelectedItem();
                String capacity = capacityField.getText();
                int capasity = Integer.parseInt(capacity);
                String driverSalary = driverSalaryField.getText();
                int dSalary = Integer.parseInt(driverSalary);
                String employeeSalary = employeeSalaryField.getText();
                int eSalary = Integer.parseInt(employeeSalary);
                String vechicleType = " ";

                if (selectedVehicleType == "Bus"){
                    //Bus newBus = new Bus(Integer.parseInt(vehicleId), "Benzin", Integer.parseInt(capacity), Integer.parseInt(driverSalary), Integer.parseInt(employeeSalary));
                    vechicleType = "Bus";
                }else if (selectedVehicleType == "Train") {
                   // Train newTrain = new Train(Integer.parseInt(vehicleId), "Electric", Integer.parseInt(capacity), Integer.parseInt(driverSalary), Integer.parseInt(employeeSalary));
                    vechicleType = "Train";
                }
                else if(selectedVehicleType == "Airplane"){
                    //AirPlane newAirplane = new AirPlane(Integer.parseInt(vehicleId), "Gas", Integer.parseInt(capacity), Integer.parseInt(driverSalary), Integer.parseInt(employeeSalary));
                    vechicleType = "Airplane";
                }else {
                    vechicleType = "bilinmiyor";
                }
                // You can also retrieve city names from the cityList
                try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")){
                    PreparedStatement st = connection.prepareStatement("INSERT INTO vehicles(capasity,driversalary,employeesalary, type, company_key) VALUES (?,?,?,?,?)");
                    st.setInt(1,capasity);
                    st.setInt(2,dSalary);
                    st.setInt(3,eSalary);
                    st.setString(4,vechicleType);
                    st.setInt(5,companyKey);
                    st.executeUpdate();
                    st.close();

                }catch (SQLException error){
                    error.printStackTrace();
                }
            }
        });

        // Adding components to the frame
        add(vehicleTypeLabel);
        add(vehicleTypeComboBox);
        add(capacityLabel);
        add(capacityField);
        add(driverSalaryLabel);
        add(driverSalaryField);
        add(employeeSalaryLabel);
        add(employeeSalaryField);
        add(cityNameLabel);
        add(cityNameField);
        add(addCityButton);
        add(cityNamesLabel);
        add(cityNamesArea);
        add(okButton);

        cityList = new ArrayList<>();

        setVisible(true);
    }

    private void addCityName() {
        String cityName = cityNameField.getText();
        if (!cityName.isEmpty()) {
            cityList.add(cityName);
            cityNameField.setText("");
            updateCityNamesArea();
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a city name.");
        }
    }

    private void updateCityNamesArea() {
        StringBuilder builder = new StringBuilder();
        for (String city : cityList) {
            builder.append(city).append("\n");
        }
        cityNamesArea.setText(builder.toString());
    }

}

