package by.geo.point;

import by.geo.math.GeodeticToDoubleFunction;
import org.apache.commons.math3.util.FastMath;
import org.jetbrains.annotations.NotNull;

/**
 * Контрольная точка.
 */
public final class ControlPoint extends GeodeticPoint implements Dzetta {

    @NotNull
    private final Observation geometric;
    @NotNull
    private final GeodeticToDoubleFunction interpolator;

    /**
     * Контрольная точка.
     */
    private ControlPoint(final double latDeg, final double lonDeg,
                         @NotNull final Observation normal,
                         @NotNull final Observation geodetic,
                         @NotNull final GeodeticToDoubleFunction interpolator) {
        super(latDeg, lonDeg);
        this.interpolator = interpolator;

        geometric = new Observation(geodetic.value() - normal.value(),
                FastMath.hypot(geodetic.error(), normal.error()));
    }

    @Override
    public Observation signal() {
        return new Observation(geometric.value() - model().value(),
                FastMath.hypot(geometric.error(), model().error()));
    }

    @Override
    public Observation model() {
        return new Observation(interpolator.applyAsDouble(this));
    }

    @Override
    public Observation geometric() {
        return geometric;
    }

    @Override
    public String toString() {
        return String.format("%.3f\t%.3f", geometric().value(), model().value());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ControlPoint that = (ControlPoint) o;

        return geometric.equals(that.geometric);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + geometric.hashCode();
        return result;
    }

    /**
     * Строитель контрольной точки
     */
    public static class Builder {
        private final double latDeg, lonDeg;
        @NotNull
        private final GeodeticToDoubleFunction interpolator;

        private double geodetic, normal, geodeticError, normalError;

        public Builder(final double latDeg, final double lonDeg,
                       @NotNull final GeodeticToDoubleFunction geodeticToDoubleFunction) {
            this.latDeg = latDeg;
            this.lonDeg = lonDeg;
            interpolator = geodeticToDoubleFunction;
        }

        public ControlPoint build() {
            return new ControlPoint(latDeg, lonDeg,
                    new Observation(normal, normalError == 0.0 ? 0.001 : normalError),
                    new Observation(geodetic, geodeticError == 0.0 ? 0.001 : geodeticError),
                    interpolator);
        }

        public Builder setGeodeticHeight(final double geodeticHeight) {
            geodetic = geodeticHeight;
            return this;
        }

        public Builder setNormalHeight(final double normalHeight) {
            normal = normalHeight;
            return this;
        }

        public Builder setGeodeticHeightError(final double geodeticHeightError) {
            geodeticError = geodeticHeightError;
            return this;
        }

        public Builder setNormalHeightError(final double normalHeightError) {
            normalError = normalHeightError;
            return this;
        }
    }
}