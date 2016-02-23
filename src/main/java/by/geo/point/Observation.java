package by.geo.point;

public class Observation {
    private final double value, error;

    public Observation(final double value, final double error) {
        this.value = value;
        this.error = error;
    }

    public Observation(final double value) {
        this(value, 0.0);
    }

    public Observation() {
        this(0.0, 0.0);
    }

    public double value() {
        return value;
    }

    public double error() {
        return error;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(error);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(value);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Observation)) {
            return false;
        }
        final Observation other = (Observation) obj;
        return Double.doubleToLongBits(error) == Double
                .doubleToLongBits(other.error) && Double.doubleToLongBits(value) == Double.doubleToLongBits(other.value);
    }

}
