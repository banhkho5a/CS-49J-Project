import java.time.LocalDate;

public class DailyAppointment extends Appointment {

    public DailyAppointment(LocalDate startDate, LocalDate endDate, String description) {
        super(startDate, endDate, description);
    }

    @Override
    public boolean occursOn(LocalDate date) {
        // True if the date falls between startDate and endDate
        return isDateInRange(date);
    }

    @Override
    public String toString() {
        return "Daily " + super.toString();
    }
}
