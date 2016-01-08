package by.geo.model;

import org.apache.commons.math3.util.FastMath;

/**
 * Геодезические координаты.
 */
public interface Geodetic {

    /**
     * Широта в градусах.
     */
    double latDeg();

    /**
     * Долгота в градусах.
     */
    double lonDeg();

    /**
     * Широта в радианах.
     */
    default double latRad() {
        return FastMath.toRadians(latDeg());
    }

    /**
     * Долгота в радианах.
     */
    default double lonRad() {
        return FastMath.toRadians(lonDeg());
    }

}
