package by.geo.grid;

import by.geo.model.Geodetic;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Грид (регулярная сетка двухмерных точек).
 */
public final class Grid {

    /**
     * Число столбцов.
     */
    private final int colNumber;

    /**
     * Число строк.
     */
    private final int rowNumber;

    private final double lat0, lon0, lat, lon;
    private final double dlat, dlon;

    /**
     * Двухмерный массив значений.
     */
    private final double[][] values;

    /**
     * Конструктор грида.
     *
     * @param lat0 минимальная широта
     * @param lon0 минимальная долгота
     * @param lat  максимальная широта
     * @param lon  максимальная долгота
     * @param dlat шаг по широте
     * @param dlon шаг по долготе
     * @throws IllegalArgumentException
     */
    public Grid(final double lat0, final double lon0, final double lat,
                final double lon, final double dlat, final double dlon)
            throws IllegalArgumentException {
        if (lat < lat0 || lon < lon0) {
            throw new IllegalArgumentException();
        }
        this.lat0 = lat0;
        this.lon0 = lon0;
        this.lat = lat;
        this.lon = lon;
        this.dlat = dlat;
        this.dlon = dlon;

        rowNumber = (int) ((lat - lat0) / dlat + 1);
        colNumber = (int) ((lon - lon0) / dlon + 1);

        values = new double[rowNumber][colNumber];
    }

    /**
     * Get the number of columns of this grid.
     */
    public int getColumnNumber() {
        return colNumber;
    }

    /**
     * Get the number of rows of this grid.
     */
    public int getRowNumber() {
        return rowNumber;
    }

    /**
     * Get the real world lat (B) of the first column.
     */
    public double getLat0() {
        return lat0;
    }

    /**
     * Get the real world lon (L) of the first row.
     */
    public double getLon0() {
        return lon0;
    }

    /**
     * Get the lat (B) of the last grid column.
     */
    public double getLatLast() {
        return lat;
    }

    /**
     * Get the lon (L) of the last grid row.
     */
    public double getLonLast() {
        return lon;
    }

    /**
     * Real world interval between two consecutive columns.
     */
    public double getDeltaLat() {
        return dlat;
    }

    /**
     * Real world interval between two consecutive row.
     */
    public double getDeltaLon() {
        return dlon;
    }

    public boolean isValid(final Geodetic p) {
        final double B = p.latDeg();
        final double L = p.lonDeg();
        return !(B < lat0 || B > lat || L < lon0 || L > lon);
    }

    /**
     * Задать значение для узла грида.
     *
     * @param i     номер строки
     * @param j     номер столбца
     * @param value значение
     */
    public void setValue(final int i, final int j, final double value) {
        values[i][j] = value;
    }

    /**
     * Получить значение из узла грида.
     *
     * @param i номер строки
     * @param j номер столбца
     * @return значение из узла грида
     */
    public double getValue(final int i, final int j) {
        return values[i][j];
    }

    /**
     * Запись грида в тектовый файл в формате BLH.
     *
     * @param txtFile файл для записи
     * @throws IOException
     */
    public void writeToBLH(final String txtFile) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(Paths.get(txtFile))) {
            StringBuilder b;
            for (int i = getRowNumber() - 1; i >= 0; i--) {
                for (int j = 0; j < getColumnNumber(); j++) {
                    b = new StringBuilder();
                    b.append(
                            String.format("%.12f\t", lon0 + dlon * j).replace(
                                    ",", "."))
                            .append(String.format("%.12f\t", lat0 + dlat * i)
                                    .replace(",", "."))
                            .append(String.format("%.5f\t", getValue(i, j))
                                    .replace(",", "."))
                            .append(String.format("%n"));

                    w.write(b.toString());
                }
            }
        }
    }
}
