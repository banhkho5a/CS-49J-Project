import java.time.LocalDate;

public abstract class Appointment implements Comparable<Appointment> {
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

    public Appointment(LocalDate startDate, LocalDate endDate, String description) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    // helpful way to determine whether a date falls between startDate and endDate
    protected boolean inBetween(LocalDate date) {
        return (date.isEqual(startDate) || date.isEqual(endDate) ||
                (date.isAfter(startDate) && date.isBefore(endDate)));
    }

    // implementing an abstract method for subclasses
    public abstract boolean occursOn(LocalDate date);

    // implementation of the compareTo function for sorting
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
