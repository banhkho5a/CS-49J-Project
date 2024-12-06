import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;

public class AppointmentTest {

    @Test
    public void testOnetimeAppointmentConstructor() {
        OnetimeAppointment appointment = new OnetimeAppointment(LocalDate.of(2024, 11, 1), "customer appointment");
        assertEquals(appointment.getStartDate(), appointment.getEndDate());
    }

    @Test
    public void testOnetimeAppointmentOccursOn() {
        OnetimeAppointment appointment = new OnetimeAppointment(LocalDate.of(2024, 11, 1), "customer appointment");
        assertTrue(appointment.occursOn(LocalDate.of(2024, 11, 1)));
        assertFalse(appointment.occursOn(LocalDate.of(2024, 11, 2)));
    }

    @Test
    public void testDailyAppointmentOccursOn() {
        DailyAppointment appointment = new DailyAppointment(LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 5), "daily meetup");
        assertTrue(appointment.occursOn(LocalDate.of(2024, 11, 3)));
        assertFalse(appointment.occursOn(LocalDate.of(2024, 11, 6)));
    }

    @Test
    public void testMonthlyAppointmentOccursOn() {
        MonthlyAppointment appointment = new MonthlyAppointment(LocalDate.of(2024, 11, 1), LocalDate.of(2025, 1, 1), "monthly meetup");
        assertTrue(appointment.occursOn(LocalDate.of(2024, 12, 1))); // Same day of month
        assertFalse(appointment.occursOn(LocalDate.of(2024, 12, 2))); // Different day of month
    }

    @Test
    public void testEqualsMethod() {
        OnetimeAppointment appointment1 = new OnetimeAppointment(LocalDate.of(2024, 11, 1), "doctor schedule");
        OnetimeAppointment appointment2 = new OnetimeAppointment(LocalDate.of(2024, 11, 1), "doctor schedule");
        assertTrue(appointment1.equals(appointment2));
    }

    @Test
    public void testCompareAppointments() {
        OnetimeAppointment appointment1 = new OnetimeAppointment(LocalDate.of(2024, 11, 1), "doctor schedule");
        DailyAppointment appointment2 = new DailyAppointment(LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 5), "daily meetup");

        Appointment[] appointments = {appointment2, appointment1};
        Arrays.sort(appointments);

        assertEquals(appointment1, appointments[0]); // ought to be arranged by the start date
    }

    // test AppointmentManager methods
    @Test
    public void testAddAppointment() {
        AppointmentManager manager = new AppointmentManager();
        OnetimeAppointment appointment = new OnetimeAppointment(LocalDate.of(2024, 12, 25), "customer appointment");
        manager.add(appointment);

        assertTrue(manager.getSortedAppointments().contains(appointment));
    }

    @Test
    public void testAddDuplicateAppointment() {
        AppointmentManager manager = new AppointmentManager();
        OnetimeAppointment appointment = new OnetimeAppointment(LocalDate.of(2024, 12, 25), "customer appointment");
        manager.add(appointment);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.add(appointment); // Adding duplicate
        });
        assertEquals("Appointment exists!", exception.getMessage()); // Fixed message
    }

    @Test
    public void testDeleteAppointment() {
        AppointmentManager manager = new AppointmentManager();
        OnetimeAppointment appointment = new OnetimeAppointment(LocalDate.of(2024, 12, 25), "customer appointment");
        manager.add(appointment);
        manager.delete(appointment);

        assertFalse(manager.getSortedAppointments().contains(appointment));
    }

    @Test
    public void testUpdateAppointment() {
        AppointmentManager manager = new AppointmentManager();
        OnetimeAppointment oldAppointment = new OnetimeAppointment(LocalDate.of(2024, 12, 25), "customer appointment");
        OnetimeAppointment newAppointment = new OnetimeAppointment(LocalDate.of(2024, 12, 31), "updated customer appointment");
        manager.add(oldAppointment);
        manager.update(oldAppointment, newAppointment);

        assertFalse(manager.getSortedAppointments().contains(oldAppointment));
        assertTrue(manager.getSortedAppointments().contains(newAppointment));
    }

    @Test
    public void testGetAppointmentsOn() {
        AppointmentManager manager = new AppointmentManager();
        DailyAppointment appointment1 = new DailyAppointment(LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 5), "daily meetup");
        OnetimeAppointment appointment2 = new OnetimeAppointment(LocalDate.of(2024, 11, 3), "customer appointment");
        manager.add(appointment1);
        manager.add(appointment2);

        Appointment[] results = manager.getAppointmentsOn(LocalDate.of(2024, 11, 3), null);
        assertEquals(2, results.length);
    }

    @Test
    public void testGetAppointmentsSorted() {
        AppointmentManager manager = new AppointmentManager();
        OnetimeAppointment appointment1 = new OnetimeAppointment(LocalDate.of(2024, 12, 25), "customer appointment");
        OnetimeAppointment appointment2 = new OnetimeAppointment(LocalDate.of(2024, 12, 31), "updated customer appointment");

        manager.add(appointment1);
        manager.add(appointment2);

        Appointment[] results = manager.getAppointmentsOn(null, Comparator.comparing(Appointment::getDescription));
        assertEquals("customer appointment", results[0].getDescription());
        assertEquals("updated customer appointment", results[1].getDescription());
    }
}
