package by.geo.egm;

import by.geo.egm.gfc.GravityModel;
import by.geo.math.EllipsoidalRadius;
import by.geo.math.GeocentricLatitude;
import by.geo.math.LegendrePolynoms;
import org.apache.commons.math3.util.FastMath;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Калькулятор высот квазигеоида.
 */
public class GeoidCalculator {

    private final GravityModel egm;
    private final Ellipsoid ell;
    private final int nMax;
    private final double N0;

    private final EllipsoidalRadius ellipsoidalRadius;
    private final GeocentricLatitude geocentricLatitude;

    /**
     * Калькулятор высот квазигеоида по глобальной модели геопотенциала.
     *
     * @param gravityModel глобальная модель геопотенциала
     */
    public GeoidCalculator(final GravityModel gravityModel) {
        egm = gravityModel;
        ell = egm.getEllipsoid();
        nMax = egm.maxDegree();
        N0 = calculateN0();

        ellipsoidalRadius = new EllipsoidalRadius(ell);
        geocentricLatitude = new GeocentricLatitude(ell);
    }

    private final Map<Double, LegendrePolynoms> map = Collections
            .synchronizedMap(new WeakHashMap<>());

    /**
     * Вычисление высоты геоида в узле <code>p</code>.
     *
     * @param p узел
     * @return высота геоида
     */
    public double calculate(final Knot p) {
        final double r = ellipsoidalRadius.calculate(p);
        final double gamma = ell.getGamma0(p.latRad());
        final double scale = egm.getA() / r;
        final double phi = geocentricLatitude.calculate(p);

        LegendrePolynoms legendre;
        if (!map.containsKey(phi)) {
            legendre = new LegendrePolynoms(phi, nMax);
            map.put(phi, legendre);
        } else {
            legendre = map.get(phi);
        }

        double N = 0, sigma;

        for (int n = 2; n <= nMax; n++) {
            sigma = 0;
            for (int m = 0; m <= n; m++) {
                sigma += (egm.getC(n, m) * p.getCosLon(m) + egm.getS(n, m)
                        * p.getSinLon(m))
                        * legendre.getValue(n, m);
            }
            N += (sigma * FastMath.pow(scale, n));
        }

        N *= (egm.getGM() / (r * gamma));

        return (N + N0);
    }

    /**
     * Вычисление андуляции геоида нулевого порядка.
     *
     * @return андуляция геоида нулевого порядка
     */
    public double calculateN0() {
        return (egm.getGM() - ell.getGM())
                / (ell.getRMean() * ell.getGammaMean())
                - (egm.getW() - ell.getU()) / ell.getGammaMean();
    }

    public ErrorCalculator getErrorCalculator() {
        return new ErrorCalculator(egm);
    }

}
