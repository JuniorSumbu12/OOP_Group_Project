package com.group4.cesc.model;

/**
 * Represents a geographical coordinate (latitude and longitude).
 * <p>
 * This model is used by scooters to store GPS location
 * and by the Adapter Pattern to convert raw input into valid objects.
 */
public class GeoPoint {

    private double latitude;
    private double longitude;

    /**
     * Default constructor for frameworks and libraries that require it.
     */
    public GeoPoint() {}

    /**
     * Creates a GeoPoint with the given coordinates.
     *
     * @param latitude  latitude value
     * @param longitude longitude value
     */
    public GeoPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * @return the latitude value
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude value.
     *
     * @param latitude latitude to set
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude value
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude value.
     *
     * @param longitude longitude to set
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "GeoPoint{lat=" + latitude + ", lon=" + longitude + "}";
    }
}
