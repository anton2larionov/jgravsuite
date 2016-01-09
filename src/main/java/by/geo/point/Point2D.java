package by.geo.point;

/**
 * Простейшая реализация объекта, имеющего Геодезические координаты.
 */
public class Point2D implements Geodetic {

    private final double latDeg, lonDeg;

    /**
     * Точка (геодезический пункт).
     *
     * @param latDeg широта в градусах
     * @param lonDeg долгота в градусах
     */
    public Point2D(double latDeg, double lonDeg) {
        this.latDeg = latDeg;
        this.lonDeg = lonDeg;
    }

    @Override
    public double latDeg() {
        return latDeg;
    }

    @Override
    public double lonDeg() {
        return lonDeg;
    }
}
