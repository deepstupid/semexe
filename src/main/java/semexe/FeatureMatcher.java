package semexe;

public interface FeatureMatcher {
    boolean matches(String feature);
}

final class AllFeatureMatcher implements FeatureMatcher {
    public static final AllFeatureMatcher matcher = new AllFeatureMatcher();

    private AllFeatureMatcher() {
    }

    @Override
    public boolean matches(String feature) {
        return true;
    }
}

final class ExactFeatureMatcher implements FeatureMatcher {
    private String match;

    public ExactFeatureMatcher(String match) {
        this.match = match;
    }

    @Override
    public boolean matches(String feature) {
        return feature.equals(match);
    }
}

final class DenotationFeatureMatcher implements FeatureMatcher {
    public static final DenotationFeatureMatcher matcher = new DenotationFeatureMatcher();

    @Override
    public boolean matches(String feature) {
        return feature.startsWith("denotation-size") ||
                feature.startsWith("count-denotation-size");
    }
}
