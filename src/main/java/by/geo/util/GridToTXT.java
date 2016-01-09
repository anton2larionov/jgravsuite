package by.geo.util;

import by.geo.point.Grid;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Преобразователь грида в текстовый формат.
 */
public final class GridToTXT {

    @NotNull
    private final Grid grid;

    /**
     * @param grid грид
     */
    public GridToTXT(@NotNull final Grid grid) {
        this.grid = grid;
    }

    /**
     * Запись грида в тектовый файл в формате LBV (долгота широта значение).
     *
     * @param txtFile файл для записи
     * @throws IOException
     */
    public void write(@NotNull final String txtFile) throws IOException {

        final double lon0 = grid.lonMin();
        final double lat0 = grid.latMin();

        final double dlon = grid.deltaLon();
        final double dlat = grid.deltaLat();

        try (Writer w = Files.newBufferedWriter(Paths.get(txtFile))) {
            StringBuilder b;
            for (int i = grid.rowNumber() - 1; i >= 0; i--) {
                for (int j = 0; j < grid.colNumber(); j++) {
                    b = new StringBuilder();
                    b.append(
                            String.format("%.12f\t", lon0 + dlon * j).replace(
                                    ",", "."))
                            .append(String.format("%.12f\t", lat0 + dlat * i)
                                    .replace(",", "."))
                            .append(String.format("%.5f\t", grid.getValue(i, j))
                                    .replace(",", "."))
                            .append(String.format("%n"));

                    w.write(b.toString());
                }
            }
        }
    }
}
