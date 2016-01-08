package by.geo.egm;

import by.geo.egm.gfc.GravityModel;
import org.apache.commons.math3.util.FastMath;

/**
 * Калькулятор ошибок высот геоида.
 */
public class ErrorCalculator {
    private final Ellipsoid ell;
    private final GravityModel egm;
    private final int nMax;

    private final double commissionError, omissionError;

    private static final double X = 1.0E-10;

    /**
     * Калькулятор ошибок высот геоида, вычисленных по EGM.
     *
     * @param egm глобальная модель геопотенциала
     */
    ErrorCalculator(final GravityModel egm) {
        this.egm = egm;

        ell = this.egm.getEllipsoid();
        nMax = this.egm.maxDegree();

        commissionError = commissionError();
        omissionError = omissionError();
    }

    private double commissionError() {
        double comm = 0;
        for (int n = 2; n <= nMax; n++) {
            double sigma = 0;
            for (int m = 0; m <= n; m++) {
                sigma += (FastMath.pow(egm.getErrorC(n, m), 2) +
                        FastMath.pow(egm.getErrorS(n, m), 2));
            }
            comm += sigma;
        }
        return comm;
    }

    private double omissionError() {
        return X / FastMath.pow((nMax + 1), 2);
    }

    /**
     * Get total error of geoidal heights modeled by EGM.
     *
     * @return total error
     */
    public double getTotalError() {
        return FastMath.hypot(getCommissionError(), getOmissionError());
    }

    /**
     * Get commission error of geoidal heights modeled by EGM.
     *
     * @return commission error
     */
    public double getCommissionError() {
        return ell.getRMean() * FastMath.sqrt(commissionError);
    }

    /**
     * Get omission error of geoidal heights modeled by EGM.
     *
     * @return omission error
     */
    public double getOmissionError() {
        return ell.getRMean() * FastMath.sqrt(omissionError);
    }

}
