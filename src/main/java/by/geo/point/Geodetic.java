package by.geo.point;

import org.apache.commons.math3.util.FastMath;

/**
 * Интерфейс доступа к объекту, имеющего Геодезические координаты.
 */
public interface Geodetic {

    /**
     * Широта в градусах.
     *
     * @return широта в градусах
     */
    double latDeg();

    /**
     * Долгота в градусах.
     *
     * @return долгота в градусах
     */
    double lonDeg();

    /**
     * Широта в радианах.
     *
     * @return широта в радианах
     */
    default double latRad() {
        return FastMath.toRadians(latDeg());
    }

    /**
     * Долгота в радианах.
     *
     * @return долгота в радианах
     */
    default double lonRad() {
        return FastMath.toRadians(lonDeg());
    }

}
