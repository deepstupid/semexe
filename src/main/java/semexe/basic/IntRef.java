package semexe.basic;

public class IntRef {
    public int value;

    public IntRef() {
        this.value = 0;
    }

    public IntRef(int value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }
}
