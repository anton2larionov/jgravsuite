package by.geo.math;

import by.geo.point.Geodetic;
import by.geo.ref.Ellipsoid;
import org.apache.commons.math3.util.FastMath;

import java.util.Objects;

/**
 * Функция вычисления нормальной силы тяжести на поверхности эллипсоида.
 */
public final class Gamma0 implements GeodeticToDoubleFunction {

    private final Ellipsoid ell;

    public Gamma0(final Ellipsoid ell) {
        this.ell = Objects.requireNonNull(ell);
    }

    /**
     * Вычисление нормальной силы тяжести на поверхности эллипсоида
     * в точке {@code pt}.
     *
     * @return нормальная сила тяжести
     */
    @Override
    public double applyAsDouble(final Geodetic pt) {
        Objects.nonNull(pt);
        final double lat = pt.latRad();

        return ell.getGammaE() * (1 + ell.getK() * FastMath.pow(FastMath.sin(lat), 2))
                / (FastMath.sqrt(1 - FastMath.pow(ell.getE() * FastMath.sin(lat), 2)));
    }
}
