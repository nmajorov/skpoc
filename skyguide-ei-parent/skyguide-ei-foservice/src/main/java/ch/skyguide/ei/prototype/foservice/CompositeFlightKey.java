package ch.skyguide.ei.prototype.foservice;

public class CompositeFlightKey {

    private final String aircraftIdentification;
    private final String departureAerodrome;
    private final String arrivalAerodrome;

    public CompositeFlightKey(final String aircraftIdentification, final String departureAerodrome, final String arrivalAerodrome) {
        this.aircraftIdentification = notEmpty(aircraftIdentification, "aircraftIdentification");
        this.departureAerodrome = notEmpty(departureAerodrome, "departureAerodrome");
        this.arrivalAerodrome = notEmpty(arrivalAerodrome, "arrivalAerodrome");
    }

    private static String notEmpty(final String value, final String name) {
        if (value == null || value.trim().length() == 0) {
            throw new IllegalArgumentException(name + " must be specified and not empty, is: " + value);
        }

        return value;
    }

    public String getAircraftIdentification() {
        return aircraftIdentification;
    }

    public String getDepartureAerodrome() {
        return departureAerodrome;
    }

    public String getArrivalAerodrome() {
        return arrivalAerodrome;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof CompositeFlightKey)) {
            return false;
        }

        final CompositeFlightKey other = CompositeFlightKey.class.cast(obj);
        return aircraftIdentification.equals(other.aircraftIdentification) &&
               departureAerodrome.equals(other.departureAerodrome) &&
               arrivalAerodrome.equals(other.arrivalAerodrome);
    }

    @Override
    public int hashCode() {
        final int prime = 17;
        int hashCode = 1;
        hashCode = hashCode * prime + aircraftIdentification.hashCode();
        hashCode = hashCode * prime + departureAerodrome.hashCode();
        hashCode = hashCode * prime + arrivalAerodrome.hashCode();

        return hashCode;
    }

    @Override
    public String toString() {
        return "[aircraftIdentification " + aircraftIdentification + ", departureAerodrome " + departureAerodrome + ", arrivalAerodrome " + arrivalAerodrome + "]";
    }

}
