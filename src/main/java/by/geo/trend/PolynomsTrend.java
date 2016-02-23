package by.geo.trend;

import by.geo.point.Geodetic;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.FastMath;

import java.util.List;

/**
 * Полиномиальная Матрица влияния.
 */
public final class PolynomsTrend implements ImpactMatrix {

    private final double lat0, lon0;
    private final int degLat;
    private final int degLon;

    /**
     * Полиномиальная Матрица влияния.
     *
     * @param degLat степень по широте
     * @param degLon степень по долготе
     * @param lat0   центральная широта
     * @param lon0   центральная долгота
     */
    public PolynomsTrend(final int degLat, final int degLon, final double lat0,
                         final double lon0) {
        if (degLat < 1 || degLat > 5 || degLon < 1 || degLon > 5) {
            throw new IllegalTrendDegreeException();
        }
        this.degLat = degLat;
        this.degLon = degLon;
        this.lat0 = lat0;
        this.lon0 = lon0;
    }

    @Override
    public RealMatrix create(final List<? extends Geodetic> list) {
        final double[] aLat = list.stream().mapToDouble(p -> p.latDeg() - lat0)
                .toArray();
        final double[] aLon = list.stream().mapToDouble(p -> p.lonDeg() - lon0)
                .toArray();

        final int q = (degLat + 1) * (degLon + 1);

        final RealMatrix A = new Array2DRowRealMatrix(list.size(), q);
        for (int i = 0; i < list.size(); i++) {
            for (int n = 0; n <= degLat; n++) {
                for (int m = 0; m <= degLon; m++) {
                    A.setEntry(i, place(n, m), FastMath.pow(aLat[i], n)
                            * FastMath.pow(aLon[i], m));
                }
            }
        }
        return A;
    }

    private int place(final int n, final int m) {
        return degLon * n + n + m;
    }

    @Override
    public String toString() {
        return "Полиномиальная Матрица влияния (" + degLat + ", " + degLon + ")";
    }

}
