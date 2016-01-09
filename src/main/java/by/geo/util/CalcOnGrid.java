package by.geo.util;

import by.geo.math.GeoidCalculator;
import by.geo.point.Grid;
import by.geo.point.Node;
import org.apache.commons.math3.util.Pair;

import java.util.Objects;

/**
 * Вычисление высот геоида для узлов регулярной сетки.
 */
public class CalcOnGrid {

    private final Grid grid;
    private final GeoidCalculator calc;

    /**
     * Вычисление высот геоида для узлов регулярной сетки.
     *
     * @param grid регулярная сетка
     * @param calc калькулятор высот геоида
     */
    public CalcOnGrid(final Grid grid, final GeoidCalculator calc) {
        this.grid = Objects.requireNonNull(grid);
        this.calc = Objects.requireNonNull(calc);
    }

    /**
     * Выполнить вычисление высот геоида для узлов регулярной сетки.
     */
    public void perform() {
        new GridToNodes(grid).knots()
                .parallelStream()
                .map(knot -> new Pair<>(knot, calc.applyAsDouble(knot)))
                .forEach(pair -> {

                    Node node = pair.getFirst();
                    double val = pair.getSecond();

                    grid.setValue(node.getI(), node.getJ(), val);
                });
    }
}
