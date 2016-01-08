package by.geo.math;

import org.apache.commons.math3.util.FastMath;

/**
 * Полностью нормализованные присоединенные полиномы Лежандра.
 */
public final class LegendrePolynoms {
    private final double t, u;

    private final double[][] map;

    /**
     * Полностью нормализованные присоединенные полиномы Лежандра.
     *
     * @param phi  геоцентрическая широта
     * @param nMax максимальная степень
     */
    public LegendrePolynoms(final double phi, final int nMax) {
        t = FastMath.sin(phi);
        u = FastMath.cos(phi);

        map = new double[nMax + 1][];
        for (int i = 0; i < map.length; i++) {
            map[i] = new double[i + 1];
        }
        createPolynoms();
    }

    /**
     * Получить значение P[n][m].
     *
     * @param n степень
     * @param m порядок
     * @return значение P[n][m]
     */
    public double getValue(final int n, final int m) {
        return map[n][m];
    }

    private void createPolynoms() {
        map[0][0] = 1.0;
        map[1][0] = value(1, 0);
        map[1][1] = u * FastMath.sqrt(3.0);

        for (int n = 2; n < map.length; n++) {
            for (int m = 0; m < map[n].length; m++) {
                map[n][m] = value(n, m);
            }
        }
    }

    private double value(final int n, final int m) {
        // sectorial
        if (n == m) {
            return u * FastMath.sqrt((2. * m + 1.) / (2. * m))
                    * map[n - 1][m - 1];
        }

        // tesseral
        return getA(n, m) * getFromMap(n - 1, m) * t - getB(n, m)
                * getFromMap(n - 2, m);
    }

    private double getFromMap(final int n, final int m) {
        if (n < m) {
            return 0.0;
        }
        return map[n][m];
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
