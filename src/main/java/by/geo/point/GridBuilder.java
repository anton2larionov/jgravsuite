package by.geo.point;

import org.jetbrains.annotations.NotNull;

/**
 * Строитель грида.
 */
public class GridBuilder {
    private double lat0;
    private double lon0;
    private double lat;
    private double lon;
    private double dlat;
    private double dlon;

    GridBuilder() {
    }

    @NotNull
    public GridBuilder setLatMin(final double lat0) {
        this.lat0 = lat0;
        return this;
    }

    @NotNull
    public GridBuilder setLonMin(final double lon0) {
        this.lon0 = lon0;
        return this;
    }

    @NotNull
    public GridBuilder setLatMax(final double lat) {
        this.lat = lat;
        return this;
    }

    @NotNull
    public GridBuilder setLonMax(final double lon) {
        this.lon = lon;
        return this;
    }

    @NotNull
    public GridBuilder setDeltaLat(final double dlat) {
        this.dlat = dlat;
        return this;
    }

    @NotNull
    public GridBuilder setDeltaLon(final double dlon) {
        this.dlon = dlon;
        return this;
    }

    @NotNull
    public Grid build() {
        return new Grid(lat0, lon0, lat, lon, dlat, dlon);
    }
}
