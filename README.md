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
        GfmRepo.EGM08.get("d:/EGM08", RefSystem.GRS80.ellipsoid())
                .ifPresent(model -> {

                    /** Вычисления */
                    final GeoidCalculator calc = new GeoidCalculator(model);
                    new CalcOnGrid(grid, calc).perform();
                    System.out.println(calc.errorCalculator().totalError());
                });
        
        // new GridToTXT(grid).write("d:/out.txt");

        System.out.println(
                new BilinearInterpolator(grid)
                        .applyAsDouble(
                                new Point2D(51.5, 23.5)));
