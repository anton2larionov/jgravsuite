package by.geo.egm;

/**
 * Фабрика эллипсоидов.
 */
public final class EllipsoidFactory {

    private EllipsoidFactory() {
    }

    /**
     * @return эллипсоид GRS-80
     */
    public static Ellipsoid getGRS80() {
        return new Ellipsoid(6_378_137, 1. / 298.257_222_101, 3.98600_5000E+14);
    }

    /**
     * @return эллипсоид WGS-84
     */
    public static Ellipsoid getWGS84() {
        return new Ellipsoid(6_378_137, 1. / 298.257_223_563, 3.98600_4418E+14);
    }

}
