package org.example.gui;

import org.example.users.Admin;
import org.example.users.Company;

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
    private StringBuilder password = new StringBuilder("1234");
    private String userName = "admin";

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
        Admin admin = new Admin(userName, password);
        for (int i = 0; i<admin.fetchCompanyNames().size();i++){
            companyListModel.addElement(admin.fetchCompanyNames().get(i));
        }

    }

    private void setServicePrice() {
        String newPrice = servicePriceField.getText();
        int servicePrice = Integer.parseInt(newPrice);
        Admin admin = new Admin(userName,password);
        admin.setServicePrice(servicePrice);

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

            Company company = new Company(companyName, new StringBuilder(companyPassword));

            Admin admin = new Admin(userName, password);
            admin.addCompany(company.getUserName(), company.getPassword().toString());

            companyListModel.addElement(companyName);
            JOptionPane.showMessageDialog(this, "New company added: " + companyName);
        }
    }


    private void deleteCompany() {
        String selectedCompany = companyList.getSelectedValue();
        if (selectedCompany != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + selectedCompany + "?");
            if (confirm == JOptionPane.YES_OPTION) {

                companyListModel.removeElement(selectedCompany);

                Admin admin = new Admin(userName, password);
                admin.deleteCompany(selectedCompany);
                JOptionPane.showMessageDialog(this, selectedCompany + " deleted.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a company to delete.");
        }
    }
}
