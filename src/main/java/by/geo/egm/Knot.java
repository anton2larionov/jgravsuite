package by.geo.egm;

import by.geo.model.Geodetic;
import org.apache.commons.math3.util.FastMath;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Узел.
 */
public final class Knot implements Geodetic {

    private final double latDeg, lonDeg;
    private final int i, j;

    private static Map<Double, double[]> mapCos = new ConcurrentHashMap<>();
    private static Map<Double, double[]> mapSin = new ConcurrentHashMap<>();

    private final double[] cosLon, sinLon;

    /**
     * Узел.
     *
     * @param latDeg широта в градусах
     * @param lonDeg долгота в градусах
     * @param nMax   максимальна¤ степень
     */
    public Knot(final double latDeg, final double lonDeg, final int nMax,
                final int i, final int j) {
        this.latDeg = latDeg;
        this.lonDeg = lonDeg;

        this.i = i;
        this.j = j;

        final double lonRad = lonRad();

        if (mapCos.containsKey(lonRad) && mapSin.containsKey(lonRad)) {
            cosLon = mapCos.get(lonRad);
            sinLon = mapSin.get(lonRad);
        } else {
            cosLon = new double[nMax + 1];
            sinLon = new double[nMax + 1];

            for (int m = 0; m < nMax + 1; m++) {
                cosLon[m] = FastMath.cos(lonRad * m);
                sinLon[m] = FastMath.sin(lonRad * m);
            }
            mapCos.put(lonRad, cosLon);
            mapSin.put(lonRad, sinLon);
        }
    }

    public double getSinLon(final int m) {
        return sinLon[m];
    }

    public double getCosLon(final int m) {
        return cosLon[m];
    }

    @Override
    public double latDeg() {
        return latDeg;
    }

    @Override
    public double lonDeg() {
        return lonDeg;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }
}
