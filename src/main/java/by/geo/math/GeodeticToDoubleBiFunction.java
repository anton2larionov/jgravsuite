package by.geo.math;

import by.geo.point.Geodetic;

/**
 * Интерфейс представляет функцию, примимающую два {@code Geodetic} аргумента
 * и возвращающую {@code double} результат.
 */
@FunctionalInterface
interface GeodeticToDoubleBiFunction {

    /**
     * Применение функции для переданных аргументов.
     *
     * @param pt1 аргумент 1
     * @param pt2 аргумент 2
     * @return результат
     */
    double applyAsDouble(final Geodetic pt1, final Geodetic pt2);

}
