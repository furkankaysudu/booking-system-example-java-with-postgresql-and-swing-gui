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
                String selectedDateStr = (String) dateComboBox.getSelectedItem();
                LocalDate lDate = LocalDate.parse(selectedDateStr);

                if (selectedVehicleType == "Bus"){
                    Bus bus = new Bus(capacity, dSalary, eSalary, companyKey, lDate, tPrice, fuelPerKm);
                    bus.insertVehicle();
                }else if (selectedVehicleType == "Train") {
                    Train train = new Train(capacity, dSalary, eSalary, companyKey, lDate, tPrice, fuelPerKm);
                    train.insertVehicle();
                }
                else if(selectedVehicleType == "Airplane"){
                    AirPlane airPlane = new AirPlane(capacity, dSalary, eSalary, companyKey, lDate, tPrice, fuelPerKm);
                    airPlane.insertVehicle();
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

