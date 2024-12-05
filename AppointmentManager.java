import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

public class AppointmentManager {

    private HashSet<Appointment> appointments;

    // Constructor
    public AppointmentManager() {
        this.appointments = new HashSet<>();
    }

    // Getter for appointments
    public HashSet<Appointment> getAppointments() {
        return appointments;
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

    // Get appointments on a specific date with optional comparator
    public Appointment[] getAppointmentsOn(LocalDate date, Comparator<Appointment> comparator) {
        PriorityQueue<Appointment> tempQueue;

        if (comparator != null) {
            tempQueue = new PriorityQueue<>(comparator);
        } else {
            tempQueue = new PriorityQueue<>();
        }

        for (Appointment a : appointments) {
            if (date == null || a.occursOn(date)) {
                tempQueue.add(a);
            }
        }

        return tempQueue.toArray(new Appointment[0]);
    }
}
