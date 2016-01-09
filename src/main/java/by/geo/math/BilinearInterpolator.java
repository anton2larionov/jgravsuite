package by.geo.math;

import by.geo.point.Geodetic;
import by.geo.point.Grid;

import java.util.Objects;

/**
 * Функция, осуществляющая билинейную интерполяцию по сетке значений.
 */
public final class BilinearInterpolator implements GeodeticToDoubleFunction {

    private final Grid grid;

    public BilinearInterpolator(final Grid grid) {
        this.grid = Objects.requireNonNull(grid);
    }

    /**
     * Получить значение, соответствующее координатам точки {@code pt}.
     *
     * @return интерполированное значение
     */
    @Override
    public double applyAsDouble(final Geodetic pt) {
        if (!(Objects.nonNull(pt) && grid.isValid(pt))) {
            throw new IllegalArgumentException("pt is not valid");
        }
        final double B = pt.latDeg();
        final double L = pt.lonDeg();
        final int i = (int) ((B - grid.latMin()) / grid.deltaLat());
        final int j = (int) ((L - grid.lonMin()) / grid.deltaLon());

        final double fx = (L - grid.lonMin()) / grid.deltaLon() - j;
        final double fy = (B - grid.latMin()) / grid.deltaLat() - i;

        final int f = i < (grid.rowNumber() - 1) ? i + 1 : i;
        final int g = j < (grid.colNumber() - 1) ? j + 1 : j;

        final double d1 = grid.getValue(i, j);
        final double d2 = grid.getValue(f, j);
        final double d3 = grid.getValue(i, g);
        final double d4 = grid.getValue(f, g);

        return ((1 - fx) * (1 - fy) * d1 + (1 - fx) * fy * d2 + fx * (1 - fy)
                * d3 + fx * fy * d4);
    }

}
