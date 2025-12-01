package com.group4.cesc.pattern.adapter;

import com.group4.cesc.model.GeoPoint;

/**
 * Adapter Pattern class responsible for converting raw
 * string coordinates into strongly-typed {@link GeoPoint} objects.
 */
public class CoordinateAdapter {

    /**
     * Converts latitude and longitude Strings into a GeoPoint.
     *
     * @param lat latitude string
     * @param lon longitude string
     * @return a GeoPoint representing the coordinate
     * @throws NumberFormatException if the strings are invalid numbers
     */
    public static GeoPoint convert(String lat, String lon) {
        double latitude = Double.parseDouble(lat);
        double longitude = Double.parseDouble(lon);
        return new GeoPoint(latitude, longitude);
    }
}
