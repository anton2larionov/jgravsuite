package by.geo.math;

import by.geo.grav.GravityFieldModel;
import by.geo.ref.Ellipsoid;
import org.apache.commons.math3.util.FastMath;
import org.jetbrains.annotations.NotNull;

/**
 * Калькулятор ошибок высот геоида.
 */
public final class GeoidErrorCalculator {
    @NotNull
    private final Ellipsoid ell;
    @NotNull
    private final GravityFieldModel model;
    private final int nMax;

    private final double commissionError, omissionError;

    private static final double X = 1.0E-10;

    /**
     * Калькулятор ошибок высот геоида, вычисленных по модели геопотенциала.
     *
     * @param gravityFieldModel глобальная модель геопотенциала
     */
    public GeoidErrorCalculator(@NotNull final GravityFieldModel gravityFieldModel) {
        model = gravityFieldModel;

        ell = model.ellipsoid();
        nMax = model.maxDegree();

        commissionError = commissionError();
        omissionError = omissionError();
    }

    private double commissionError() {
        double comm = 0;
        for (int n = 2; n <= nMax; n++) {
            double sigma = 0;
            for (int m = 0; m <= n; m++) {
                sigma += (FastMath.pow(model.getErrorC(n, m), 2) +
                        FastMath.pow(model.getErrorS(n, m), 2));
            }
            comm += sigma;
        }
        return comm;
    }

    private double omissionError() {
        return X / FastMath.pow((nMax + 1), 2);
    }

    /**
     * @return точность вычисления высоты геоида по модели геопотенциала в метрах
     */
    public double totalError() {
        return ell.getRMean() * FastMath.sqrt(commissionError + omissionError);
    }

}
