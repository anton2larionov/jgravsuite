package by.geo.lsc;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.jetbrains.annotations.NotNull;

public class ErrorMatrixAdjImpl implements ErrorMatrix {
    private final @NotNull RealMatrix Css;
    private final @NotNull RealMatrix A;
    private final @NotNull RealMatrix B;
    private final @NotNull RealMatrix Q, Qt, Exx, Cts, At, Bt;

    public ErrorMatrixAdjImpl(@NotNull final RealMatrix css,
                              @NotNull final RealMatrix cst,
                              @NotNull final RealMatrix a,
                              @NotNull final RealMatrix Cinv,
                              @NotNull final RealMatrix b) {
        Css = css;
        A = a;
        B = b;

        Q = cst.multiply(Cinv);
        Qt = Q.transpose();
        At = A.transpose();
        Bt = B.transpose();
        Cts = cst.transpose();
        Exx = MatrixUtils.inverse(At.multiply(Cinv).multiply(A));
    }

    @Override
    @NotNull
    public RealMatrix standarts() {
        return Css.subtract(Q.multiply(Cts)).add(
                Q.multiply(A).subtract(B).multiply(Exx)
                        .multiply(At.multiply(Qt).subtract(Bt)));
    }

}
