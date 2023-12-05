package org.example.gui;
import org.example.data.IReservable;
import org.example.data.Transport;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class VehicleReservationGUI extends JFrame {

    private JTable vehicleInfoTable;
    private DefaultTableModel tableModel;
    private String cityName1, cityName2;
    private int passengerCount, capacity, vKey;
    private LocalDate departureDate;
    private Boolean isOneWay;
    private ArrayList<Integer> vehicleKeys = new ArrayList<>();

    public VehicleReservationGUI(String cityName1, String cityName2, int passangerCount, LocalDate departureDate, Boolean isOneWay) {
        this.cityName1 = cityName1;
        this.cityName2 = cityName2;
        this.passengerCount = passangerCount;
        this.departureDate = departureDate;
        this.isOneWay = isOneWay;

        setTitle("Vehicle Reservation");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tableModel = new DefaultTableModel();
        vehicleInfoTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(vehicleInfoTable);

        add(scrollPane);

        fetchVehicleInformation();
        setDefaultCloseOperation(VehicleReservationGUI.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void fetchVehicleInformation() {
        tableModel.addColumn("no");
        tableModel.addColumn("Type");
        tableModel.addColumn("Capacity");
        tableModel.addColumn("Company");
        tableModel.addColumn("Departure Date");
        tableModel.addColumn("Action");

        java.sql.Date sqlDate = java.sql.Date.valueOf(departureDate);
        String qry = "SELECT r.vehicle_key, r.route FROM route r "
                + "JOIN vehicles v ON r.vehicle_key = v.id "
                + "WHERE v.date = ? ";
        //COMPLETED ADD A CONDİTİON OF DEPARTURE DATE

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")) {
            PreparedStatement statement = connection.prepareStatement(qry);
            statement.setDate(1,sqlDate);
            ResultSet rSet = statement.executeQuery();

            while (rSet.next()){
                int vehicleKey = rSet.getInt("vehicle_key");
                String route = rSet.getString("route");
                if (route.contains(cityName1))
                vehicleKeys.add(vehicleKey);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query = "SELECT v.id, v.type, v.capacity, v.date, c.name AS name, r.route AS route \n" +
                "FROM vehicles v\n" +
                "JOIN route r ON v.id = r.vehicle_key\n" +
                "JOIN companies c ON c.id = v.company_key\n" +
                "WHERE r.vehicle_key = ? AND r.is_one_way = ?\n";

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setBoolean(2,isOneWay);

            for (Integer vehicleKey : vehicleKeys) {
                statement.setInt(1, vehicleKey);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String route = resultSet.getString("route");
                    if (route.contains(cityName2)){
                        vKey = resultSet.getInt("id");
                        String vehicleType = resultSet.getString("type");
                        capacity = resultSet.getInt("capacity");
                        Date departureDate = resultSet.getDate("date");
                        String companyName = resultSet.getString("name");
                        tableModel.addRow(new Object[]{vKey, vehicleType, capacity, companyName, departureDate, "Book Seat"});
                    }else {
                        JOptionPane.showMessageDialog(this,"VEHİCLE NOT FOUND, PLEASE CHEK SEARCH TİCKET INFO");
                    }
                }
                vehicleInfoTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
                vehicleInfoTable.getColumn("Action").setCellEditor(new ButtonEditor(new JTextField()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean clicked;

        public ButtonEditor(JTextField textField) {
            super(textField);
            button = new JButton();
            button.setOpaque(true);

            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                //COMPLETED İF PASSENGERCOUNT<CAPACİTY UPDATE CAPACİTY
                int vehicleKey = (int) tableModel.getValueAt(vehicleInfoTable.getSelectedRow(),0);
                IReservable reservable = new Transport(capacity);
                if (reservable.isReservable(passengerCount)) {
                    reservable.reserveSeat(passengerCount);
                    String query = "UPDATE vehicles SET capacity = ? WHERE id = ?";
                    try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ReservationSystem", "postgres", "1234")) {
                        PreparedStatement statement = connection.prepareStatement(query);
                            statement.setInt(1, reservable.resevableSeatCount());
                            statement.setInt(2, vehicleKey);
                            statement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                //COMPLETED GET İNFO tableModel.getValueAt(vehicleInfoTable.getSelectedRow() THEN UPDATE DB
                String message = String.format("Seat booked for %s on %s", tableModel.getValueAt(vehicleInfoTable.getSelectedRow(), 1), tableModel.getValueAt(vehicleInfoTable.getSelectedRow(), 4));
                JOptionPane.showMessageDialog(VehicleReservationGUI.this, message, "Seat Booked", JOptionPane.INFORMATION_MESSAGE);
            }
            clicked = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }
}
