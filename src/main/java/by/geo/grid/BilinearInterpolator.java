package by.geo.grid;

import by.geo.model.Geodetic;

public class BilinearInterpolator implements Interpolator {

    private final Grid grid;

    public BilinearInterpolator(final Grid grid) {
        super();
        this.grid = grid;
    }

    /**
     * Get the value corresponding to the B, L position.
     *
     * @param p точка, реализуемая интерфейс {@link by.geo.model.Geodetic}
     * @return интерполированное значение
     */
    @Override
    public double interpolate(final Geodetic p) {
        if (!grid.isValid(p)) {
            throw new IllegalArgumentException("Выход за пределы грида.");
        }
        final double B = p.latDeg();
        final double L = p.lonDeg();
        final int i = (int) ((B - grid.getLat0()) / grid.getDeltaLat());
        final int j = (int) ((L - grid.getLon0()) / grid.getDeltaLon());

        final double fx = (L - grid.getLon0()) / grid.getDeltaLon() - j;
        final double fy = (B - grid.getLat0()) / grid.getDeltaLat() - i;

        final int f = i < (grid.getRowNumber() - 1) ? i + 1 : i;
        final int g = j < (grid.getColumnNumber() - 1) ? j + 1 : j;

        final double d1 = grid.getValue(i, j);
        final double d2 = grid.getValue(f, j);
        final double d3 = grid.getValue(i, g);
        final double d4 = grid.getValue(f, g);

        return ((1 - fx) * (1 - fy) * d1 + (1 - fx) * fy * d2 + fx * (1 - fy)
                * d3 + fx * fy * d4);
    }

}
