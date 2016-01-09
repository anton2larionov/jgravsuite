## Geoid Modeling

Пример использования для Моделирования поверхности геоида:

        /** Формирование грида с разрешением 2,5' по широте и долготе */
        final Grid grid = Grid.builder()
                .setLatMin(51.5)
                .setLonMin(23.)
                .setLatMax(52)
                .setLonMax(23.5)
                .setDeltaLat(2.5 / 60.0)
                .setDeltaLon(2.5 / 60.0)
                .build();

        /** Создание эллисоида, модели геопотенциала и калькулятора высот геоида */
        final Ellipsoid ellipsoid = RefSystem.GRS80.ellipsoid();
        final GravityFieldModel gravityModel = new EGM08("d:/EGM08", ellipsoid);
        final GeoidCalculator calc = new GeoidCalculator(gravityModel);

        /** Вычисления */
        new CalcOnGrid(grid, calc).perform();

        /** Точность высот модели */
        System.out.println(calc.errorCalculator().totalError());

        new GridToTXT(grid).write("d:/out.txt");

        /** Интерполяция */
        System.out.println(
                new BilinearInterpolator(grid)
                        .applyAsDouble(
                                new Point2D(51.5, 23.5)));
