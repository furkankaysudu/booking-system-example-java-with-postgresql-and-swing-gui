package org.example.gui;

import org.example.users.Company;
import org.example.vehicles.Bus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

public class CompanyPage extends JFrame {
    private JTextArea vehicleTextArea;
    private JLabel companyNameLabel;
    private JComboBox selectDateBox;
    private JTextPane incomeField, expenseField, balanceField;
    private List<String> vehicleInfoList = new ArrayList<>();
    private List<Integer> foundIds = new ArrayList<>();
    private int companyId, income, expense, balance;
    private String  userName, password;
    private String[] dates = {"2023-12-04", "2023-12-05", "2023-12-06", "2023-12-07", "2023-12-08", "2023-12-09", "2023-12-10"};

    public CompanyPage(int companyId, String userName, String password) {
        this.companyId = companyId;
        this.userName = userName;
        this.password = password;
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
        // Inside the constructor
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                fetchDataFromDatabase();
                return null;
            }

            @Override
            protected void done() {
                // Update UI if needed after fetching data
                vehicleTextArea.setText(String.join("\n", vehicleInfoList));
            }
        };
        worker.execute();


    }
    private void fetchDataFromDatabase() {
        int i = 0;

        Company company = new Company(userName, new StringBuilder(password));

        List<Company.Vehicle> infoList = company.getVehicleInfo(companyId);
        ListIterator<Company.Vehicle> iterator = infoList.listIterator();

        while (iterator.hasNext()){

            Company.Vehicle vehicle = infoList.get(i);
            String item = "id: " +vehicle.getId()+" ||type: " + vehicle.getType() + " ||capacity: " + vehicle.getCapacity() +
                " ||driver salary = " + vehicle.getDriversalary() + " ||employee salary: " + vehicle.getEmployeesalary() +
                    " ||ticket price: " + vehicle.getTicket_price() + " ||departure date: " + vehicle.getDepatureDate();

            vehicleInfoList.add(item);
            i++;
        }

        vehicleTextArea.setText(String.join("\n", vehicleInfoList));

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
                calculationPopUp.setPopupSize(400, 50);
                calculationPopUp.setVisible(true);
                calculationPopUp.setLayout(new GridLayout(2,3));

                selectDateBox = new JComboBox<>(dates);
                calculationPopUp.add(selectDateBox);

                JButton okButton = new JButton("OK");
                okButton.setSize(10,10);
                calculationPopUp.add(okButton);

                JButton totalButton = new JButton("total");
                okButton.setSize(10,10);
                calculationPopUp.add(totalButton);

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

                    String selectedDate = selectDateBox.getSelectedItem().toString();
                    findId(selectedDate); // Pass the selected date to findId method
                    setFields();
                });

                totalButton.addActionListener(e -> {

                    Company company = new Company(userName, new StringBuilder(password));
                    income = company.calculateTotalIncome(companyId);
                    expense = company.calculateTotalExpense(companyId);
                    balance = company.totalBalance(income, expense);

                    incomeField.setText(String.valueOf(income));
                    expenseField.setText(String.valueOf(expense));
                    balanceField.setText(String.valueOf(balance));
                });
                // Display the popup
            }
            public void findId(String selectedDate) {
                // Use the selected date value
                int i = 0;

                Company company = new Company(userName, new StringBuilder(password));
                List<Company.Vehicle> infoList = company.getVehicleInfo(companyId);

                for (String searchString : vehicleInfoList) {
                    Company.Vehicle vehicle = infoList.get(i);
                    if (vehicle.getDepatureDate().toString().equals(selectedDate)) {
                        foundIds.add(vehicle.getId());
                    }
                    i++;
                }
                System.out.println(foundIds);
            }
            public void setFields() {
                // Reset values before calculations

                Company company = new Company(userName, new StringBuilder(password));

                for (int id : foundIds) {
                    income += company.calculateIncome(id, companyId);
                    expense += company.calculateExpense(id, companyId);
                    balance += company.dailyBalance(income,expense);

                    System.out.println(income + " " +expense + " " + balance);
                }
                    // Set the fields outside the loops
                incomeField.setText(String.valueOf(income));
                expenseField.setText(String.valueOf(expense));
                balanceField.setText(String.valueOf(balance));
            }
        });
    }
}
