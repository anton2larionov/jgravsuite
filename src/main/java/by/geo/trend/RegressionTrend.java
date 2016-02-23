package by.geo.trend;

import by.geo.point.Geodetic;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.FastMath;

import java.util.ArrayList;
import java.util.List;

/**
 * Nested bilinear polynomial series.
 */
public final class RegressionTrend implements ImpactMatrix {
    private final int oX;
    private final int oY;
    private final int total;

    /**
     * Nested bilinear polynomial series.
     *
     * @param degreeX     max degree Lat
     * @param degreeY     max degree Lon
     * @param totalDegree max total degree Lat*Lon
     */
    public RegressionTrend(final int degreeX, final int degreeY,
                           final int totalDegree) {
        if (totalDegree < 1 || totalDegree > 5) {
            throw new IllegalTrendDegreeException();
        }
        if (degreeX > totalDegree || degreeX < 1) {
            throw new IllegalTrendDegreeException();
        }
        if (degreeY > totalDegree || degreeY < 1) {
            throw new IllegalTrendDegreeException();
        }
        oX = degreeX;
        oY = degreeY;
        total = totalDegree;
    }

    @Override
    public RealMatrix create(final List<? extends Geodetic> list) {
        final double[] aX = list.stream().mapToDouble(Geodetic::latRad)
                .toArray();
        final double[] aY = list.stream().mapToDouble(Geodetic::lonRad)
                .toArray();

        final int z = size();

        final RealMatrix A = new Array2DRowRealMatrix(list.size(), z);
        final List<Double> raw = new ArrayList<>(z);
        for (int n = 0; n < list.size(); n++) {
            raw.add(1.0);
            for (int i = 1; i <= oX; i++) {
                raw.add(FastMath.pow(aX[n], i));
            }
            for (int j = 1; j <= oY; j++) {
                raw.add(FastMath.pow(aY[n], j));
            }
            for (int i = 1; i <= oX; i++) {
                for (int j = 1; j <= oY; j++) {
                    if ((i + j) <= total) {
                        raw.add(FastMath.pow(aX[n], i) * FastMath.pow(aY[n], j));
                    }
                }
            }
            A.setRow(n, raw.stream().mapToDouble(Double::doubleValue).toArray());
            raw.clear();
        }
        return A;
    }

    private int size() {
        int size = 1;
        for (int i = 1; i <= oX; i++) {
            size++;
        }
        for (int j = 1; j <= oY; j++) {
            size++;
        }
        for (int i = 1; i <= oX; i++) {
            for (int j = 1; j <= oY; j++) {
                if ((i + j) <= total) {
                    size++;
                }
            }
        }
        return size;
    }

    @Override
    public String toString() {
        return "Nested bilinear polynomial series (" + oX + ", " + oY + ", " + total + ")";
    }

}
