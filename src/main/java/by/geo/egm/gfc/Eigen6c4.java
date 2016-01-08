package by.geo.egm.gfc;

import by.geo.egm.Ellipsoid;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Eigen-6c4.
 */
public final class Eigen6c4 extends GravityModel {

    private final static double GM = 3.98_600_4415E+14;
    private final static double a = 6_378_136.46;
    private final static int nMax = 2190;

    public Eigen6c4(final String GFC, final Ellipsoid ell) throws IOException {
        super(GFC, ell, nMax);
    }

    @Override
    public double getGM() {
        return GM;
    }

    @Override
    public double getA() {
        return a;
    }

    @Override
    public int maxDegree() {
        return nMax;
    }

    @Override
    void readGFC() throws IOException {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(GFC))) {
            String line;
            String[] rawCS;

            int x = 0, y = 0;

            while ((line = br.readLine()) != null && x <= nMax) {
                rawCS = line.replaceAll("D", "e").split("\\s++");

                setC(x, y, Double.parseDouble(rawCS[3]));
                setS(x, y, Double.parseDouble(rawCS[4]));

                setErrorC(x, y, Double.parseDouble(rawCS[5]));
                setErrorS(x, y, Double.parseDouble(rawCS[6]));

                x++;
                if (x > nMax) {
                    x = y + 1;
                    y++;
                }
            }
        }
    }
}
