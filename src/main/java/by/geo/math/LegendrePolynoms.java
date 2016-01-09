package by.geo.math;

import org.apache.commons.math3.util.FastMath;

/**
 * Полностью нормализованные присоединенные полиномы Лежандра.
 */
public final class LegendrePolynoms {
    private final double t, u;

    /**
     * Двухмерный неровный массив значений.
     */
    private final double[][] vals;

    /**
     * Полностью нормализованные присоединенные полиномы Лежандра.
     *
     * @param phi  геоцентрическая широта
     * @param nMax максимальная степень
     * @throws IllegalArgumentException если {@code nMax < 1}
     */
    public LegendrePolynoms(final double phi, final int nMax) {
        if (nMax < 1)
            throw new IllegalArgumentException("nMax is not valid");

        t = FastMath.sin(phi);
        u = FastMath.cos(phi);

        vals = new double[nMax + 1][];
        for (int i = 0; i < vals.length; i++) {
            vals[i] = new double[i + 1];
        }
        createPolynoms();
    }

    /**
     * Получить значение P[n][m].
     *
     * @param n степень
     * @param m порядок
     * @return значение P[n][m]
     * @throws IllegalArgumentException если {@code (n < 0 || n > nMax || m < 0 || m > n)}
     */
    public double value(final int n, final int m) {
        if (n < 0 || n >= vals.length)
            throw new IllegalArgumentException("n is not valid");
        if (m < 0 || m > n)
            throw new IllegalArgumentException("m is not valid");

        return vals[n][m];
    }

    private void createPolynoms() {
        // стартовые значения
        vals[0][0] = 1.0;
        vals[1][0] = init(1, 0);
        vals[1][1] = u * FastMath.sqrt(3.0);

        for (int n = 2; n < vals.length; n++) {
            for (int m = 0; m < vals[n].length; m++) {
                vals[n][m] = init(n, m);
            }
        }
    }

    // Реализация реккурентного соотношения
    private double init(final int n, final int m) {
        // sectorial
        if (n == m) {
            return u * FastMath.sqrt((2. * m + 1.) / (2. * m))
                    * vals[n - 1][m - 1];
        }

        // tesseral
        return getA(n, m) * getFromArray(n - 1, m) * t - getB(n, m)
                * getFromArray(n - 2, m);
    }

    private double getFromArray(final int n, final int m) {
        if (n < m) {
            return 0.0;
        }
        return vals[n][m];
    }

    private static double getA(final int n, final int m) {
        return FastMath.sqrt(((2. * n - 1.) * (2. * n + 1.))
                / ((n - m) * (n + m)));
    }

    private static double getB(final int n, final int m) {
        return FastMath.sqrt(((2. * n + 1.) * (n + m - 1.) * (n - m - 1.))
                / ((n - m) * (n + m) * (2. * n - 3.)));
    }

}
