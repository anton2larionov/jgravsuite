package by.geo.util;

import by.geo.point.Grid;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class GridReader {

    private GridReader() {
    }

    /**
     * Чтение грида из текстового файла BLH.
     *
     * @param grid грид
     * @param in   файл с координатами BLH
     */
    public static void makeGridAsc(@NotNull final Grid grid, @NotNull final String in)
            throws IOException {
        try (BufferedReader r = Files.newBufferedReader(Paths.get(in))) {
            for (int i = 0; i < grid.rowNumber(); i++) {
                for (int j = 0; j < grid.colNumber(); j++) {
                    final String[] s = r.readLine().trim().replaceAll(",", ".")
                            .split("\\s+");
                    grid.setValue(i, j, Double.parseDouble(s[2]));
                }
            }
        }
    }

    /**
     * Чтение грида из текстового файла BLH.
     *
     * @param grid грид
     * @param in   файл с координатами BLH
     */
    public static void makeGridDesc(@NotNull final Grid grid, @NotNull final String in)
            throws IOException {
        try (BufferedReader r = Files.newBufferedReader(Paths.get(in))) {
            for (int i = grid.rowNumber() - 1; i >= 0; i--) {
                for (int j = 0; j < grid.colNumber(); j++) {
                    final String[] s = r.readLine().trim().replaceAll(",", ".")
                            .split("\\s+");
                    grid.setValue(i, j, Double.parseDouble(s[2]));
                }
            }
        }
    }
}
