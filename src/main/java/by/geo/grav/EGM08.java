package by.geo.grav;

import by.geo.ref.Ellipsoid;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;

/**
 * EGM08.
 * <p>
 * <p>An Earth Gravitational Model is complete to spherical harmonic
 * degree and order 2159, and contains additional coefficients extending
 * to degree 2190 and order 2159. </p>
 * <p>
 * <table>
 * <tr><td>earth_gravity_constant</td>    <td>0.3986004415E+15</td></tr>
 * <tr><td>radius</td>                    <td>0.63781363E+07</td></tr>
 * <tr><td>max_degree</td>                <td>2190</td></tr>
 * </table>
 *
 * @see <a href="http://earth-info.nga.mil/GandG/wgs84/gravitymod/egm2008/">
 * earth-info.nima.mil</a>
 */
public final class EGM08 extends GravityFieldModel {

    private final static double GM = 3.98_600_4415E+14;
    private final static double a = 6_378_136.3;
    private final static int nMax = 2190;

    /**
     * Конструктор EGM08.
     *
     * @param GFC путь к файлу с
     *            <a href="http://icgem.gfz-potsdam.de/ICGEM/shms/egm2008.gfc">
     *            коэффициентами модели</a> (в кодировке UTF-8 w/o BOM)
     * @param ell эллипсоид
     * @throws IOException
     */
    public EGM08(@NotNull final String GFC, @NotNull final Ellipsoid ell) throws IOException {
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
        try (BufferedReader br = Files.newBufferedReader(GFC)) {
            String line;
            String[] rawCS;

            int x = 2, y = 0;

            while ((line = br.readLine()) != null && x <= nMax) {
                rawCS = line.split("\\s++");

                if (!rawCS[0].equals("gfc")) continue;

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
