package by.geo.grav;

import by.geo.ref.Ellipsoid;
import org.apache.commons.math3.util.FastMath;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Глобальная модель гравитационного поля.
 */
public abstract class GravityFieldModel {

    private static final int zonDeg = 5;
    private final static double W = 62_636_856.0;
    @NotNull
    private final Ellipsoid ell;

    /**
     * Путь к файлу с коэффициентами модели.
     */
    protected final Path GFC;

    /**
     * Конструктор глобальной модели гравитационного поля.
     *
     * @param GFC  имя файла с коэффициентами модели
     * @param ell  эллипсоид
     * @param nMax максимальная степень
     * @throws IllegalArgumentException если {@code nMax < 1}
     * @throws IOException
     */
    protected GravityFieldModel(@NotNull final String GFC,
                                @NotNull final Ellipsoid ell, final int nMax)
            throws IOException {

        if (nMax < 1)
            throw new IllegalArgumentException("nMax is not valid");

        this.GFC = Paths.get(GFC);
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
     * Коэффициенты.
     */
    @NotNull
    private final double[][] C, S;

    /**
     * Ошибки коэффициентов.
     */
    @NotNull
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
    @NotNull
    public Ellipsoid ellipsoid() {
        return ell;
    }

    /**
     * Формирование массивов.
     */
    private static void fillArray(@NotNull final double[][] arr) {
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
        return (ellipsoid().getGM() / getGM())
                * FastMath.pow(ellipsoid().getA() / getA(), n)
                * ellipsoid().getJ2n(n) / FastMath.sqrt(4 * n + 1);
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
