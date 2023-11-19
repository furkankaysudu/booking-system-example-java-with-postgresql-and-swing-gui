package org.example.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyPage extends JFrame {
    private JTextArea vehicleTextArea;
    private JLabel companyNameLabel;

    private int companyId;

    public CompanyPage(int companyId) {
        this.companyId = companyId;
        // Set up the main frame
        setTitle("Vehicle Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize components
        companyNameLabel = new JLabel("Company Name");

        JPanel buttonsPanel = new JPanel();
        JButton addVehicleButton = new JButton("Add Vehicle");
        JButton addTripButton = new JButton("Add Destination");
        JButton calculateBalance = new JButton("Calculate Balance");
        buttonsPanel.add(addVehicleButton);
        buttonsPanel.add(addTripButton);
        buttonsPanel.add(calculateBalance);

        // Text area for displaying vehicles and capacities
        vehicleTextArea = new JTextArea(20, 30);
        vehicleTextArea.setEditable(false);
        JScrollPane vehicleScrollPane = new JScrollPane(vehicleTextArea);

        // Add components to the frame
        add(companyNameLabel, BorderLayout.NORTH);
        add(vehicleScrollPane, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        // Add action listeners
        addVehicleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the add vehicle page
                openAddVehiclePage();
            }
        });

        addTripButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddDestinationPage();
            }
        });

        calculateBalance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                balanceCalculator();
            }
        });

        // Fetch data from the database and populate the vehicle list
        fetchDataFromDatabase();
    }

    private void fetchDataFromDatabase() {
        List<String> vehicleInfoList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")) {
            String sql = "SELECT capasity, type FROM vehicles WHERE company_key = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, companyId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String type = resultSet.getString("type");
                int capasity = resultSet.getInt("capasity");

                String vehicleInfo = type + " (Koltuk Sayısı: " + capasity + ")";
                vehicleInfoList.add(vehicleInfo);
            }

            vehicleTextArea.setText(String.join("\n", vehicleInfoList));
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    private void openAddVehiclePage() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Open a new window or dialog for adding a vehicle
                new AddVehicle(companyId);

            }
        });
    }

    private void openAddDestinationPage(){

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JPanel panel = new JPanel();
                panel.add(new AddDestination(companyId));
            }
        });
    }
    private void balanceCalculator(){
        //TODo SELECT İNCOME AND EXPENSES THEN CALCULATE AND DİSPLAY BALANCE

    }
}
