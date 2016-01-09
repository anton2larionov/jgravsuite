package by.geo.point;

/**
 * Грид (регулярная сетка двухмерных точек)
 * на основе двухмерного массива значений.
 */
public final class Grid {

    private final static GridBuilder builder = new GridBuilder();

    /**
     * @return Строитель грида
     */
    public static GridBuilder builder() {
        return builder;
    }

    private final double lat0;
    private final double lon0;
    private final double lat;
    private final double lon;
    private final double dlat;
    private final double dlon;

    /**
     * Число столбцов.
     */
    private final int colNumber;

    /**
     * Число строк.
     */
    private final int rowNumber;

    /**
     * Двухмерный массив значений.
     */
    private final double[][] vals;

    /**
     * Конструктор грида.
     *
     * @param lat0 минимальная широта
     * @param lon0 минимальная долгота
     * @param lat  максимальная широта
     * @param lon  максимальная долгота
     * @param dlat шаг по широте
     * @param dlon шаг по долготе
     * @throws IllegalArgumentException если {@code (lat < lat0 || lon < lon0)}
     */
    Grid(final double lat0, final double lon0, final double lat,
         final double lon, final double dlat, final double dlon) {
        if (lat < lat0 || lon < lon0) {
            throw new IllegalArgumentException("lat < lat0 || lon < lon0");
        }
        this.lat0 = lat0;
        this.lon0 = lon0;
        this.lat = lat;
        this.lon = lon;
        this.dlat = dlat;
        this.dlon = dlon;

        rowNumber = (int) ((lat - lat0 + dlat / 2) / dlat + 1);
        colNumber = (int) ((lon - lon0 + dlat / 2) / dlon + 1);

        vals = new double[rowNumber][colNumber];
    }

    /**
     * @return число столбцов данных грида
     */
    public int colNumber() {
        return colNumber;
    }

    /**
     * @return число рядов данных грида
     */
    public int rowNumber() {
        return rowNumber;
    }

    /**
     * @return минимальная широта данных грида
     */
    public double latMin() {
        return lat0;
    }

    /**
     * @return минимальная долгота данных грида
     */
    public double lonMin() {
        return lon0;
    }

    /**
     * @return максимальная широта данных грида
     */
    public double latMax() {
        return lat;
    }

    /**
     * @return максимальная долгота данных грида
     */
    public double lonMax() {
        return lon;
    }

    /**
     * @return шаг по широте
     */
    public double deltaLat() {
        return dlat;
    }

    /**
     * @return шаг по долготе
     */
    public double deltaLon() {
        return dlon;
    }

    /**
     * @param pt геодезические координаты
     * @return {@code true}
     * если координаты {@code pt} в территориальных рамках грида,
     * иначе {@code false}
     */
    public boolean isValid(final Geodetic pt) {
        final double B = pt.latDeg();
        final double L = pt.lonDeg();
        return !(B < lat0 || B > lat || L < lon0 || L > lon);
    }

    private void testIJ(final int i, final int j) {
        if (i < 0 || i >= rowNumber)
            throw new IllegalArgumentException("i is not valid");
        if (j < 0 || j > colNumber)
            throw new IllegalArgumentException("j is not valid");
    }

    /**
     * Задать значение для узла грида.
     *
     * @param i     номер ряда
     * @param j     номер столбца
     * @param value значение
     * @throws IllegalArgumentException если
     *                                  {@code (i < 0 || i >= rowNumber || j < 0 || j > colNumber)}
     */
    public void setValue(final int i, final int j, final double value) {
        testIJ(i, j);
        vals[i][j] = value;
    }

    /**
     * Получить значение из узла грида.
     *
     * @param i номер строки
     * @param j номер столбца
     * @return значение из узла грида
     * @throws IllegalArgumentException если
     *                                  {@code (i < 0 || i >= rowNumber || j < 0 || j > colNumber)}
     */
    public double getValue(final int i, final int j) {
        testIJ(i, j);
        return vals[i][j];
    }
}
