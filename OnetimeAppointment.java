import java.time.LocalDate;

public class OnetimeAppointment extends Appointment {

    public OnetimeAppointment(LocalDate startDate, String description) {
        // Call the superclass constructor and set endDate to startDate (single-day appointment)
        super(startDate, startDate, description);
    }

    @Override
    public boolean occursOn(LocalDate date) {
        // Only true if the date matches the start date
        return date.isEqual(getStartDate());
    }

    @Override
    public String toString() {
        return "Onetime " + super.toString();
    }
}
