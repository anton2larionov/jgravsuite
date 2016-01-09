package by.geo.point;

/**
 * Узел.
 */
public class Node implements Geodetic {

    private final double latDeg, lonDeg;
    private final int i, j;

    /**
     * Узел.
     *
     * @param latDeg широта в градусах
     * @param lonDeg долгота в градусах
     * @param i      первый индекс
     * @param j      второй индекс
     */
    public Node(final double latDeg, final double lonDeg,
                final int i, final int j) {
        this.latDeg = latDeg;
        this.lonDeg = lonDeg;

        this.i = i;
        this.j = j;
    }

    @Override
    public double latDeg() {
        return latDeg;
    }

    @Override
    public double lonDeg() {
        return lonDeg;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }
}
