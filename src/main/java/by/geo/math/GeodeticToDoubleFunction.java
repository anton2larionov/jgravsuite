package by.geo.math;

import by.geo.point.Geodetic;

/**
 * Интерфейс представляет функцию, примимающую {@code Geodetic} аргумент
 * и возвращающую {@code double} результат.
 */
@FunctionalInterface
public interface GeodeticToDoubleFunction {

    /**
     * Применение функции для переданного аргумента.
     *
     * @param pt аргумент
     * @return результат
     */
    double applyAsDouble(final Geodetic pt);

}
