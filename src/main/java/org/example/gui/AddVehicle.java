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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddVehicle extends JFrame {

    private JComboBox<String> vehicleTypeComboBox, dateComboBox, fuelTypeComboBox;
    private JTextField capacityField, driverSalaryField, employeeSalaryField, ticketPriceField, fuelPerKmField;
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

        JLabel fuelTypeLabel = new JLabel("Fuel Type");
        String[] fuelTypes = {"Benzin", "Motorin", "Elektrik", "Gaz"};
        fuelTypeComboBox = new JComboBox<>(fuelTypes);

        JLabel capacityLabel = new JLabel("Capacity:");
        capacityField = new JTextField();

        JLabel driverSalaryLabel = new JLabel("Driver Salary:");
        driverSalaryField = new JTextField();

        JLabel employeeSalaryLabel = new JLabel("Employee Salary:");
        employeeSalaryField = new JTextField();

        JLabel ticketPriceLabel = new JLabel("Ticket Price:");
        ticketPriceField = new JTextField();

        JLabel fuelLabel = new JLabel("Fuel Cost Per KM");
        fuelPerKmField = new JTextField();

        JLabel dateLabel = new JLabel("Date:");
        String[] allowedDates = {"2023-12-04", "2023-12-05", "2023-12-06", "2023-12-07", "2023-12-08", "2023-12-09", "2023-12-10"};
        dateComboBox = new JComboBox<>(allowedDates);
        add(dateLabel);
        add(dateComboBox);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform actions when OK button is clicked
                // For example, retrieve entered values and process them
                String selectedVehicleType = (String) vehicleTypeComboBox.getSelectedItem();

                String capacityS = capacityField.getText();
                int capacity = Integer.parseInt(capacityS);
                String driverSalary = driverSalaryField.getText();
                int dSalary = Integer.parseInt(driverSalary);
                String employeeSalary = employeeSalaryField.getText();
                int eSalary = Integer.parseInt(employeeSalary);
                String ticketPrice = ticketPriceField.getText();
                int tPrice = Integer.parseInt(ticketPrice);
                String fuelCost = fuelPerKmField.getText();
                int fuelPerKm = Integer.parseInt(fuelCost);
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
                    String selectedDateStr = (String) dateComboBox.getSelectedItem();

                    // Convert String to LocalDate
                    LocalDate lDate = LocalDate.parse(selectedDateStr);
                    // Convert LocalDate to java.sql.Date
                    java.sql.Date sqlDate = java.sql.Date.valueOf(lDate);

                    PreparedStatement st = connection.prepareStatement("INSERT INTO vehicles(capacity,driversalary,employeesalary, type, company_key,date, ticket_price, fuel_per_km) VALUES (?,?,?,?,?,?,?,?)");
                    st.setInt(1,capacity);
                    st.setInt(2,dSalary);
                    st.setInt(3,eSalary);
                    st.setString(4,vechicleType);
                    st.setInt(5,companyKey);
                    st.setDate(6, sqlDate);
                    st.setInt(7,tPrice);
                    st.setInt(8,fuelPerKm);
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
        add(ticketPriceLabel);
        add(ticketPriceField);
        add(fuelTypeLabel);
        add(fuelTypeComboBox);
        add(fuelLabel);
        add(fuelPerKmField);
        add(dateLabel);
        add(dateComboBox);
        add(okButton);

        setDefaultCloseOperation(AddVehicle.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}

