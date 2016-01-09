package by.geo.point;

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

    public GridBuilder setLatMin(final double lat0) {
        this.lat0 = lat0;
        return this;
    }

    public GridBuilder setLonMin(final double lon0) {
        this.lon0 = lon0;
        return this;
    }

    public GridBuilder setLatMax(final double lat) {
        this.lat = lat;
        return this;
    }

    public GridBuilder setLonMax(final double lon) {
        this.lon = lon;
        return this;
    }

    public GridBuilder setDeltaLat(final double dlat) {
        this.dlat = dlat;
        return this;
    }

    public GridBuilder setDeltaLon(final double dlon) {
        this.dlon = dlon;
        return this;
    }

    public Grid build() {
        return new Grid(lat0, lon0, lat, lon, dlat, dlon);
    }
}
