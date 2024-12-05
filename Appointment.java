import java.time.LocalDate;

public abstract class Appointment implements Comparable<Appointment> {
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

    public Appointment(LocalDate startDate, LocalDate endDate, String description) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be earlier than or equal to end date.");
        }
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    protected boolean inBetween(LocalDate date) {
        return (date.isEqual(startDate) || date.isEqual(endDate) ||
                (date.isAfter(startDate) && date.isBefore(endDate)));
    }

    public abstract boolean occursOn(LocalDate date);

    @Override
    public int compareTo(Appointment other) {
        int result = this.startDate.compareTo(other.startDate);
        if (result == 0) {
            result = this.endDate.compareTo(other.endDate);
            if (result == 0) {
                result = this.description.compareTo(other.description);
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Appointment)) {
            return false;
        }
        Appointment other = (Appointment) obj;
        return this.startDate.equals(other.startDate) &&
                this.endDate.equals(other.endDate) &&
                this.description.equals(other.description);
    }

    @Override
    public String toString() {
        return description + " (" + startDate + " to " + endDate + ")";
    }

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
