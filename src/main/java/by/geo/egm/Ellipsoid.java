package by.geo.egm;

import org.apache.commons.math3.util.FastMath;

/**
 * Референц эллипсоид.
 */
public final class Ellipsoid {

    private final double a, f, GM;
    private final double b;
    private final double gA;
    private final double gB;
    private final double e1;
    private final double e2;
    private final double k;
    private final double m;
    private final double q0;
    private final double C;
    private final double U;
    private final double Rm;
    private final double gamma;
    private final double w = 7.292115E-5; // стандартное значение

    /**
     * Референц эллипсоид.
     *
     * @param a  большая полуось
     * @param f  сжатие
     * @param GM геоцентрическая гравитационная постоянная
     */
    public Ellipsoid(final double a, final double f, final double GM) {
        this.a = a;
        this.f = f;
        this.GM = GM;

        final double a2 = FastMath.pow(this.a, 2);
        final double w2 = FastMath.pow(w, 2);

        b = a * (1 - f);
        double e = FastMath.sqrt(a2 - FastMath.pow(b, 2));
        e1 = e / a;
        e2 = e / b;

        U = GM * FastMath.atan(e2) / e + w2 * a2 / 3.;
        m = w2 * a2 * b / GM;

        final double q = FastMath.pow(a, 3) * w2 / GM;
        final double ee = FastMath.pow(e2, 2);

        gA = GM * (1. - 3 * q / 2 + f + FastMath.pow(f, 2) - 3. * f * q / 7.
                + FastMath.pow(f, 3) - 125. * q * FastMath.pow(f, 2)
                / 294.) / a2;

        gB = GM / a2 - 2. * w2 * b
                * (1. - 2. * ee * (e2 - FastMath.atan(e2))
                / ((3. + ee) * FastMath.atan(e2) - 3. * e2)) / 3.;

        q0 = 0.5 * ((1 + 3 / FastMath.pow(e2, 2)) * FastMath.atan(e2) - 3. / e2);

        k = (b * gB - a * gA) / (a * gA);

        C = (1. / 3.) * (1. - (2. / 15.) * (m * e2 / q0));

        Rm = (2. * a + b) / 3.;
        gamma = (2. * gA + gB) / 3.;
    }

    /**
     * Нормальный потенциал U0.
     */
    public double getU() {
        return U;
    }

    /**
     * Большая полуось.
     */
    public double getA() {
        return a;
    }

    /**
     * Малая полуось.
     */
    public double getB() {
        return b;
    }

    /**
     * Средняя сила тяжести.
     */
    public double getGammaMean() {
        return gamma;
    }

    /**
     * Средний радиус.
     */
    public double getRMean() {
        return Rm;
    }

    /**
     * Геоцентрическая гравитационная постоянная.
     */
    public double getGM() {
        return GM;
    }

    /**
     * Первый эксцентриситет.
     */
    public double getE() {
        return e1;
    }

    /**
     * Расчет нормальной силы тяжести на поверхности эллипсоида.
     *
     * @param phi широта точки в радианах
     * @return нормальная сила тяжести
     */
    public double getGamma0(final double phi) {
        return gA * (1 + k * FastMath.pow(FastMath.sin(phi), 2))
                / (FastMath.sqrt(1 - FastMath.pow(e1 * FastMath.sin(phi), 2)));
    }

    /**
     * Коэффициент динамического сжатия.
     *
     * @param n степень
     * @return коэффициент динамического сжатия
     */
    public double getJ2n(final int n) {
        return FastMath.pow(-1, n + 1) * 3 * FastMath.pow(e1, 2 * n)
                * (1 - n + 5 * n * C) / ((2 * n + 1) * (2 * n + 3));
    }

}
