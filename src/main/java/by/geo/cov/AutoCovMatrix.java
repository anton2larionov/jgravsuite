package by.geo.cov;

import by.geo.math.SphericalDistance;
import by.geo.point.Geodetic;
import by.geo.ref.Ellipsoid;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Автоковариационная матрица.
 */
public class AutoCovMatrix implements CovMatrix {

    private final @NotNull CovFunction covFun;
    private final @NotNull List<? extends Geodetic> list;
    private final @NotNull SphericalDistance sphericalDistance;

    public AutoCovMatrix(final @NotNull List<? extends Geodetic> list,
                         final @NotNull CovFunction covFun, @NotNull final Ellipsoid ell) {
        this.covFun = covFun;
        this.list = list;
        this.sphericalDistance = new SphericalDistance(ell);
    }

    @Override
    @NotNull
    public RealMatrix covMatrix() {
        final int size = list.size();
        final RealMatrix Ctt = new Array2DRowRealMatrix(size, size);

        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                if (i != j) {
                    final double value = covFun
                            .covariance(sphericalDistance
                                    .applyAsDouble(list.get(i), list.get(j)));
                    Ctt.setEntry(i, j, value);
                    Ctt.setEntry(j, i, value);
                } else
                    Ctt.setEntry(i, j, covFun.variance());
            }
        }
        return Ctt;
    }
}
