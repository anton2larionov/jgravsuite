package by.geo;

import by.geo.cov.CovFunction;
import by.geo.cov.EmpiricalCov;
import by.geo.grav.GfmRepo;
import by.geo.lsc.LSqCollocationAdjImpl;
import by.geo.math.BilinearInterpolator;
import by.geo.math.GeodeticToDoubleFunction;
import by.geo.math.GeoidCalculator;
import by.geo.point.*;
import by.geo.ref.Ellipsoid;
import by.geo.ref.RefSystem;
import by.geo.trend.ImpactMatrix;
import by.geo.trend.RegressionTrend;
import by.geo.util.CalcOnGrid;
import by.geo.util.PrognosisNodes;
import org.apache.commons.math3.util.FastMath;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Set;
import java.util.function.ToDoubleFunction;

import static java.lang.Double.parseDouble;
import static java.util.stream.Collectors.toSet;

public class Main {

    /**
     * Основной алгоритм.
     */
    public static void main(final String[] args) throws Exception {
        /** Формирование грида с разрешением 2,5' по широте и долготе */
        final Grid grid = Grid.builder()
                .setLatMin(51.)
                .setLonMin(23.)
                .setLatMax(57.)
                .setLonMax(33.)
                .setDeltaLat(2.5 / 60.0)
                .setDeltaLon(2.5 / 60.0)
                .build();

        /** Создание эллисоида, модели геопотенциала и калькулятора высот геоида */
        final Ellipsoid ellipsoid = RefSystem.GRS80.ellipsoid(); // отсчетный эллипсоид

        GfmRepo.EGM08.get(
                "d:/EGM08", // путь к файлу
                ellipsoid)
                .ifPresent(model -> {
                    final GeoidCalculator calc = new GeoidCalculator(model);
                    new CalcOnGrid(grid, calc).perform();
                });

        final GeodeticToDoubleFunction geodeticToDouble = new BilinearInterpolator(grid);

        /** исходные точки */
        final Set<ControlPoint> controlPoints = Files
                .lines(Paths.get("c:/points.dat"))
                .map(line -> line.replaceAll(",", "."))
                .map(line -> line.split("\\s++"))
                .map(raw -> new ControlPoint.Builder(
                        parseDouble(raw[1]),
                        parseDouble(raw[2]),
                        geodeticToDouble)
                        .setNormalHeight(parseDouble(raw[3]))
                        .setGeodeticHeight(parseDouble(raw[4]))
                        .setNormalHeightError(parseDouble(raw[5]))
                        .setGeodeticHeightError(parseDouble(raw[6])).build())
                .collect(toSet());

        /** узлы сетки для прогноза */
        final Set<PrognosisNode> predict = new PrognosisNodes(controlPoints, grid).get();

        /**
         * Определение типа ков. функции, построение эмпирической ковариации,
         * оценивание параметров ков. функции, нахождение матрицы влияния.
         */
        final CovFunction.Type ctype = CovFunction.Type.hirvonen;
        final EmpiricalCov empirical = new EmpiricalCov(controlPoints, ctype, ellipsoid);
        final ImpactMatrix impact = new RegressionTrend(1, 1, 1);

        /** cреднеквадратическая коллокация */
        final Collection<PrognosisNode> predicted = new LSqCollocationAdjImpl(
                controlPoints, predict, empirical.covFunction(), ellipsoid, impact)
                .prognosis();

        /** корректировка узлов модели */
        predicted.forEach(node -> grid.setValue(node.getI(), node.getJ(),
                node.geometric().value()));

        //  new GridToTXT(grid).write("d:/out.txt");
    }

    /**
     * СКО сигналов.
     */
    private static double RMSofSignals(@NotNull final Collection<? extends Dzetta> c,
                                       @NotNull final ToDoubleFunction<? super Observation> mapper) {

        return FastMath.sqrt(c.stream().map(Dzetta::signal).mapToDouble(mapper)
                .map(p -> FastMath.pow(p, 2)).average().orElse(0.0));
    }
}
