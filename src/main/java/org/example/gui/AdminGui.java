package org.example.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminGui extends JFrame {
    private JList<String> companyList;
    private DefaultListModel<String> companyListModel;
    private JTextField servicePriceField;
    private JButton setServicePriceButton;
    private JButton addCompanyButton;
    private JButton deleteCompanyButton;
    public AdminGui() {
        setTitle("Admin Panel");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize components
        companyListModel = new DefaultListModel<>();
        companyList = new JList<>(companyListModel);
        servicePriceField = new JTextField("1000", 1);
        setServicePriceButton = new JButton("Set Service Price");
        addCompanyButton = new JButton("Add Company");
        deleteCompanyButton = new JButton("Delete Company");

        // Fetch company names from the database and populate the company list
        fetchCompanyNames();

        // Set layout manager
        setLayout(new BorderLayout());

        // Left panel for company names
        JPanel leftPanel = new JPanel(new BorderLayout());
        JLabel companyNames = new JLabel("Companie Names");
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 50, 20));
        leftPanel.add(companyNames,BorderLayout.NORTH);
        leftPanel.add(companyList, BorderLayout.CENTER);

        // Right panel for service price
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 100, 50, 10));
        rightPanel.add(new JLabel("Service Price: "), BorderLayout.NORTH);
        rightPanel.add(servicePriceField, BorderLayout.CENTER);
        rightPanel.add(setServicePriceButton, BorderLayout.SOUTH);

        // Button panel for adding and deleting companies
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(addCompanyButton);
        buttonPanel.add(deleteCompanyButton);

        // Add components to the frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action listeners
        setServicePriceButton.addActionListener(e -> setServicePrice());
        addCompanyButton.addActionListener(e -> addCompany());
        deleteCompanyButton.addActionListener(e -> deleteCompany());

        setVisible(true);
    }

    private void fetchCompanyNames() {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT name FROM companies");

            while (resultSet.next()) {
                String companyName = resultSet.getString("name");
                companyListModel.addElement(companyName);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void setServicePrice() {
            String newPrice = servicePriceField.getText();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")){
            PreparedStatement st = connection.prepareStatement("INSERT INTO admin(serviceprice) VALUES (?)");
            st.setInt(1,Integer.parseInt(newPrice));
            st.executeUpdate();
            st.close();
        }catch (SQLException error){
            error.printStackTrace();
        }
    }

    private void addCompany() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField companyNameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);

        panel.add(new JLabel("Company Name:"));
        panel.add(companyNameField);
        panel.add(new JLabel("Company Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Enter Company Information",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String companyName = companyNameField.getText();
            char[] passwordChars = passwordField.getPassword();
            String companyPassword = new String(passwordChars);

            try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")){
                PreparedStatement st = connection.prepareStatement("INSERT INTO companies(name,password) VALUES (?,?)");
                st.setString(1,companyName);
                st.setString(2,companyPassword);
                st.executeUpdate();
                st.close();
            }catch (SQLException error){
                error.printStackTrace();
            }

            // Logic to add a new company to the database using companyName and companyPassword
            // Your database insertion logic here...

            // Update GUI with the new company (e.g., add to the companyListModel)
            companyListModel.addElement(companyName);
            JOptionPane.showMessageDialog(this, "New company added: " + companyName);
        }
    }


    private void deleteCompany() {
        String selectedCompany = companyList.getSelectedValue();
        if (selectedCompany != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + selectedCompany + "?");
            if (confirm == JOptionPane.YES_OPTION) {
                // Logic to delete the selected company from the database
                // Your database deletion logic here...
                companyListModel.removeElement(selectedCompany);
                try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")){
                    String sql = "DELETE FROM companies WHERE name = ?";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1,selectedCompany);
                    statement.executeUpdate();
                    statement.close();

                }catch (SQLException error) {
                    error.printStackTrace();
                }

                JOptionPane.showMessageDialog(this, selectedCompany + " deleted.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a company to delete.");
        }
    }
}
