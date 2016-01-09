package by.geo.math;

import by.geo.point.Geodetic;
import by.geo.ref.Ellipsoid;
import org.apache.commons.math3.util.FastMath;

import java.util.Objects;

/**
 * Функция вычисления сферической широты.
 */
public final class GeocentricLatitude implements GeodeticToDoubleFunction {

    private final Ellipsoid ell;

    public GeocentricLatitude(final Ellipsoid ell) {
        this.ell = Objects.requireNonNull(ell);
    }

    /**
     * Вычисление сферической широты в точке {@code pt}.
     *
     * @return сферическая широта
     */
    @Override
    public double applyAsDouble(final Geodetic pt) {
        Objects.nonNull(pt);
        return FastMath.atan(FastMath.pow((ell.getB() / ell.getA()), 2)
                * FastMath.tan(pt.latRad()));
    }

}
