package org.example.users;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Admin extends User {


    public Admin(String userName, StringBuilder password) {
        super(userName, password);

    }

    public void deleteCompany(String selectedCompany) {

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")) {
            String sql = "DELETE FROM companies WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, selectedCompany);
            statement.executeUpdate();
            statement.close();

        } catch (SQLException error) {
            error.printStackTrace();
        }
    }
    public DefaultListModel<String> fetchCompanyNames() {
        DefaultListModel<String> companyListModel = new DefaultListModel<>();

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

        return companyListModel;
    }

    public void addCompany(String userName, String password){

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")){
            PreparedStatement st = connection.prepareStatement("INSERT INTO companies(name,password) VALUES (?,?)");
            st.setString(1, userName);
            st.setString(2, password);
            st.executeUpdate();
            st.close();
        }catch (SQLException error){
            error.printStackTrace();
        }
    }

    public void setServicePrice(int servicePrice){

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")){
            PreparedStatement st = connection.prepareStatement("UPDATE admin SET serviceprice = ?");
            st.setInt(1, servicePrice);
            st.executeUpdate();
            st.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }

    }
}
