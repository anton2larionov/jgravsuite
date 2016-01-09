package by.geo.util;

import by.geo.math.GeoidCalculator;
import by.geo.point.Grid;
import by.geo.point.Node;
import org.apache.commons.math3.util.Pair;
import org.jetbrains.annotations.NotNull;

/**
 * Вычисление высот геоида для узлов регулярной сетки.
 */
public class CalcOnGrid {

    @NotNull
    private final Grid grid;
    @NotNull
    private final GeoidCalculator calc;

    /**
     * Вычисление высот геоида для узлов регулярной сетки.
     *
     * @param grid регулярная сетка
     * @param calc калькулятор высот геоида
     */
    public CalcOnGrid(@NotNull final Grid grid, @NotNull final GeoidCalculator calc) {
        this.grid = grid;
        this.calc = calc;
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
