package by.geo.cov;

import by.geo.math.SphericalDistance;
import by.geo.point.ControlPoint;
import by.geo.ref.Ellipsoid;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * Эмпирическая ковариация.
 */
public class EmpiricalCov {

    /**
     * Точность.
     */
    private final static double EPS = 1E-3;

    private final double minD, maxD;
    private final @NotNull List<ControlPoint> list;
    private final int size;
    private final @NotNull CovFunction foo;
    private final @NotNull SphericalDistance sphericalDistance;
    private final @NotNull List<Sample>
            samples = new ArrayList<>(),
            data = new ArrayList<>();

    /**
     * Пространственная эмпирическая ковариация.
     */
    public EmpiricalCov(@NotNull final Collection<ControlPoint> controlPoints,
                        @NotNull final CovFunction.Type fooType,
                        @NotNull final Ellipsoid ell) {

        sphericalDistance = new SphericalDistance(ell);
        list = new ArrayList<>(controlPoints);
        size = controlPoints.size();

        minD = minDist();
        maxD = aveDist();

        foo = new CovFunction(fooType, calcVariance());
        calculate();
        findOptimalParam();
    }

    public CovFunction covFunction() {
        return foo;
    }

    private double calcVariance() {
        final double[] array = list.parallelStream()
                .mapToDouble(x -> x.signal().value()).toArray();
        return StatUtils.variance(array);
    }

    private void calculate() {
        final double ave = StatUtils.mean(list.parallelStream()
                .mapToDouble(x -> x.signal().value()).toArray());

        /** расчет эмпирических ковариаций */
        double dist, mult;

        for (int p = 0; p < size; p++) {
            for (int i = p; i < size; i++) {
                final ControlPoint a = list.get(p);
                final ControlPoint b = list.get(i);

                dist = sphericalDistance.applyAsDouble(a, b);
                if (i == p) {
                    mult = FastMath.pow(a.signal().value() - ave, 2);
                } else {
                    mult = (a.signal().value() - ave)
                            * (b.signal().value() - ave);
                }
                samples.add(new Sample(dist, mult));
            }
        }

        /** Разбиение на интервалы. */
        double start = 0.0;
        double fin = minD;

        while (fin < maxD) {
            final double left = start;
            final double right = fin;
            final Predicate<Sample> predicate = x -> (x.getDistance() > left && x
                    .getDistance() <= right);

            final double aveVal = samples.parallelStream().filter(predicate)
                    .mapToDouble(Sample::getValue).average().getAsDouble();

            final double aveDist = samples.parallelStream().filter(predicate)
                    .mapToDouble(Sample::getDistance).average().getAsDouble();

            data.add(new Sample(aveDist, aveVal));
            start = fin;
            fin += minD;
        }
    }

    /**
     * Подбор параметров ковариационной функции.
     */
    private void findOptimalParam() {
        double alpha = 0.0, res = 1E6;
        for (double newalpha = EPS; newalpha < maxD; newalpha += EPS) {
            foo.setCorrelDistance(newalpha);
            final double newres = getSqSum();
            if (newres < res) {
                alpha = newalpha;
                res = newres;
            }
        }
        foo.setCorrelDistance(alpha);
    }

    /**
     * Среднее квадратическое отклонение.
     */
    private double getSqSum() {
        return FastMath.sqrt(data
                .parallelStream()
                .mapToDouble(
                        x -> FastMath.pow(
                                x.getValue() - foo.covariance(x.getDistance()),
                                2)).average().getAsDouble());
    }

    private double minDist() {
        final List<Double> raw = new ArrayList<>(size);
        final List<Double> min = new ArrayList<>(size);
        for (int p = 0; p < size; p++) {
            for (int i = 0; i < size; i++)
                if (i != p)
                    raw.add(sphericalDistance.applyAsDouble(list.get(p), list.get(i)));
            min.add(raw.stream().mapToDouble(Double::doubleValue).min()
                    .getAsDouble());
            raw.clear();
        }
        return min.stream().mapToDouble(Double::doubleValue).average()
                .getAsDouble();
    }

    private double aveDist() {
        final List<Double> raw = new ArrayList<>(size);
        final List<Double> ave = new ArrayList<>(size);
        for (int p = 0; p < size; p++) {
            for (int i = 0; i < size; i++)
                if (i != p)
                    raw.add(sphericalDistance.applyAsDouble(list.get(p), list.get(i)));
            ave.add(raw.stream().mapToDouble(Double::doubleValue).average()
                    .getAsDouble());
            raw.clear();
        }
        return ave.stream().mapToDouble(Double::doubleValue).average()
                .getAsDouble();
    }

    private static class Sample {
        private final Pair<Double, Double> sample;

        Sample(final double distance, final double value) {
            sample = new Pair<>(distance, value);
        }

        public double getDistance() {
            return sample.getFirst();
        }

        public double getValue() {
            return sample.getSecond();
        }
    }
}
