package by.geo.lsc;

import by.geo.cov.AutoCovMatrix;
import by.geo.cov.CovFunction;
import by.geo.cov.CrossCovMatrix;
import by.geo.point.ControlPoint;
import by.geo.point.Observation;
import by.geo.point.PrognosisNode;
import by.geo.ref.Ellipsoid;
import by.geo.trend.ImpactMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DiagonalMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.FastMath;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class LSqCollocationAdjImpl extends LSqCollocationImpl {

    /**
     * Матрица влияния.
     */
    private final @NotNull ImpactMatrix impact;

    /**
     * Среднеквадратическая коллокация с параметрами.
     */
    public LSqCollocationAdjImpl(@NotNull final Collection<ControlPoint> controlPoints,
                                 @NotNull final Collection<PrognosisNode> predict,
                                 @NotNull final CovFunction covFunction,
                                 @NotNull final Ellipsoid ell,
                                 @NotNull final ImpactMatrix impact) {
        super(controlPoints, predict, covFunction, ell);
        this.impact = impact;
    }

    @Override
    @NotNull
    public Collection<PrognosisNode> prognosis() {
        /* вектор невязок */
        final double[] arrL = controlPoints.stream()
                .mapToDouble(p -> p.signal().value()).toArray();

		/* вектор дисперсий ошибок */
        final double[] arrD = controlPoints.stream()
                .mapToDouble(p -> FastMath.pow(p.signal().error(), 2))
                .toArray();

		/* матрица невязок */
        final RealMatrix L = new Array2DRowRealMatrix(arrL);

		/* авто-ковариационная матрица */
        final RealMatrix Ctt = new AutoCovMatrix(controlPoints, covFunction, ell).covMatrix();

		/* матрица шума */
        final RealMatrix D = new DiagonalMatrix(arrD);

		/* обратная общая ковариационная матрица */
        final RealMatrix Cinv = MatrixUtils.inverse(Ctt.add(D));

		/* кросс-ковариационная матрица */
        final RealMatrix Cst = new CrossCovMatrix(predict, controlPoints, covFunction, ell).covMatrix();

		/* фильтрация и уравнивание */
        /* матрица влияния */
        final RealMatrix A = impact.create(controlPoints);
        final RealMatrix At = A.transpose();

        final RealMatrix X = MatrixUtils.inverse(At.multiply(Cinv).multiply(A))
                .multiply(At).multiply(Cinv).multiply(L);

		/* параметрическая модель */
        final RealMatrix AX = A.multiply(X);
        final RealMatrix B = impact.create(predict);

		/* прогнозируемый сигнал */
        final RealMatrix S = Cst.multiply(Cinv).multiply(L.subtract(AX));

		/* прогнозируемый сигнал */
        final double[] result = B.multiply(X).add(S).transpose().getData()[0];

		/* ошибка сигнала */
        final ErrorMatrix em = new ErrorMatrixAdjImpl(new AutoCovMatrix(predict, covFunction, ell).covMatrix(), Cst, A, Cinv, B);

        final RealMatrix errS = em.standarts();
        final double[][] resErr = errS.getData();

        final Collection<PrognosisNode> c = new ArrayList<>();
        for (int i = 0; i < predict.size(); i++) {
            c.add(new PrognosisNode(predict.get(i), new Observation(result[i], FastMath
                    .sqrt(resErr[i][i]))));
        }
        return c;
    }
}
