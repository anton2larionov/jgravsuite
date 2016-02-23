package by.geo.point;

import org.jetbrains.annotations.NotNull;

/**
 * Точка для прогноза.
 */
public final class PrognosisNode extends Node implements Dzetta {

    @NotNull
    private final Observation modeled, predicted;

    /**
     * Точка грида для прогноза.
     *
     * @param latDeg широта в градусах
     * @param lonDeg долгота в градусах
     * @param dzetta моделированная аномалия высоты
     * @param i      номер строки
     * @param j      номер столбца
     */
    public PrognosisNode(final double latDeg, final double lonDeg,
                         final int i, final int j,
                         final double dzetta) {
        super(latDeg, lonDeg, i, j);
        modeled = new Observation(dzetta);
        predicted = new Observation();
    }

    public PrognosisNode(@NotNull final PrognosisNode node, @NotNull final Observation predicted) {
        super(node.latDeg(), node.lonDeg(), node.getI(), node.getJ());
        this.modeled = node.modeled;
        this.predicted = predicted;
    }

    @Override
    public Observation signal() {
        return predicted;
    }

    @Override
    public Observation model() {
        return modeled;
    }

    @Override
    public Observation geometric() {
        return new Observation(modeled.value() + predicted.value());
    }

    @Override
    public String toString() {
        return String.format("%.9f\t%.9f\t%.4f\t%.4f\t%.4f",
                lonDeg(), latDeg(), geometric().value(), signal().value(), signal().error())
                .replaceAll(",", ".");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PrognosisNode that = (PrognosisNode) o;

        return modeled.equals(that.modeled) && predicted.equals(that.predicted);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + modeled.hashCode();
        result = 31 * result + predicted.hashCode();
        return result;
    }
}
