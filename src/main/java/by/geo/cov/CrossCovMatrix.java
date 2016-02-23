package by.geo.cov;

import by.geo.math.SphericalDistance;
import by.geo.point.Geodetic;
import by.geo.ref.Ellipsoid;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Кросс-ковариационная матрица.
 */
public class CrossCovMatrix implements CovMatrix {

    private final @NotNull CovFunction covFun;
    private final @NotNull List<? extends Geodetic> base;
    private final @NotNull List<? extends Geodetic> pred;
    private final @NotNull SphericalDistance sphericalDistance;

    public CrossCovMatrix(final @NotNull List<? extends Geodetic> pred,
                          final @NotNull List<? extends Geodetic> base,
                          final @NotNull CovFunction covFun,
                          final @NotNull Ellipsoid ell) {
        this.covFun = covFun;
        this.base = base;
        this.pred = pred;
        this.sphericalDistance = new SphericalDistance(ell);
    }

    /**
     * Создать кросс-ковариационную матрицу {@code Cst}.
     *
     * @return кросс-ковариационная матрица {@code Cst}
     */
    @Override
    @NotNull
    public RealMatrix covMatrix() {
        final RealMatrix Cst = new Array2DRowRealMatrix(pred.size(),
                base.size());

        for (int i = 0; i < pred.size(); i++) {
            for (int j = 0; j < base.size(); j++) {
                final double value = covFun.covariance(sphericalDistance
                        .applyAsDouble(pred.get(i), base.get(j)));
                Cst.setEntry(i, j, value);
            }
        }
        return Cst;
    }
}
