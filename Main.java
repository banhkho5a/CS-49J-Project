import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import net.sourceforge.jdatepicker.impl.DateComponentFormatter;

public class Main {
    private static AppointmentManager manager = new AppointmentManager();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Manage Your Appointments");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 450);
        frame.setLayout(new BorderLayout(15, 15));

        // Input section
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        JLabel typeLabel = new JLabel("Type:");
        String[] appointmentTypes = {"One-time", "Daily", "Monthly"};
        JComboBox<String> typeDropdown = new JComboBox<>(appointmentTypes);

        JLabel startLabel = new JLabel("Start Date:");
        JDatePickerImpl startPicker = createDatePicker();

        JLabel endLabel = new JLabel("End Date:");
        JDatePickerImpl endPicker = createDatePicker();

        JLabel descLabel = new JLabel("Description:");
        JTextField descField = new JTextField();

        inputPanel.add(typeLabel);
        inputPanel.add(typeDropdown);
        inputPanel.add(startLabel);
        inputPanel.add(startPicker);
        inputPanel.add(endLabel);
        inputPanel.add(endPicker);
        inputPanel.add(descLabel);
        inputPanel.add(descField);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton checkButton = new JButton("Check");
        JButton updateButton = new JButton("Update");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(checkButton);
        buttonPanel.add(updateButton);

        // Display panel
        JPanel displayPanel = new JPanel(new BorderLayout());
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> appointmentList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(appointmentList);

        JLabel statusLabel = new JLabel("", SwingConstants.CENTER);

        displayPanel.add(scrollPane, BorderLayout.CENTER);
        displayPanel.add(statusLabel, BorderLayout.SOUTH);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(displayPanel, BorderLayout.SOUTH);

        // Button actions
        addButton.addActionListener(e -> {
            try {
                String type = (String) typeDropdown.getSelectedItem();
                LocalDate start = convertToDate(startPicker);
                LocalDate end = convertToDate(endPicker);
                String description = descField.getText();

                Appointment appointment;
                if ("One-time".equals(type)) {
                    appointment = new OnetimeAppointment(start, end, description);
                } else if ("Daily".equals(type)) {
                    appointment = new DailyAppointment(start, end, description);
                } else {
                    appointment = new MonthlyAppointment(start, end, description);
                }

                manager.add(appointment);
                listModel.addElement(appointment.toString());
                statusLabel.setText("Added successfully.");
            } catch (Exception ex) {
                statusLabel.setText("Error: " + ex.getMessage());
            }
        });

        deleteButton.addActionListener(e -> {
            int selected = appointmentList.getSelectedIndex();
            if (selected >= 0) {
                Appointment toDelete = manager.getAppointments()
                        .stream()
                        .toList()
                        .get(selected);
                manager.delete(toDelete);
                listModel.remove(selected);
                statusLabel.setText("Deleted.");
            } else {
                statusLabel.setText("Select an appointment to delete.");
            }
        });

        checkButton.addActionListener(e -> {
            try {
                // Prompt the user for start and end dates
                String startInput = JOptionPane.showInputDialog("Enter start date (yyyy-mm-dd):");
                String endInput = JOptionPane.showInputDialog("Enter end date (yyyy-mm-dd):");

                // Parse the input dates
                LocalDate startDate = LocalDate.parse(startInput);
                LocalDate endDate = LocalDate.parse(endInput);

                // Validate the date range
                if (startDate.isAfter(endDate)) {
                    statusLabel.setText("Error: Start date must be earlier than or equal to end date.");
                    return;
                }

                // Find appointments that overlap with the provided range
                StringBuilder results = new StringBuilder("Appointments matching range:\n");
                boolean found = false;

                for (Appointment appointment : manager.getAppointments()) {
                    // Check if the appointment overlaps with the range
                    if (!(appointment.getEndDate().isBefore(startDate) || appointment.getStartDate().isAfter(endDate))) {
                        results.append(appointment.toString()).append("\n");
                        found = true;
                    }
                }

                // Display the results or indicate no matches found
                if (found) {
                    JOptionPane.showMessageDialog(null, results.toString(), "Results", JOptionPane.INFORMATION_MESSAGE);
                    statusLabel.setText("Matching appointments found.");
                } else {
                    statusLabel.setText("No matching appointments found.");
                }

            } catch (Exception ex) {
                statusLabel.setText("Invalid date input. Please try again.");
            }
        });


        updateButton.addActionListener(e -> {
            int selected = appointmentList.getSelectedIndex();
            if (selected >= 0) {
                Appointment current = manager.getAppointments()
                        .stream()
                        .toList()
                        .get(selected);

                String newDescription = (String) JOptionPane.showInputDialog(null, "New description:", "Update", JOptionPane.QUESTION_MESSAGE, null, null, current.getDescription());
//                String newDescription = JOptionPane.showInputDialog("New description:");
                if (newDescription == null || newDescription.isEmpty()) {
                    statusLabel.setText("Update canceled.");
                    return;
                }

                String newStartInput = (String) JOptionPane.showInputDialog(null, "New start date (yyyy-mm-dd):", "Update", JOptionPane.QUESTION_MESSAGE, null, null, current.getStartDate().toString());
                if (newStartInput == null || newStartInput.isEmpty()) {
                    statusLabel.setText("Update canceled.");
                    return;
                }
                LocalDate newStart = newStartInput == null ? current.getStartDate() : LocalDate.parse(newStartInput);

                String newEndInput = (String) JOptionPane.showInputDialog(null, "New end date (yyyy-mm-dd):", "Update", JOptionPane.QUESTION_MESSAGE, null, null, current.getEndDate().toString());
                if (newEndInput == null || newEndInput.isEmpty()) {
                    statusLabel.setText("Update canceled.");
                    return;
                }
                LocalDate newEnd = newEndInput == null ? current.getEndDate() : LocalDate.parse(newEndInput);

                String type = (String) typeDropdown.getSelectedItem();
                Appointment updated;
                if ("One-time".equals(type)) {
                    updated = new OnetimeAppointment(newStart, newEnd, newDescription);
                } else if ("Daily".equals(type)) {
                    updated = new DailyAppointment(newStart, newEnd, newDescription);
                } else {
                    updated = new MonthlyAppointment(newStart, newEnd, newDescription);
                }

                manager.update(current, updated);
                listModel.set(selected, updated.toString());
                statusLabel.setText("Updated.");
            } else {
                statusLabel.setText("Select an appointment to update.");
            }
        });

        frame.setVisible(true);
    }

    private static JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl panel = new JDatePanelImpl(model);
        return new JDatePickerImpl(panel, new DateComponentFormatter());
    }

    private static LocalDate convertToDate(JDatePickerImpl picker) {
        java.util.Date date = (java.util.Date) picker.getModel().getValue();
        if (date == null) {
            throw new IllegalArgumentException("Date not selected!");
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}

// TODO: handle invalid update input - handle crash
// TODO: sort appointments in order, or description
// TODO: display appointments on a certain date, or display all: create buttons or ....