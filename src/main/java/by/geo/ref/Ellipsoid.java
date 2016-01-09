package by.geo.ref;

import org.apache.commons.math3.util.FastMath;

/**
 * Референц эллипсоид.
 */
public final class Ellipsoid {

    /* Основные параметры. */
    private final double a, f, GM;
    private final double w = 7.292115E-5; // стандартное значение

    /* Производные параметры. */
    private final double b;
    private final double gammaE;
    private final double gammaP;
    private final double e1;
    private final double e2;
    private final double k;
    private final double C;
    private final double U;
    private final double rMean;
    private final double gammaMean;

    /**
     * Референц эллипсоид.
     *
     * @param a  большая полуось
     * @param f  сжатие
     * @param GM геоцентрическая гравитационная постоянная
     */
    Ellipsoid(final double a, final double f, final double GM) {
        this.a = a;
        this.f = f;
        this.GM = GM;

        final double a2 = FastMath.pow(this.a, 2);
        final double w2 = FastMath.pow(this.w, 2);

        b = a * (1 - f);
        double e = FastMath.sqrt(a2 - FastMath.pow(b, 2));
        e1 = e / a;
        e2 = e / b;

        U = GM * FastMath.atan(this.e2) / e + w2 * a2 / 3.;
        final double m = w2 * a2 * b / GM;

        final double q = FastMath.pow(this.a, 3) * w2 / GM;
        final double ee = FastMath.pow(this.e2, 2);

        gammaE = GM * (1. - 3 * q / 2 + f + FastMath.pow(this.f, 2) - 3. * f * q / 7.
                + FastMath.pow(this.f, 3) - 125. * q * FastMath.pow(this.f, 2)
                / 294.) / a2;

        gammaP = GM / a2 - 2. * w2 * b
                * (1. - 2. * ee * (e2 - FastMath.atan(this.e2))
                / ((3. + ee) * FastMath.atan(this.e2) - 3. * e2)) / 3.;

        final double q0 = 0.5 * ((1 + 3 / FastMath.pow(this.e2, 2))
                * FastMath.atan(this.e2) - 3. / e2);

        k = (b * gammaP - a * gammaE) / (a * gammaE);

        C = (1. / 3.) * (1. - (2. / 15.) * (m * e2 / q0));

        rMean = (2. * a + b) / 3.;
        gammaMean = (2. * gammaE + gammaP) / 3.;
    }

    /**
     * @return Нормальный потенциал
     */
    public double getU() {
        return U;
    }

    /**
     * @return Большая полуось
     */
    public double getA() {
        return a;
    }

    /**
     * @return Малая полуось
     */
    public double getB() {
        return b;
    }

    /**
     * @return Средняя сила тяжести
     */
    public double getGammaMean() {
        return gammaMean;
    }

    /**
     * @return Средний радиус
     */
    public double getRMean() {
        return rMean;
    }

    /**
     * @return Геоцентрическая гравитационная постоянная
     */
    public double getGM() {
        return GM;
    }

    /**
     * @return Первый эксцентриситет
     */
    public double getE() {
        return e1;
    }

    /**
     * @return Cила тяжести на экваторе
     */
    public double getGammaE() {
        return gammaE;
    }

    /**
     * @return Cила тяжести на полюсах
     */
    public double getGammaP() {
        return gammaP;
    }

    /**
     * @return Гравитационное сжатие
     */
    public double getK() {
        return k;
    }

    /**
     * Коэффициент динамического сжатия.
     *
     * @param n степень
     * @return коэффициент динамического сжатия
     * @throws IllegalArgumentException если {@code n < 0}
     */
    public double getJ2n(final int n) {
        if (n < 0)
            throw new IllegalArgumentException("n is not valid");
        return FastMath.pow(-1, n + 1) * 3 * FastMath.pow(e1, 2 * n)
                * (1 - n + 5 * n * C) / ((2 * n + 1) * (2 * n + 3));
    }

}
