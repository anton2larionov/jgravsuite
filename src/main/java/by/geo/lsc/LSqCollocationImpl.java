package by.geo.lsc;

import by.geo.cov.AutoCovMatrix;
import by.geo.cov.CovFunction;
import by.geo.cov.CrossCovMatrix;
import by.geo.point.ControlPoint;
import by.geo.point.Observation;
import by.geo.point.PrognosisNode;
import by.geo.ref.Ellipsoid;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DiagonalMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.FastMath;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class LSqCollocationImpl implements LSqCollocation {

    /**
     * Исходные точки.
     */
    final @NotNull List<ControlPoint> controlPoints;

    /**
     * Узлы сетки для прогноза.
     */
    final @NotNull List<PrognosisNode> predict;

    /**
     * Ковариационная функция.
     */
    final @NotNull CovFunction covFunction;

    /**
     * Эллипсоид
     */
    final @NotNull Ellipsoid ell;

    /**
     * Среднеквадратическая коллокация без параметров.
     */
    public LSqCollocationImpl(@NotNull final Collection<ControlPoint> controlPoints,
                              @NotNull final Collection<PrognosisNode> predict,
                              @NotNull final CovFunction covFunction,
                              @NotNull final Ellipsoid ell) {
        this.ell = ell;
        this.controlPoints = controlPoints.stream().collect(Collectors.toList());
        this.predict = predict.stream().collect(Collectors.toList());
        this.covFunction = covFunction;
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
        final RealMatrix Ctt = new AutoCovMatrix(controlPoints, covFunction, ell)
                .covMatrix();

		/* матрица шума */
        final RealMatrix D = new DiagonalMatrix(arrD);

		/* обратная общая ковариационная матрица */
        final RealMatrix Cinv = MatrixUtils.inverse(Ctt.add(D));

		/* кросс-ковариационная матрица */
        final RealMatrix Cst = new CrossCovMatrix(predict, controlPoints, covFunction, ell).covMatrix();

		/* прогнозируемый сигнал */
        final RealMatrix S = Cst.multiply(Cinv).multiply(L);
        final double[] result = S.transpose().getData()[0];

		/* ошибка сигнала */
        final RealMatrix errS = new ErrorMatrixImpl(new AutoCovMatrix(predict, covFunction, ell).covMatrix(), Cst, Cinv).standarts();
        final double[][] resErr = errS.getData();

        final Collection<PrognosisNode> c = new ArrayList<>();
        for (int i = 0; i < predict.size(); i++) {
            c.add(new PrognosisNode(predict.get(i), new Observation(result[i], FastMath
                    .sqrt(resErr[i][i]))));
        }
        return c;
    }

}
