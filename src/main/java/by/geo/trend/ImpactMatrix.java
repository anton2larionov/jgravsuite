package by.geo.trend;

import by.geo.point.Geodetic;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.List;

/**
 * Матрица влияния.
 */
@FunctionalInterface
public interface ImpactMatrix {

    RealMatrix create(List<? extends Geodetic> list);

}
