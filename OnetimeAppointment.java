import java.time.LocalDate;

public class OnetimeAppointment extends Appointment {

    public OnetimeAppointment(LocalDate startDate, String description) {
        // calls a single-day appointment by using the superclass constructor and setting endDate to startDate
        super(startDate, startDate, description);
    }

    @Override
    public boolean occursOn(LocalDate date) {
        // true only if the date corresponds to start date
        return date.isEqual(getStartDate());
    }

    @Override
    public String toString() {
        return "Onetime " + super.toString();
    }
}
