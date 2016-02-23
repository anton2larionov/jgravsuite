package by.geo.trend;

import by.geo.point.Geodetic;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.FastMath;

import java.util.List;

/**
 * Тригонометрическая Матрица влияния.
 */
public final class TrigonometricTrend implements ImpactMatrix {

    private final int order;

    /**
     * Тригонометрическая Матрица влияния.
     *
     * @param order степень
     */
    public TrigonometricTrend(final int order) {
        if (order < 3 || order > 5) {
            throw new IllegalTrendDegreeException();
        }
        this.order = order;
    }

    @Override
    public RealMatrix create(final List<? extends Geodetic> list) {
        final double[] aCosLat = list.stream()
                .mapToDouble(p -> FastMath.cos(p.latRad())).toArray();
        final double[] aCosLon = list.stream()
                .mapToDouble(p -> FastMath.cos(p.lonRad())).toArray();

        final double[] aSinLat = list.stream()
                .mapToDouble(p -> FastMath.sin(p.latRad())).toArray();
        final double[] aSinLon = list.stream()
                .mapToDouble(p -> FastMath.sin(p.lonRad())).toArray();

        RealMatrix A;

        if (order == 3) {
            A = new Array2DRowRealMatrix(list.size(), order);
            for (int i = 0; i < list.size(); i++) {
                A.setEntry(i, 0, aCosLat[i] * aCosLon[i]);
                A.setEntry(i, 1, aCosLat[i] * aSinLon[i]);
                A.setEntry(i, 2, aSinLat[i]);
            }
        } else if (order == 4) {
            A = new Array2DRowRealMatrix(list.size(), order);
            for (int i = 0; i < list.size(); i++) {
                A.setEntry(i, 0, aCosLat[i] * aCosLon[i]);
                A.setEntry(i, 1, aCosLat[i] * aSinLon[i]);
                A.setEntry(i, 2, aSinLat[i]);
                A.setEntry(i, 3, 1.0);
            }
        } else {
            A = new Array2DRowRealMatrix(list.size(), order);
            for (int i = 0; i < list.size(); i++) {
                A.setEntry(i, 0, 1.0);
                A.setEntry(i, 1, aCosLat[i] * aCosLon[i]);
                A.setEntry(i, 2, aCosLat[i] * aSinLon[i]);
                A.setEntry(i, 3, aSinLat[i]);
                A.setEntry(i, 4, aSinLat[i] * aSinLat[i]);
            }
        }
        return A;
    }

    @Override
    public String toString() {
        return "Тригонометрическая Матрица влияния (" + order + ")";
    }
}
