import java.util.*;
import java.time.LocalDate;

public class AppointmentManager {

    private HashSet<Appointment> appointments;

    // Constructor
    public AppointmentManager() {
        this.appointments = new HashSet<>();
    }

    // Add an appointment
    public void add(Appointment appointment) {
        if (appointments.contains(appointment)) {
            throw new IllegalArgumentException("Appointment exists!");
        }
        appointments.add(appointment);
    }

    // Delete an appointment
    public void delete(Appointment appointment) {
        if (!appointments.contains(appointment)) {
            throw new IllegalArgumentException("Appointment not found!");
        }
        appointments.remove(appointment);
    }

    // Update an appointment
    public void update(Appointment current, Appointment modified) {
        delete(current);  // Remove the current appointment
        add(modified);    // Add the modified appointment
    }

    // Get sorted appointments
    public List<Appointment> getSortedAppointments() {
        List<Appointment> sortedAppointments = new ArrayList<>(appointments);
        sortedAppointments.sort(null);  // Uses the compareTo method for sorting
        return sortedAppointments;
    }

    // Get appointments on a specific date with optional comparator
    public Appointment[] getAppointmentsOn(LocalDate date, Comparator<Appointment> comparator) {
        List<Appointment> filteredAppointments = new ArrayList<>();

        for (Appointment a : appointments) {
            if (date == null || a.occursOn(date)) {
                filteredAppointments.add(a);
            }
        }

        if (comparator != null) {
            filteredAppointments.sort(comparator);
        } else {
            filteredAppointments.sort(null);  // Use default sorting
        }

        return filteredAppointments.toArray(new Appointment[0]);
    }
}
