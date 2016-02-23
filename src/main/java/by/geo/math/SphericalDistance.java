package by.geo.math;

import by.geo.point.Geodetic;
import by.geo.ref.Ellipsoid;
import org.apache.commons.math3.util.FastMath;
import org.jetbrains.annotations.NotNull;

/**
 * Функция вычисления сферического расстояния.
 */
public class SphericalDistance implements GeodeticToDoubleBiFunction {

    private final @NotNull GeocentricLatitude geocentricLatitude;

    public SphericalDistance(@NotNull final Ellipsoid ell) {
        geocentricLatitude = new GeocentricLatitude(ell);
    }

    /**
     * Функция вычисления сферического расстояния.
     */
    @Override
    public double applyAsDouble(final Geodetic pt1, final Geodetic pt2) {

        final double phiA = geocentricLatitude.applyAsDouble(pt1);
        final double phiB = geocentricLatitude.applyAsDouble(pt2);

        final double lonA = pt1.lonRad();
        final double lonB = pt2.lonRad();

        final double x1 = FastMath.pow(FastMath.sin((phiA - phiB) / 2), 2);
        final double x2 = FastMath.pow(FastMath.sin((lonA - lonB) / 2), 2);
        final double x3 = FastMath.cos(phiA) * FastMath.cos(phiB);

        final double x4 = FastMath.sqrt(x1 + x2 * x3);

        return FastMath.toDegrees(2 * FastMath.asin(x4));
    }
}
