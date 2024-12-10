import java.time.LocalDate;

public abstract class Appointment implements Comparable<Appointment> {
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

    public Appointment(LocalDate startDate, LocalDate endDate, String description) {
        LocalDate today = LocalDate.now();
        if (startDate.isBefore(today)) {
            throw new IllegalArgumentException("Start date must be today or later.");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be earlier than or equal to end date.");
        }
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    // Determines whether a date falls within the startDate and endDate range
    protected boolean inBetween(LocalDate date) {
        return (date.isEqual(startDate) || date.isEqual(endDate) ||
                (date.isAfter(startDate) && date.isBefore(endDate)));
    }

    // Abstract method for subclasses to implement occurrence logic
    public abstract boolean occursOn(LocalDate date);

    // Implementing the compareTo function for sorting
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
        boolean sameStartDate = this.startDate.equals(other.startDate);
        boolean sameEndDate = this.endDate.equals(other.endDate);
        boolean sameDescription = this.description.equals(other.description);

        return sameStartDate && sameEndDate && sameDescription;
    }

    @Override
    public String toString() {
        return description + " (" + startDate + " to " + endDate + ")";
    }

    // Getters for filtering functionality
    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getDescription() {
        return description;
    }

    // Additional method for checking if a description matches a keyword
    public boolean matchesDescription(String keyword) {
        return description.toLowerCase().contains(keyword.toLowerCase());
    }
}
