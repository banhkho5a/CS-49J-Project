import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import net.sourceforge.jdatepicker.impl.DateComponentFormatter;

public class Main {
    private static AppointmentManager manager = new AppointmentManager();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Manage Your Appointments");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
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
        JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
       //JButton checkButton = new JButton("Check");
        JButton updateButton = new JButton("Update");
        JButton filterByDateButton = new JButton("Filter by Date");
        JButton filterByDescriptionButton = new JButton("Filter by Description");
        JButton showAllButton = new JButton("Show All");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        //buttonPanel.add(checkButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(filterByDateButton);
        buttonPanel.add(filterByDescriptionButton);
        buttonPanel.add(showAllButton);

        // Display
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
                    appointment = new OnetimeAppointment(start, description);
                } else if ("Daily".equals(type)) {
                    appointment = new DailyAppointment(start, end, description);
                } else {
                    appointment = new MonthlyAppointment(start, end, description);
                }

                manager.add(appointment);
                updateAppointmentList(listModel);
                statusLabel.setText("Added successfully.");
            } catch (Exception ex) {
                statusLabel.setText("Error: " + ex.getMessage());
            }
        });

        deleteButton.addActionListener(e -> {
            int selected = appointmentList.getSelectedIndex();
            if (selected >= 0) {
                Appointment toDelete = manager.getSortedAppointments().get(selected);
                manager.delete(toDelete);
                updateAppointmentList(listModel);
                statusLabel.setText("Deleted.");
            } else {
                statusLabel.setText("Select an appointment to delete.");
            }
        });

        /*checkButton.addActionListener(e -> {
            try {
                String input = JOptionPane.showInputDialog("Enter date (yyyy-mm-dd):");
                LocalDate date = LocalDate.parse(input);

                Appointment[] results = manager.getAppointmentsOn(date, null);
                if (results.length > 0) {
                    statusLabel.setText("Found: " + results[0].getDescription());
                } else {
                    statusLabel.setText("No appointments found.");
                }
            } catch (Exception ex) {
                statusLabel.setText("Invalid date.");
            }
        });
*/
        updateButton.addActionListener(e -> {
            int selected = appointmentList.getSelectedIndex();
            if (selected >= 0) {
                Appointment current = manager.getSortedAppointments().get(selected);

                String newDescription = JOptionPane.showInputDialog("New description:", current.getDescription());
                String newStartInput = JOptionPane.showInputDialog("New start date (yyyy-mm-dd):", current.getStartDate().toString());
                String newEndInput = JOptionPane.showInputDialog("New end date (yyyy-mm-dd):", current.getEndDate().toString());

                LocalDate newStart = newStartInput == null ? current.getStartDate() : LocalDate.parse(newStartInput);
                LocalDate newEnd = newEndInput == null ? current.getEndDate() : LocalDate.parse(newEndInput);

                String type = (String) typeDropdown.getSelectedItem();
                Appointment updated;
                if ("One-time".equals(type)) {
                    updated = new OnetimeAppointment(newStart, newDescription);
                } else if ("Daily".equals(type)) {
                    updated = new DailyAppointment(newStart, newEnd, newDescription);
                } else {
                    updated = new MonthlyAppointment(newStart, newEnd, newDescription);
                }

                manager.update(current, updated);
                updateAppointmentList(listModel);
                statusLabel.setText("Updated.");
            } else {
                statusLabel.setText("Select an appointment to update.");
            }
        });

        filterByDateButton.addActionListener(e -> {
            try {
                String inputStart = JOptionPane.showInputDialog("Enter start date (yyyy-mm-dd):");
                String inputEnd = JOptionPane.showInputDialog("Enter end date (yyyy-mm-dd):");
                LocalDate startDate = LocalDate.parse(inputStart);
                LocalDate endDate = LocalDate.parse(inputEnd);

                List<Appointment> filtered = manager.getAppointmentsByDateRange(startDate, endDate);
                listModel.clear();
                for (Appointment appointment : filtered) {
                    listModel.addElement(appointment.toString());
                }
                statusLabel.setText("Filtered by date range.");
            } catch (Exception ex) {
                statusLabel.setText("Invalid dates.");
            }
        });

        filterByDescriptionButton.addActionListener(e -> {
            String keyword = JOptionPane.showInputDialog("Enter description keyword:");
            if (keyword != null && !keyword.isEmpty()) {
                List<Appointment> filtered = manager.getAppointmentsByDescription(keyword);
                listModel.clear();
                for (Appointment appointment : filtered) {
                    listModel.addElement(appointment.toString());
                }
                statusLabel.setText("Filtered by description.");
            } else {
                statusLabel.setText("Enter a valid keyword.");
            }
        });

        showAllButton.addActionListener(e -> {
            updateAppointmentList(listModel);
            statusLabel.setText("Displaying all appointments.");
        });

        frame.setVisible(true);
    }

    private static void updateAppointmentList(DefaultListModel<String> listModel) {
        listModel.clear();
        List<Appointment> sortedAppointments = manager.getSortedAppointments();
        for (Appointment appointment : sortedAppointments) {
            listModel.addElement(appointment.toString());
        }
    }

    private static JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl panel = new JDatePanelImpl(model);
        return new JDatePickerImpl(panel, new DateComponentFormatter());
    }

    private static LocalDate convertToDate(JDatePickerImpl picker) {
        java.util.Date date = (java.util.Date) picker.getModel().getValue();
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
