package org.example.gui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class searchForm extends JFrame {
    private JLabel departureLabel, arrivalLabel, passengerLabel, dateLabel;
    private JTextField departureField, arrivalField, passengerField, dateField;
    private JButton reserveButton;

    private final String[] cities = {"Istanbul", "Kocaeli", "Bilecik", "Ankara", "Eskişehir", "Konya"};

    public searchForm() {
        setTitle("Ticket Reservation App");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        departureLabel = new JLabel("Departure City:");
        departureField = new JTextField();

        arrivalLabel = new JLabel("Arrival City:");
        arrivalField = new JTextField();

        passengerLabel = new JLabel("Number of Passengers:");
        passengerField = new JTextField();

        dateLabel = new JLabel("Departure Date (YYYY-MM-DD):");
        dateField = new JTextField();

        reserveButton = new JButton("Reserve Ticket");
        reserveButton.addActionListener(e -> reserveTicket());

        add(departureLabel);
        add(departureField);
        add(arrivalLabel);
        add(arrivalField);
        add(passengerLabel);
        add(passengerField);
        add(dateLabel);
        add(dateField);
        add(new JLabel()); // Empty label as a placeholder
        add(reserveButton);

        setVisible(true);
    }

    private void reserveTicket() {
        String departure = departureField.getText();
        String arrival = arrivalField.getText();
        String passenger = passengerField.getText();
        String dateStr = dateField.getText();

        if (!isValidCity(departure) || !isValidCity(arrival)) {
            JOptionPane.showMessageDialog(this, "Invalid city name!");
            return;
        }

        int numOfPassengers;
        try {
            numOfPassengers = Integer.parseInt(passenger);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number of passengers!");
            return;
        }

        LocalDate departureDate;
        try {
            departureDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate minDate = LocalDate.of(2023, 12, 4);
            LocalDate maxDate = LocalDate.of(2023, 12, 10);
            if (departureDate.isBefore(minDate) || departureDate.isAfter(maxDate)) {
                JOptionPane.showMessageDialog(this, "Invalid departure date!");
                return;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format!");
            return;
        }
        SwingUtilities.invokeLater(() -> new VehicleReservationGUI(departure,arrival,numOfPassengers,departureDate));

        // Perform ticket reservation or further actions based on inputs
        // You can handle the reservation logic here
        // For now, display a message with the input details
        String message = "Departure City: " + departure + "\nArrival City: " + arrival + "\nPassengers: "
                + numOfPassengers + "\nDeparture Date: " + departureDate;
        JOptionPane.showMessageDialog(this, "SEARCH RESULTS ACCORDİNG TO: \n" + message);
    }

    private boolean isValidCity(String cityName) {
        for (String city : cities) {
            if (city.equalsIgnoreCase(cityName)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(searchForm::new);
    }
}
