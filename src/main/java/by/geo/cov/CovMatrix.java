package by.geo.cov;

import org.apache.commons.math3.linear.RealMatrix;
import org.jetbrains.annotations.NotNull;

/**
 * Ковариационная матрица.
 */
@FunctionalInterface
public interface CovMatrix {
    @NotNull RealMatrix covMatrix();
}
