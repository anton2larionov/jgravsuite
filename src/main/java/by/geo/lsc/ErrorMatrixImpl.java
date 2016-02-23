package by.geo.lsc;

import org.apache.commons.math3.linear.RealMatrix;
import org.jetbrains.annotations.NotNull;

public class ErrorMatrixImpl implements ErrorMatrix {
    private final @NotNull RealMatrix Css;
    private final @NotNull RealMatrix Q, Cts;

    public ErrorMatrixImpl(@NotNull final RealMatrix css,
                           @NotNull final RealMatrix cst,
                           @NotNull final RealMatrix Cinv) {
        Css = css;
        Q = cst.multiply(Cinv);
        Cts = cst.transpose();
    }

    @Override
    @NotNull
    public RealMatrix standarts() {
        return Css.subtract(Q.multiply(Cts));
    }

}
