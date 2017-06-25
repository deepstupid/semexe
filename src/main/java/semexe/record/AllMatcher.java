package semexe.record;

/**
 * Matches all strings.
 */
public class AllMatcher implements Matcher {
    public static final AllMatcher matcher = new AllMatcher();

    public boolean matches(String s, VarBindingList bindings) {
        return true;
    }

    public String toString() {
        return "*";
    }
}
