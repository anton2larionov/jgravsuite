package by.geo.egm.gfc;

import by.geo.egm.Ellipsoid;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * EGM08.
 */
public final class EGM08 extends GravityModel {

    private final static double GM = 3.98_600_4415E+14;
    private final static double a = 6_378_136.3;
    private final static int nMax = 2190;

    /**
     * Конструктор EGM08.
     *
     * @param ell референц эллипсоид
     * @throws IOException
     */
    public EGM08(final String GFC, final Ellipsoid ell) throws IOException {
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

            int x = 2, y = 0;

            while ((line = br.readLine()) != null && x <= nMax) {
                rawCS = line.split("\\s++");

                setC(x, y, Double.parseDouble(rawCS[3]));
                setS(x, y, (y != 0) ? Double.parseDouble(rawCS[4]) : 0.0);

                setErrorC(x, y, Double.parseDouble(rawCS[5]));
                setErrorS(x, y, (y != 0) ? Double.parseDouble(rawCS[6]) : 0.0);

                y++;
                if (y > x) {
                    x++;
                    y = 0;
                }
            }
        }
    }
}
