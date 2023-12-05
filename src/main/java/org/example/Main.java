package org.example;

import org.example.gui.CompanyPage;
import org.example.gui.LoginGui;
import org.example.users.Company;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGui::new);
    }
}