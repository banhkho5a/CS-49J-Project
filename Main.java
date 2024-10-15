import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static List<Appointment> appointments = new ArrayList<>();

    public static void main(String[] args) {
        // GUI
        JFrame frame = new JFrame("Appointment Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout(10, 10));

        // panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel startDateLabel = new JLabel("Start Date (yyyy-mm-dd):");
        JTextField startDateField = new JTextField();
        JLabel endDateLabel = new JLabel("End Date (yyyy-mm-dd):");
        JTextField endDateField = new JTextField();
        JLabel descriptionLabel = new JLabel("Description:");
        JTextField descriptionField = new JTextField();

        inputPanel.add(startDateLabel);
        inputPanel.add(startDateField);
        inputPanel.add(endDateLabel);
        inputPanel.add(endDateField);
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
                    LocalDate startDate = LocalDate.parse(startDateField.getText());
                    LocalDate endDate = LocalDate.parse(endDateField.getText());
                    String description = descriptionField.getText();

                    Appointment appointment = new Appointment(startDate, endDate, description);
                    appointments.add(appointment);
                    listModel.addElement(description + " (" + startDate + " to " + endDate + ")");
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
}