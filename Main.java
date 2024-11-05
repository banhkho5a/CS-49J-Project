import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import net.sourceforge.jdatepicker.impl.DateComponentFormatter;

public class Main {
    private static List<Appointment> appointments = new ArrayList<>();

    public static void main(String[] args) {
        // GUI setup
        JFrame frame = new JFrame("Appointment Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout(10, 10));

        // Panel setup
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel typeLabel = new JLabel("Appointment Type:");
        String[] types = {"One-time", "Daily", "Monthly"};
        JComboBox<String> typeComboBox = new JComboBox<>(types);
        JLabel startDateLabel = new JLabel("Start Date:");

        // Use JDatePicker for the start date
        JDatePickerImpl startDatePicker = createDatePicker();
        JLabel endDateLabel = new JLabel("End Date:");

        // Use JDatePicker for the end date
        JDatePickerImpl endDatePicker = createDatePicker();
        JLabel descriptionLabel = new JLabel("Description:");
        JTextField descriptionField = new JTextField();

        inputPanel.add(typeLabel);
        inputPanel.add(typeComboBox);
        inputPanel.add(startDateLabel);
        inputPanel.add(startDatePicker);
        inputPanel.add(endDateLabel);
        inputPanel.add(endDatePicker);
        inputPanel.add(descriptionLabel);
        inputPanel.add(descriptionField);

        // buttons panels
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));

        JButton addButton = new JButton("Add Appointment");
        JButton deleteButton = new JButton("Delete Appointment");
        JButton checkButton = new JButton("Check Appointment");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(checkButton);

        // list, result panels
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> appointmentList = new JList<>(listModel);
        JScrollPane listScrollPane = new JScrollPane(appointmentList);

        JLabel resultLabel = new JLabel("", SwingConstants.CENTER);

        listPanel.add(listScrollPane, BorderLayout.CENTER);
        listPanel.add(resultLabel, BorderLayout.SOUTH);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(listPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String type = ((String) typeComboBox.getSelectedItem()).toLowerCase();
                    LocalDate startDate = convertToLocalDate(startDatePicker);
                    LocalDate endDate = convertToLocalDate(endDatePicker);
                    String description = descriptionField.getText();

                    Appointment appointment;
                    switch (type) {
                        case "one-time":
                            appointment = new OnetimeAppointment(startDate, description);
                            break;
                        case "daily":
                            appointment = new DailyAppointment(startDate, endDate, description);
                            break;
                        case "monthly":
                            appointment = new MonthlyAppointment(startDate, endDate, description);
                            break;
                        default:
                            resultLabel.setText("Invalid appointment type. Use 'One-time', 'Daily', or 'Monthly'.");
                            return;
                    }

                    appointments.add(appointment);
                    listModel.addElement(type.substring(0, 1).toUpperCase() + type.substring(1) + ": " +
                            description + " (" + startDate + " to " + endDate + ")");
                    resultLabel.setText("Appointment added successfully.");
                } catch (Exception ex) {
                    resultLabel.setText("Invalid input. Please check the date format and fill all fields.");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = appointmentList.getSelectedIndex();
                if (selectedIndex != -1) {
                    appointments.remove(selectedIndex);
                    listModel.remove(selectedIndex);
                    resultLabel.setText("Appointment deleted successfully.");
                } else {
                    resultLabel.setText("Please select an appointment to delete.");
                }
            }
        });

        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    LocalDate checkDate = LocalDate.parse(JOptionPane.showInputDialog("Enter date to check (yyyy-mm-dd):"));
                    boolean found = false;
                    for (Appointment appointment : appointments) {
                        if (appointment.occursOn(checkDate)) {
                            resultLabel.setText("Appointment found: " + appointment.getDescription());
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        resultLabel.setText("No appointment occurs on this date.");
                    }
                } catch (Exception ex) {
                    resultLabel.setText("Invalid date format. Please use yyyy-mm-dd.");
                }
            }
        });

        frame.setVisible(true);
    }

    private static JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        return new JDatePickerImpl(datePanel, new DateComponentFormatter());
    }

    private static LocalDate convertToLocalDate(JDatePickerImpl datePicker) {
        java.util.Date selectedDate = (java.util.Date) datePicker.getModel().getValue();
        return selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}


// Here is how to run the program on terminal:
// javac -cp .:lib/jdatepicker-1.3.2.jar Main.java
// java -cp .:lib/jdatepicker-1.3.2.jar Main
