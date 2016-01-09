package by.geo.math;

import by.geo.point.Geodetic;
import by.geo.ref.Ellipsoid;
import org.apache.commons.math3.util.FastMath;
import org.jetbrains.annotations.NotNull;

/**
 * Функция вычисления нормальной силы тяжести на поверхности эллипсоида.
 */
public final class Gamma0 implements GeodeticToDoubleFunction {

    @NotNull
    private final Ellipsoid ell;

    public Gamma0(@NotNull final Ellipsoid ell) {
        this.ell = ell;
    }

    /**
     * Вычисление нормальной силы тяжести на поверхности эллипсоида
     * в точке {@code pt}.
     *
     * @return нормальная сила тяжести
     */
    @Override
    public double applyAsDouble(@NotNull final Geodetic pt) {
        final double lat = pt.latRad();

        return ell.getGammaE() * (1 + ell.getK() * FastMath.pow(FastMath.sin(lat), 2))
                / (FastMath.sqrt(1 - FastMath.pow(ell.getE() * FastMath.sin(lat), 2)));
    }
}
