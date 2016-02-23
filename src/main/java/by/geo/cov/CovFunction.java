package by.geo.cov;

import org.apache.commons.math3.util.FastMath;
import org.jetbrains.annotations.NotNull;

/**
 * Ковариационная функция.
 */
public class CovFunction {

    private final @NotNull CovFunction.Type type;
    private final double variance;
    private double correlDistance;

    CovFunction(@NotNull final CovFunction.Type type, final double variance) {
        this.type = type;
        this.variance = variance;
    }

    /**
     * Ковариация.
     *
     * @return ковариация
     */
    public double covariance(final double distance) {
        return type.covariance(distance, correlDistance, variance);
    }

    /**
     * Расстояние корреляции.
     *
     * @return расстояние корреляции
     */
    public double correlDistance() {
        return correlDistance;
    }

    /**
     * Дисперсия.
     *
     * @return дисперсия
     */
    public double variance() {
        return variance;
    }

    void setCorrelDistance(final double correlDistance) {
        this.correlDistance = correlDistance;
    }

    @NotNull
    public CovFunction.Type type() {
        return type;
    }

    /**
     * Тип ковариационной функции.
     */
    public enum Type {
        gaussMarkov2 {
            @Override
            public double covariance(final double distance,
                                     final double correlDistance, final double variance) {
                final double t = distance / correlDistance;
                return variance * (1 + t) * FastMath.exp(-t);
            }
        },

        gaussMarkov3 {
            @Override
            public double covariance(final double distance,
                                     final double correlDistance, final double variance) {
                final double t = distance / correlDistance;
                final double t2 = FastMath.pow(t, 2);
                return variance * (1 + t + t2 / 3) * FastMath.exp(-t);
            }
        },

        gaussian {
            @Override
            public double covariance(final double distance,
                                     final double correlDistance, final double variance) {
                final double t = FastMath.pow(distance / correlDistance, 2);
                return variance * FastMath.exp(-t);
            }
        },

        hirvonen {
            @Override
            public double covariance(final double distance,
                                     final double correlDistance, final double variance) {
                final double t = FastMath.pow(distance / correlDistance, 2);
                return variance / (1 + t);
            }
        };

        public abstract double covariance(final double distance,
                                          final double correlDistance, final double variance);
    }
}