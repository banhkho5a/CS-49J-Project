import java.time.LocalDate;

public class MonthlyAppointment extends Appointment {

    public MonthlyAppointment(LocalDate startDate, LocalDate endDate, String description) {
        super(startDate, endDate, description);
    }

    @Override
    public boolean occursOn(LocalDate date) {
        // True if the date falls within the range and matches the day of the startDate
        return isDateInRange(date) && date.getDayOfMonth() == getStartDate().getDayOfMonth();
    }

    @Override
    public String toString() {
        return "Monthly " + super.toString();
    }
}
