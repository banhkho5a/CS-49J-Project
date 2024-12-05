import java.time.LocalDate;

public class OnetimeAppointment extends Appointment {

    public OnetimeAppointment(LocalDate startDate, LocalDate endDate, String description) {
        // calls the superclass constructor to use both startDate and endDate
        super(startDate, endDate, description);
    }

    @Override
    public boolean occursOn(LocalDate date) {
        // check if the date falls between startDate and endDate
        return inBetween(date);
    }

    @Override
    public String toString() {
        return "Onetime " + super.toString();
    }
}
