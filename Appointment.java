import java.time.LocalDate;

public abstract class Appointment {
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

    public Appointment(LocalDate startDate, LocalDate endDate, String description) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    // Simple method to check if a date is between startDate and endDate
    protected boolean isDateInRange(LocalDate date) {
        return (date.isEqual(startDate) || date.isEqual(endDate) || (date.isAfter(startDate) && date.isBefore(endDate)));
    }

    // Abstract method to be implemented by subclasses
    public abstract boolean occursOn(LocalDate date);

    @Override
    public String toString() {
        return "Appointment: " + description + " (From " + startDate + " to " + endDate + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Appointment other = (Appointment) obj;
        return startDate.equals(other.startDate) &&
                endDate.equals(other.endDate) &&
                description.equals(other.description);
    }

    // Getter methods
    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getDescription() {
        return description;
    }
}
