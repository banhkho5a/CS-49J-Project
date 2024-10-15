import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;

public class AppointmentTest {

    @Test
    public void testOccursOnBeforeStartDate() {
        Appointment appointment = new Appointment(LocalDate.of(2024, 10, 10), LocalDate.of(2024, 10, 15), "Test Appointment");
        assertFalse(appointment.occursOn(LocalDate.of(2024, 10, 9)));
    }

    @Test
    public void testOccursOnStartDate() {
        Appointment appointment = new Appointment(LocalDate.of(2024, 10, 10), LocalDate.of(2024, 10, 15), "Test Appointment");
        assertTrue(appointment.occursOn(LocalDate.of(2024, 10, 10)));
    }

    @Test
    public void testOccursOnBetweenDates() {
        Appointment appointment = new Appointment(LocalDate.of(2024, 10, 10), LocalDate.of(2024, 10, 15), "Test Appointment");
        assertTrue(appointment.occursOn(LocalDate.of(2024, 10, 12)));
    }

    @Test
    public void testOccursOnEndDate() {
        Appointment appointment = new Appointment(LocalDate.of(2024, 10, 10), LocalDate.of(2024, 10, 15), "Test Appointment");
        assertTrue(appointment.occursOn(LocalDate.of(2024, 10, 15)));
    }

    @Test
    public void testOccursOnAfterEndDate() {
        Appointment appointment = new Appointment(LocalDate.of(2024, 10, 10), LocalDate.of(2024, 10, 15), "Test Appointment");
        assertFalse(appointment.occursOn(LocalDate.of(2024, 10, 16)));
    }
}



