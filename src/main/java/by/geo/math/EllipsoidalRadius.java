package by.geo.math;

import by.geo.egm.Ellipsoid;
import by.geo.model.Geodetic;
import org.apache.commons.math3.util.FastMath;

public class EllipsoidalRadius {

    private final Ellipsoid ell;

    public EllipsoidalRadius(final Ellipsoid ell) {
        this.ell = ell;
    }

    /**
     * Вычисление локального эллипсоидального радиуса.
     *
     * @return локальный эллипсоидальной радиус
     */
    public double calculate(final Geodetic p) {
        final double e2 = FastMath.pow(ell.getE(), 2);
        final double sinPhi2 = FastMath.pow(FastMath.sin(p.latRad()), 2);

        return ell.getA()
                * FastMath.sqrt(1 - e2 * (1 - e2) * sinPhi2
                / (1 - e2 * sinPhi2));
    }

}
