package org.example.gui;

import org.example.users.AuthenticatedUserInfo;
import org.example.users.User;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginGui extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, searchButton;

    public LoginGui() {
        setTitle("Login Page");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize components
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");
        searchButton = new JButton("Search Ticket");
        JLabel buttonLabel = new JLabel();
        // Set layout manager
        setLayout(new GridLayout(4, 2));

        // Add components to the frame
        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(new JLabel(""));
        add(loginButton);
        add(buttonLabel);
        add(searchButton);

        // Add action listener to the login button
        loginButton.addActionListener(e -> authenticate());
        searchButton.addActionListener(e -> openBookTicketPage());

        setVisible(true);
    }
    private void authenticate() {
        String username = usernameField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

        User user = new User(username, new StringBuilder(password)) {

        };
        AuthenticatedUserInfo authenticatedUserInfo = user.authenticatedUser();
        switch (authenticatedUserInfo.getUserType()){
            case NONE -> JOptionPane.showMessageDialog(this, "Invalid username or password.");
            case ADMIN -> {
                JOptionPane.showMessageDialog(this, "Admin login successful!");
                new AdminGui().setVisible(true);
                dispose();
            }
            case COMPANY -> {
                JOptionPane.showMessageDialog(this, "Company login successful!");
                new CompanyPage(authenticatedUserInfo.getId(), username, password).setVisible(true);
                dispose();
            }
        }
    }
    private void openBookTicketPage(){
        new searchForm().setVisible(true);
        dispose();
    }
}
