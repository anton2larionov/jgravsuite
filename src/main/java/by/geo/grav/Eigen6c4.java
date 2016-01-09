package by.geo.grav;

import by.geo.ref.Ellipsoid;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Eigen-6c4.
 * <p>
 * <p>EIGEN-6C4 is a static global combined gravity
 * field modelup to degree and order 2190. </p>
 * <p>
 * <table>
 * <tr><td>earth_gravity_constant</td>    <td>0.3986004415E+15</td></tr>
 * <tr><td>radius</td>                    <td>0.6378136460E+07</td></tr>
 * <tr><td>max_degree</td>                <td>2190</td></tr>
 * </table>
 *
 * @see <a href="http://icgem.gfz-potsdam.de/ICGEM/documents/Foerste-et-al-EIGEN-6C4.pdf">
 * Foerste-et-al-EIGEN-6C4.pdf</a>
 */
public final class Eigen6c4 extends GravityFieldModel {

    private final static double GM = 3.98_600_4415E+14;
    private final static double a = 6_378_136.46;
    private final static int nMax = 2190;

    /**
     * Конструктор Eigen-6c4.
     *
     * @param GFC путь к файлу с
     *            <a href="http://icgem.gfz-potsdam.de/ICGEM/shms/eigen-6c4.gfc">
     *            коэффициентами модели</a> (в кодировке UTF-8 w/o BOM)
     * @param ell эллипсоид
     * @throws IOException
     */
    public Eigen6c4(@NotNull final String GFC, @NotNull final Ellipsoid ell) throws IOException {
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

            int x = 0, y = 0;

            while ((line = br.readLine()) != null && x <= nMax) {
                rawCS = line.replaceAll("D", "e").split("\\s++");

                if (!rawCS[0].equals("gfc")) continue;

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
