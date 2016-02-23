package by.geo.lsc;

import org.apache.commons.math3.linear.RealMatrix;
import org.jetbrains.annotations.NotNull;

interface ErrorMatrix {
    @NotNull RealMatrix standarts();
}
