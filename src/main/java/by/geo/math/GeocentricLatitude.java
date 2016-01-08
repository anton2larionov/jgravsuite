package by.geo.math;

import by.geo.egm.Ellipsoid;
import by.geo.model.Geodetic;
import org.apache.commons.math3.util.FastMath;

public class GeocentricLatitude {

    private final Ellipsoid ell;

    public GeocentricLatitude(final Ellipsoid ell) {
        this.ell = ell;
    }

    /**
     * Вычисление сферической широты.
     *
     * @return сферическая широта
     */
    public double calculate(final Geodetic p) {
        return FastMath.atan(FastMath.pow((ell.getB() / ell.getA()), 2)
                * FastMath.tan(p.latRad()));
    }

}
