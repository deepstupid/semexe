package semexe.servlet;

public class Value {
    public final String value;
    public final String cmpKey;

    public Value(String value) {
        this.value = value;
        this.cmpKey = null;
    }

    public Value(String value, String cmpKey) {
        this.value = value;
        this.cmpKey = cmpKey;
    }

    public Value(int value) {
        this.value = String.valueOf(value);
        this.cmpKey = String.valueOf(value);
    }

    public String toString() {
        return value;
    }
}
