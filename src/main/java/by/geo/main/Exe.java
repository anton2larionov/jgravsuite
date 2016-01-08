package by.geo.main;

import by.geo.egm.EllipsoidFactory;
import by.geo.egm.GeoidCalculator;
import by.geo.egm.Knot;
import by.geo.egm.gfc.EGM08;
import by.geo.egm.gfc.GravityModel;
import by.geo.grid.BilinearInterpolator;
import by.geo.grid.Grid;
import by.geo.model.Geodetic;
import by.geo.util.Grid2Knots;
import org.apache.commons.math3.util.Pair;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Exe {

    /**
     * Основной алгоритм.
     */
    public static void main(final String[] args) throws Exception {

        final DateFormat format = new SimpleDateFormat("hh:mm:ss");

        System.out.println(format.format(new Date()));

        /** формирование грида с разрешением 2,5' по широте и долготе */
        final double delta = 2.5 / 60.0;
        //   final Grid grid = new Grid(51., 23., 57., 33., delta, delta);

        final Grid grid = new Grid(51., 23., 52., 24., delta, delta);
        /** создание модели квазигеоида для территории Беларуси */

        final GravityModel gravityModel =
                new EGM08("d:/EGM08", EllipsoidFactory.getGRS80());

        final GeoidCalculator calc = new GeoidCalculator(gravityModel);

        System.out.println(calc.getErrorCalculator().getTotalError());

        /** Вычисления */
        new Grid2Knots(grid).knots(gravityModel)
                .parallelStream()
                .map(knot -> new Pair<>(knot, calc.calculate(knot)))
                .forEach(pair -> {

                    Knot knot = pair.getFirst();
                    double val = pair.getSecond();

                    grid.setValue(knot.getI(), knot.getJ(), val);
                });

        grid.writeToBLH("d:/out.txt");

        System.out.println(
                new BilinearInterpolator(grid).interpolate(new Geodetic() {
                    @Override
                    public double latDeg() {
                        return 51.5;
                    }

                    @Override
                    public double lonDeg() {
                        return 23.5;
                    }
                })
        );

        System.out.println(format.format(new Date()));
    }
}
