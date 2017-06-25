package semexe;

import semexe.basic.Evaluation;

/**
 * An Executor takes a logical form (Formula) and computes its denotation
 * (Value).
 *
 * @author Percy Liang
 */
public abstract class Executor {
    // Execute the formula in the given context.
    public abstract Response execute(Formula formula, ContextValue context);

    public static class Response {
        public final Value value;
        public final Evaluation stats;

        public Response(Value value) {
            this(value, new Evaluation());
        }
        public Response(Value value, Evaluation stats) {
            this.value = value;
            this.stats = stats;
        }
    }
}
