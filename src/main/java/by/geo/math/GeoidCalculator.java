package by.geo.math;

import by.geo.grav.GravFieldModel;
import by.geo.point.Geodetic;
import by.geo.ref.Ellipsoid;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Калькулятор высот геоида.
 */
public final class GeoidCalculator implements GeodeticToDoubleFunction {

    private final @NotNull GravFieldModel model;
    @NotNull
    private final Ellipsoid ell;
    private final int nMax;
    private final double N0;

    @NotNull
    private final EllipsoidalRadius ellipsoidalRadius;
    @NotNull
    private final GeocentricLatitude geocentricLatitude;
    @NotNull
    private final Gamma0 gamma0;

    // Cache
    private final Map<Double, LegendrePolynoms> polynomsMap =
            Collections.synchronizedMap(new WeakHashMap<>()); // иначе OutOfMemoryError
    private final Map<Double, Pair<double[], double[]>>
            mapSinCos = new ConcurrentHashMap<>();

    /**
     * Калькулятор высот квазигеоида по глобальной модели геопотенциала.
     *
     * @param gravFieldModel глобальная модель геопотенциала
     */
    public GeoidCalculator(final @NotNull GravFieldModel gravFieldModel) {
        model = gravFieldModel;
        ell = model.ellipsoid();
        nMax = model.maxDegree();
        N0 = calculateN0();

        ellipsoidalRadius = new EllipsoidalRadius(ell);
        geocentricLatitude = new GeocentricLatitude(ell);
        gamma0 = new Gamma0(ell);
    }

    /**
     * Вычисление высоты геоида в точке <code>pt</code>.
     *
     * @param pt точка
     * @return высота геоида в метрах
     */
    @Override
    public double applyAsDouble(@NotNull final Geodetic pt) {
        // этап инициализации
        final double r = ellipsoidalRadius.applyAsDouble(pt);
        final double phi = geocentricLatitude.applyAsDouble(pt);
        final double lon = pt.lonRad();

        final double gamma = gamma0.applyAsDouble(pt);
        final double scale = model.getA() / r;

        // этап кеширования
        final LegendrePolynoms legendre =
                polynomsMap.computeIfAbsent(phi,
                        key -> new LegendrePolynoms(phi, nMax));

        final Pair<double[], double[]> pair =
                mapSinCos.computeIfAbsent(lon, this::computePair);

        final double[] sinLon = pair.getFirst();
        final double[] cosLon = pair.getSecond();

        // этап вычислений
        double N = 0, sigma;

        for (int n = 2; n <= nMax; n++) {
            sigma = 0;
            for (int m = 0; m <= n; m++) {
                sigma += (model.getC(n, m) * cosLon[m]
                        + model.getS(n, m) * sinLon[m])
                        * legendre.value(n, m);
            }
            N += (sigma * FastMath.pow(scale, n));
        }

        N *= (model.getGM() / (r * gamma));

        return (N + N0);
    }

    /**
     * Вычисление андуляции геоида нулевого порядка.
     *
     * @return андуляция геоида нулевого порядка
     */
    public double calculateN0() {
        return (model.getGM() - ell.getGM())
                / (ell.getRMean() * ell.getGammaMean())
                - (model.getW() - ell.getU()) / ell.getGammaMean();
    }

    /**
     * @return калькулятор ошибок высот геоида
     */
    @NotNull
    public GeoidErrorCalculator errorCalculator() {
        return new GeoidErrorCalculator(model);
    }

    @NotNull
    private Pair<double[], double[]> computePair(final double lonRad) {
        final double[] sinLon = new double[nMax + 1];
        final double[] cosLon = new double[nMax + 1];

        for (int m = 0; m < nMax + 1; m++) {
            sinLon[m] = FastMath.sin(lonRad * m);
            cosLon[m] = FastMath.cos(lonRad * m);
        }
        return new Pair<>(sinLon, cosLon);
    }
}
