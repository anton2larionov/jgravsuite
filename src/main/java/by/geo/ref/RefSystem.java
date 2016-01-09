package by.geo.ref;

import org.jetbrains.annotations.NotNull;

/**
 * Геодезическая система отсчета координат.
 */
public enum RefSystem {
    GRS80 {
        @NotNull
        @Override
        public Ellipsoid ellipsoid() {
            return new Ellipsoid(6_378_137, 1. / 298.257_222_101, 3.98600_5000E+14);
        }
    },

    WGS84 {
        @NotNull
        @Override
        public Ellipsoid ellipsoid() {
            return new Ellipsoid(6_378_137, 1. / 298.257_223_563, 3.98600_4418E+14);
        }
    };

    /**
     * Получить стандартный эллипсоид.
     *
     * @return эллипсоид
     */
    @NotNull
    public abstract Ellipsoid ellipsoid();

}
