package ui;

import model.TrafficSignal;
import service.TrafficManager;
import exception.InvalidSignalException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TrafficManagementGUI extends JFrame {

    private JTextField txtId;
    private JTextField txtLocation;
    private JComboBox<String> cmbStatus;

    private JTable table;
    private DefaultTableModel model;

    private TrafficManager manager;

    // 🚦 Traffic Light Panels
    private JPanel redLight;
    private JPanel yellowLight;
    private JPanel greenLight;

    public TrafficManagementGUI() {

        manager = new TrafficManager();

        setTitle("🚦 Smart Traffic Management System");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createGUI();

        setVisible(true);
    }

    private void createGUI() {

        Font font = new Font("Arial", Font.PLAIN, 14);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // ================= FORM PANEL =================
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        JLabel lblId = new JLabel("Signal ID");
        JLabel lblLocation = new JLabel("Location");
        JLabel lblStatus = new JLabel("Status");

        txtId = new JTextField();
        txtLocation = new JTextField();

        cmbStatus = new JComboBox<>();
        cmbStatus.addItem("Green");
        cmbStatus.addItem("Yellow");
        cmbStatus.addItem("Red");

        JButton btnAdd = new JButton("Add Signal");
        JButton btnRemove = new JButton("Remove Signal");
        JButton btnUpdate = new JButton("Update Status");

        formPanel.add(lblId);
        formPanel.add(txtId);

        formPanel.add(lblLocation);
        formPanel.add(txtLocation);

        formPanel.add(lblStatus);
        formPanel.add(cmbStatus);

        formPanel.add(btnAdd);
        formPanel.add(btnRemove);
        formPanel.add(btnUpdate);

        // ================= TABLE =================
        String[] columns = {"Signal ID", "Location", "Status"};

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        // ================= TRAFFIC LIGHT PANEL =================
        JPanel lightPanel = new JPanel();
        lightPanel.setLayout(new GridLayout(3, 1, 5, 5));
        lightPanel.setPreferredSize(new Dimension(100, 200));

        redLight = createLight(Color.DARK_GRAY);
        yellowLight = createLight(Color.DARK_GRAY);
        greenLight = createLight(Color.DARK_GRAY);

        lightPanel.add(redLight);
        lightPanel.add(yellowLight);
        lightPanel.add(greenLight);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(lightPanel, BorderLayout.EAST);

        // ================= EMERGENCY BUTTON =================
        JButton btnEmergency = new JButton("🚨 Emergency Mode");

        // Add everything
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(btnEmergency, BorderLayout.SOUTH);

        add(mainPanel);

        // ================= ACTIONS =================
        btnAdd.addActionListener(e -> addSignal());
        btnRemove.addActionListener(e -> removeSignal());
        btnUpdate.addActionListener(e -> updateStatus());
        btnEmergency.addActionListener(e -> emergencyMode());

        // table selection → light update
        table.getSelectionModel().addListSelectionListener(e -> updateLightFromTable());
    }

    // 🚦 Create circular light
    private JPanel createLight(Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setPreferredSize(new Dimension(50, 50));
        return panel;
    }

    // 🚦 Update traffic light UI
    private void setLight(String status) {

        redLight.setBackground(Color.DARK_GRAY);
        yellowLight.setBackground(Color.DARK_GRAY);
        greenLight.setBackground(Color.DARK_GRAY);

        switch (status.toLowerCase()) {
            case "red":
                redLight.setBackground(Color.RED);
                break;
            case "yellow":
                yellowLight.setBackground(Color.YELLOW);
                break;
            case "green":
                greenLight.setBackground(Color.GREEN);
                break;
        }
    }

    private void updateLightFromTable() {
        int row = table.getSelectedRow();
        if (row != -1) {
            String status = model.getValueAt(row, 2).toString();
            setLight(status);
        }
    }

    // ================= ORIGINAL LOGIC (UNCHANGED) =================

    private void addSignal() {

        try {

            String idText = txtId.getText().trim();
            String location = txtLocation.getText().trim();

            if (idText.isEmpty() || location.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            int id = Integer.parseInt(idText);

            if (id <= 0) throw new NumberFormatException();

            if (location.length() < 3) {
                throw new InvalidSignalException("Location must contain at least 3 characters.");
            }

            String status = cmbStatus.getSelectedItem().toString();

            TrafficSignal signal = new TrafficSignal(id, location, status);
            manager.addSignal(signal);

            model.addRow(new Object[]{id, location, status});

            setLight(status);

            txtId.setText("");
            txtLocation.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Signal ID must be a positive number.");
        } catch (InvalidSignalException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void removeSignal() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a signal first.");
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);
        manager.removeSignal(id);
        model.removeRow(selectedRow);
    }

    private void updateStatus() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a signal.");
            return;
        }

        String newStatus = cmbStatus.getSelectedItem().toString();

        model.setValueAt(newStatus, selectedRow, 2);

        setLight(newStatus);

        JOptionPane.showMessageDialog(this, "Signal status updated successfully.");
    }

    private void emergencyMode() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a signal first.");
            return;
        }

        model.setValueAt("Green", selectedRow, 2);

        setLight("Green");

        JOptionPane.showMessageDialog(this,
                "Emergency Mode Activated. Signal changed to Green.");
    }
}