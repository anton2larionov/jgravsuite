package by.geo.point;

/**
 * Высоты квазигеоида.
 */
public interface Dzetta {

    /**
     * signal.
     */
    Observation signal();

    /**
     * by model.
     */
    Observation model();

    /**
     * geometric.
     */
    Observation geometric();
}
