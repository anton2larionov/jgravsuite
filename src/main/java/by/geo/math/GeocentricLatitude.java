package by.geo.math;

import by.geo.point.Geodetic;
import by.geo.ref.Ellipsoid;
import org.apache.commons.math3.util.FastMath;
import org.jetbrains.annotations.NotNull;

/**
 * Функция вычисления сферической широты.
 */
public final class GeocentricLatitude implements GeodeticToDoubleFunction {

    @NotNull
    private final Ellipsoid ell;

    public GeocentricLatitude(@NotNull final Ellipsoid ell) {
        this.ell = ell;
    }

    /**
     * Вычисление сферической широты в точке {@code pt}.
     *
     * @return сферическая широта
     */
    @Override
    public double applyAsDouble(@NotNull final Geodetic pt) {
        return FastMath.atan(FastMath.pow((ell.getB() / ell.getA()), 2)
                * FastMath.tan(pt.latRad()));
    }

}
