import java.time.LocalDate;

public class MonthlyAppointment extends Appointment {

    public MonthlyAppointment(LocalDate startDate, LocalDate endDate, String description) {
        super(startDate, endDate, description);
    }

    @Override
    public boolean occursOn(LocalDate date) {
        // true if the date fits with the start day and is within the range
        return inBetween(date) && date.getDayOfMonth() == getStartDate().getDayOfMonth();
    }

    @Override
    public String toString() {
        return "Monthly " + super.toString();
    }
}
