package by.geo.math;

import by.geo.point.Geodetic;
import by.geo.ref.Ellipsoid;
import org.apache.commons.math3.util.FastMath;
import org.jetbrains.annotations.NotNull;

/**
 * Функция вычисления локального эллипсоидального радиуса.
 */
public final class EllipsoidalRadius implements GeodeticToDoubleFunction {

    @NotNull
    private final Ellipsoid ell;

    public EllipsoidalRadius(@NotNull final Ellipsoid ell) {
        this.ell = ell;
    }

    /**
     * Вычисление локального эллипсоидального радиуса в точке {@code pt}.
     *
     * @return локальный эллипсоидальной радиус
     */
    @Override
    public double applyAsDouble(@NotNull final Geodetic pt) {
        final double e2 = FastMath.pow(ell.getE(), 2);
        final double sinPhi2 = FastMath.pow(FastMath.sin(pt.latRad()), 2);

        return ell.getA()
                * FastMath.sqrt(1 - e2 * (1 - e2) * sinPhi2
                / (1 - e2 * sinPhi2));
    }

}
