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
    private JComboBox selectDateBox;
    private JTextPane incomeField, expenseField, balanceField;
    private List<String> vehicleInfoList = new ArrayList<>();
    private List<Integer> foundIds = new ArrayList<>();
    private List<Integer> counts = new ArrayList<>();
    private List<List<Integer>> info = new ArrayList<List<Integer>>();
    private int companyId, vID, capacity, driversalary, employeesalary, ticket_price, distance, fuelPerKm, income, expense, balance;
    private String type;
    private String[] dates = {"2023-12-04", "2023-12-05", "2023-12-06", "2023-12-07", "2023-12-08", "2023-12-09", "2023-12-10", };
    private Boolean is_one_way;
    private Date depatureDate;

    public CompanyPage(int companyId) {
        this.companyId = companyId;
        // Set up the main frame
        setTitle("Vehicle Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        selectDateBox = new JComboBox<>(dates);
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
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")) {
            String sql = "SELECT v.id, v.capacity, v.type, v.driversalary, v.employeesalary, v.type, v.ticket_price, v.fuel_per_km, v.date"+
                    " FROM vehicles v WHERE company_key = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, companyId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                vID = resultSet.getInt("id");
                type = resultSet.getString("type");
                capacity = resultSet.getInt("capacity");
                driversalary = resultSet.getInt("driversalary");
                employeesalary = resultSet.getInt("employeesalary");
                ticket_price = resultSet.getInt("ticket_price");
                fuelPerKm = resultSet.getInt("fuel_per_km");
                depatureDate = resultSet.getDate("date");

                String vehicleInfo ="(id: " + vID + " ) || " + type + " ||" +
                        " seat count: " + capacity + " || " +
                        " driever salary: " + driversalary + " || "  +
                        " employee salary: " + employeesalary + " || " +
                        " ticket price: " + ticket_price + " || " +
                        " departure date: " + depatureDate ;
                vehicleInfoList.add(vehicleInfo);

                // Create a new list for each set of vehicle information
                List<Integer> infoItem = new ArrayList<>();
                infoItem.add(vID);
                infoItem.add(capacity);
                infoItem.add(driversalary);
                infoItem.add(employeesalary);
                infoItem.add(ticket_price);
                infoItem.add(fuelPerKm);
                infoItem.add(companyId);

                // Add the new list to the info list
                info.add(infoItem);
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
                new AddDestination(companyId);
            }
        });
    }
    private void balanceCalculator(){
        //COMPLETE SELECT İNCOME AND EXPENSES THEN CALCULATE AND DİSPLAY BALANCE according to date
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JPopupMenu calculationPopUp = new JPopupMenu();
                calculationPopUp.setPopupSize(200, 50);
                calculationPopUp.setVisible(true);
                calculationPopUp.setLayout(new GridLayout(2,0));

                selectDateBox = new JComboBox<>(dates);
                calculationPopUp.add(selectDateBox);

                JButton okButton = new JButton("OK");
                okButton.setSize(10,10);
                calculationPopUp.add(okButton);

                incomeField = new JTextPane();
                expenseField = new JTextPane();
                balanceField = new JTextPane();
                JLabel emptyLabel = new JLabel();

                calculationPopUp.add(emptyLabel);
                calculationPopUp.add(incomeField);
                calculationPopUp.add(expenseField);
                calculationPopUp.add(balanceField);

                // Action listener for the OK button
                okButton.addActionListener(e -> {
                    // Get the selected date from the combo box
                    foundIds.clear();
                    String selectedDate = selectDateBox.getSelectedItem().toString();
                    findId(selectedDate); // Pass the selected date to findId method
                    setFields();
                });
                // Display the popup
            }
            public void findId(String selectedDate) {
                // Use the selected date value
                for (String searchString : vehicleInfoList) {
                    if (searchString.contains(selectedDate) && searchString.contains(String.valueOf(companyId))) {
                        int idStart = searchString.indexOf("id: ") + 4;
                        int idEnd = searchString.indexOf(")", idStart);
                        if (idStart != -1 && idEnd != -1) {
                            String idPart = searchString.substring(idStart, idEnd).trim();
                            int id = Integer.parseInt(idPart);
                            foundIds.add(id); // Add the found ID to the list
                        }
                    }
                }
                System.out.println(foundIds);
            }
            public void setFields() {
                // Reset values before calculations
                income = 0;
                expense = 0;
                balance = 0;

                String query = "SELECT distance, is_one_way FROM route WHERE vehicle_key = ?";
                try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")) {
                    PreparedStatement statement = connection.prepareStatement(query);
                    for (int id : foundIds) {
                        statement.setInt(1, id);
                        ResultSet resultSet = statement.executeQuery();

                        while (resultSet.next()) {
                            distance = resultSet.getInt("distance");
                            is_one_way = resultSet.getBoolean("is_one_way");

                            // Iterate through the vehicle information
                            for (List<Integer> o : info) {
                                if (o.get(0) == id) {
                                    // Calculate income, expense, and balance
                                    income += (!is_one_way) ? ((o.get(1) * o.get(4))*2) : (o.get(1) * o.get(4));
                                    expense += o.get(2) + o.get(3);
                                    expense += (!is_one_way) ? (distance * o.get(5))*2 : (distance * o.get(5));
                                    balance += income - expense;
                                    System.out.println(income + " " + expense + " " + balance);
                                }
                            }
                        }
                    }
                    // Set the fields outside the loops
                    incomeField.setText(String.valueOf(income));
                    expenseField.setText(String.valueOf(expense));
                    balanceField.setText(String.valueOf(balance));
                    // Remaining code...
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
