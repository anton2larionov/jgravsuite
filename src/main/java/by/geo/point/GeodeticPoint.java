package by.geo.point;

/**
 * Реализация объекта, имеющего Геодезические координаты.
 */
public class GeodeticPoint implements Geodetic {

    private final double latDeg, lonDeg;

    /**
     * Точка (геодезический пункт).
     *
     * @param latDeg широта в градусах
     * @param lonDeg долгота в градусах
     */
    GeodeticPoint(final double latDeg, final double lonDeg) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeodeticPoint that = (GeodeticPoint) o;

        return Double.compare(that.latDeg, latDeg) == 0 && Double.compare(that.lonDeg, lonDeg) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latDeg);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lonDeg);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
