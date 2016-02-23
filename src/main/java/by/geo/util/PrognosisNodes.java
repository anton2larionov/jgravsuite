package by.geo.util;

import by.geo.point.Geodetic;
import by.geo.point.Grid;
import by.geo.point.PrognosisNode;
import org.jetbrains.annotations.NotNull;

import java.util.DoubleSummaryStatistics;
import java.util.HashSet;
import java.util.Set;

public final class PrognosisNodes {

    @NotNull
    private final Set<? extends Geodetic> base;
    @NotNull
    private final Grid grid;

    public PrognosisNodes(@NotNull final Set<? extends Geodetic> geodetics,
                          @NotNull final Grid grid) {
        this.base = geodetics;
        this.grid = grid;
    }

    /**
     * Получить список точек для прогноза из грида.
     *
     * @return список точек для прогноза
     */
    @NotNull
    public Set<PrognosisNode> get() {
        final DoubleSummaryStatistics latStat = base.parallelStream()
                .mapToDouble(Geodetic::latDeg).summaryStatistics();
        final DoubleSummaryStatistics lonStat = base.parallelStream()
                .mapToDouble(Geodetic::lonDeg).summaryStatistics();

        final double minLat = latStat.getMin();
        final double minLon = lonStat.getMin();
        final double maxLat = latStat.getMax();
        final double maxLon = lonStat.getMax();

        final double dLat = grid.deltaLat();
        final double dLon = grid.deltaLon();

        final double minLatGr = grid.latMin();
        final double maxLatGr = grid.latMax();

        final double minLonGr = grid.lonMin();
        final double maxLonGr = grid.lonMax();

        final int minI = (int) ((minLat - minLatGr) / dLat);
        final int minJ = (int) ((minLon - minLonGr) / dLon);

        final int maxI = grid.rowNumber()
                - (int) ((maxLatGr - maxLat) / dLat);
        final int maxJ = grid.colNumber()
                - (int) ((maxLonGr - maxLon) / dLon);

        final Set<PrognosisNode> pred = new HashSet<>();
        for (int i = minI; i < maxI; i++) {
            for (int j = minJ; j < maxJ; j++) {
                pred.add(new PrognosisNode(
                        minLatGr + dLat * i,
                        minLonGr + dLon * j,
                        i, j,
                        grid.getValue(i, j)));
            }
        }
        return pred;
    }
}
