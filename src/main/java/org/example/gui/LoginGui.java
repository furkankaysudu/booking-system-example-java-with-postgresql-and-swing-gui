package org.example.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginGui extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginGui() {
        setTitle("Login Page");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize components
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");

        // Set layout manager
        setLayout(new GridLayout(3, 2));

        // Add components to the frame
        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(new JLabel(""));
        add(loginButton);

        // Add action listener to the login button
        loginButton.addActionListener(e -> authenticate());

        setVisible(true);
    }
    private void authenticate() {
        String username = usernameField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")) {
            // Admin authentication
            PreparedStatement adminStatement = connection.prepareStatement("SELECT * FROM admin WHERE name = ?");
            adminStatement.setString(1, username);
            ResultSet adminResultSet = adminStatement.executeQuery();

            if (adminResultSet.next()) {
                String retrievedUsername = adminResultSet.getString("name");
                String retrievedPassword = adminResultSet.getString("password");

                if (username.equals(retrievedUsername) && password.equals(retrievedPassword)) {
                    // Admin authentication successful
                    JOptionPane.showMessageDialog(this, "Admin login successful!");
                    new AdminGui().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.");
                    System.out.println(retrievedPassword);
                }
            } else {
                // Company authentication
                PreparedStatement companyStatement = connection.prepareStatement("SELECT * FROM companies WHERE name = ?");
                companyStatement.setString(1, username);
                ResultSet companyResultSet = companyStatement.executeQuery();

                if (companyResultSet.next()) {
                    String retrievedPassword = companyResultSet.getString("password");
                    int id = companyResultSet.getInt("id");

                    if (password.equals(retrievedPassword)) {
                        // Company authentication successful
                        JOptionPane.showMessageDialog(this, "Company login successful!");
                        new CompanyPage(id).setVisible(true);
                        dispose();
                        // Proceed with company-related actions or open another GUI
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid username or password.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGui::new);
    }
}
