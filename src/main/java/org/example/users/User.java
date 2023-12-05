package org.example.users;

import org.example.gui.AdminGui;
import org.example.gui.CompanyPage;

import javax.swing.*;
import java.sql.*;

public abstract class User implements ILoginable {
    @Override
    public AuthenticatedUserInfo authenticatedUser() {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")) {
            // Admin authentication
            PreparedStatement adminStatement = connection.prepareStatement("SELECT * FROM admin WHERE name = ?");
            adminStatement.setString(1, userName);
            ResultSet adminResultSet = adminStatement.executeQuery();

            if (adminResultSet.next()) {
                String retrievedUsername = adminResultSet.getString("name");
                String retrievedPassword = adminResultSet.getString("password");

                if (userName.equals(retrievedUsername) && password.toString().equals(retrievedPassword)) {
                    // Admin authentication successful
                    return new AuthenticatedUserInfo(true, AuthenticatedUserInfo.UserType.ADMIN, adminResultSet.getInt("id"));
                }
            } else {
                // Company authentication
                PreparedStatement companyStatement = connection.prepareStatement("SELECT * FROM companies WHERE name = ?");
                companyStatement.setString(1, userName);
                ResultSet companyResultSet = companyStatement.executeQuery();

                if (companyResultSet.next()) {
                    String retrievedPassword = companyResultSet.getString("password");
                    int id = companyResultSet.getInt("id");

                    if (password.toString().equals(retrievedPassword)) {
                        // Company authentication successful
                        return new AuthenticatedUserInfo(true, AuthenticatedUserInfo.UserType.COMPANY,id);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new AuthenticatedUserInfo(false, AuthenticatedUserInfo.UserType.NONE,0);
    }

    private String userName;
    private StringBuilder password;

    public User(String userName, StringBuilder password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public StringBuilder getPassword() {
        return password;
    }

    public void setPassword(StringBuilder password) {
        this.password = password;
    }


}
