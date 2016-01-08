package by.geo.egm.gfc;

import by.geo.egm.Ellipsoid;
import org.apache.commons.math3.util.FastMath;

import java.io.IOException;

/**
 * Глобальная модель гравитационного поля.
 */
public abstract class GravityModel {

    private static final int zonDeg = 5;
    private final static double W = 62_636_856.0;
    private final Ellipsoid ell;

    /**
     * Имя файла с коэффициентами модели.
     */
    protected final String GFC;

    protected GravityModel(final String GFC, final Ellipsoid ell, final int nMax)
            throws IOException {
        this.GFC = GFC;
        this.ell = ell;
        C = new double[nMax + 1][];
        S = new double[nMax + 1][];
        dC = new double[nMax + 1][];
        dS = new double[nMax + 1][];
        fillArray(C);
        fillArray(S);
        fillArray(dC);
        fillArray(dS);
        readGFC();
        zonalCorrect();
    }

    abstract void readGFC() throws IOException;

    /**
     * коэффициенты
     */
    private final double[][] C, S;

    /**
     * ошибки коэффициентов
     */
    private final double[][] dC, dS;

    /**
     * Геоцентрическая гравитационная постоянная.
     */
    public abstract double getGM();

    /**
     * Большая полуось.
     */
    public abstract double getA();

    /**
     * Потенциал W0.
     */
    public double getW() {
        return W;
    }

    /**
     * Максимальная степень.
     */
    public abstract int maxDegree();

    /**
     * Референц эллипсоид.
     */
    public Ellipsoid getEllipsoid() {
        return ell;
    }

    /**
     * Формирование массивов.
     */
    private static void fillArray(final double[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new double[i + 1];
        }
    }

    /**
     * Коррекция зональных коэффициентов.
     */
    private void zonalCorrect() {
        for (int i = 1; i <= zonDeg; i++) {
            C[2 * i][0] += getZonalC(i);
        }
    }

    /**
     * Нормированный зональный коэффициент.
     */
    private double getZonalC(final int n) {
        return (getEllipsoid().getGM() / getGM())
                * FastMath.pow(getEllipsoid().getA() / getA(), n)
                * getEllipsoid().getJ2n(n) / FastMath.sqrt(4 * n + 1);
    }

    /**
     * Коэффициент C[n][m].
     */
    public double getC(final int n, final int m) {
        return C[n][m];
    }

    /**
     * Коэффициент S[n][m].
     */
    public double getS(final int n, final int m) {
        return S[n][m];
    }

    /**
     * Ошибка коэффициента C[n][m].
     */
    public double getErrorC(final int n, final int m) {
        return dC[n][m];
    }

    /**
     * Ошибка коэффициента S[n][m].
     */
    public double getErrorS(final int n, final int m) {
        return dS[n][m];
    }

    /**
     * Задать коэффициент C[n][m].
     */
    protected void setC(final int n, final int m, final double val) {
        C[n][m] = val;
    }

    /**
     * Задать коэффициент S[n][m].
     */
    protected void setS(final int n, final int m, final double val) {
        S[n][m] = val;
    }

    /**
     * Задать oшибкy коэффициента C[n][m].
     */
    protected void setErrorC(final int n, final int m, final double val) {
        dC[n][m] = val;
    }

    /**
     * Задать oшибкy коэффициента S[n][m].
     */
    protected void setErrorS(final int n, final int m, final double val) {
        dS[n][m] = val;
    }

}
