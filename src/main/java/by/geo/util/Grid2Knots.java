package by.geo.util;

import by.geo.egm.Knot;
import by.geo.egm.gfc.GravityModel;
import by.geo.grid.Grid;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 */
public class Grid2Knots {

    private final Grid grid;

    public Grid2Knots(final Grid grid) {
        this.grid = grid;
    }

    public Collection<Knot> knots(GravityModel gm) {

        final double lon0 = grid.getLon0();
        final double lat0 = grid.getLat0();

        final double dlon = grid.getDeltaLon();
        final double dlat = grid.getDeltaLat();

        final int n = gm.maxDegree();

        final Collection<Knot> knots = new ArrayList<>();

        for (int i = 0; i < grid.getRowNumber(); i++) {
            for (int j = 0; j < grid.getColumnNumber(); j++) {
                knots.add(new Knot(lat0 + dlat * i, lon0 + dlon * j, n, i, j));
            }
        }
        return knots;
    }
}
