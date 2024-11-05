import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.util.Arrays;

public class AppointmentTest {

    @Test
    public void testOnetimeAppointmentConstructor() {
        OnetimeAppointment appointment = new OnetimeAppointment(LocalDate.of(2024, 11, 1), "Dentist Visit");
        assertEquals(appointment.getStartDate(), appointment.getEndDate());
    }

    @Test
    public void testOnetimeAppointmentOccursOn() {
        OnetimeAppointment appointment = new OnetimeAppointment(LocalDate.of(2024, 11, 1), "Dentist Visit");
        assertTrue(appointment.occursOn(LocalDate.of(2024, 11, 1)));
        assertFalse(appointment.occursOn(LocalDate.of(2024, 11, 2)));
    }

    @Test
    public void testDailyAppointmentOccursOn() {
        DailyAppointment appointment = new DailyAppointment(LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 5), "Daily Meeting");
        assertTrue(appointment.occursOn(LocalDate.of(2024, 11, 3)));
        assertFalse(appointment.occursOn(LocalDate.of(2024, 11, 6)));
    }

    @Test
    public void testMonthlyAppointmentOccursOn() {
        MonthlyAppointment appointment = new MonthlyAppointment(LocalDate.of(2024, 11, 1), LocalDate.of(2025, 1, 1), "Monthly Review");
        assertTrue(appointment.occursOn(LocalDate.of(2024, 12, 1))); // Same day of month
        assertFalse(appointment.occursOn(LocalDate.of(2024, 12, 2))); // Different day of month
    }

    @Test
    public void testEqualsMethod() {
        OnetimeAppointment appointment1 = new OnetimeAppointment(LocalDate.of(2024, 11, 1), "Doctor Visit");
        OnetimeAppointment appointment2 = new OnetimeAppointment(LocalDate.of(2024, 11, 1), "Doctor Visit");
        assertTrue(appointment1.equals(appointment2));
    }

    @Test
    public void testCompareAppointments() {
        OnetimeAppointment appointment1 = new OnetimeAppointment(LocalDate.of(2024, 11, 1), "Doctor Visit");
        DailyAppointment appointment2 = new DailyAppointment(LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 5), "Daily Checkup");

        Appointment[] appointments = {appointment2, appointment1};
        Arrays.sort(appointments);

        assertEquals(appointment1, appointments[0]); // Should be sorted by start date
    }
}
