package semexe.basic;

public class DoubleRef {
    public double value;

    public DoubleRef() {
        this.value = 0;
    }

    public DoubleRef(double value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }
}
