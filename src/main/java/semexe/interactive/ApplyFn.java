package semexe.interactive;


import semexe.basic.LispTree;
import semexe.basic.Option;
import semexe.*;

import java.util.List;

/**
 * Take any number of arguments and apply them to the lambda expression given in
 * this SemanticFn TODO: type inference, some function applications
 *
 * @author sidaw
 */
public class ApplyFn extends SemanticFn {
    public static Options opts = new Options();
    Formula formula;

    public ApplyFn() {
    }

    public ApplyFn(Formula f) {
        formula = f;
    }

    @Override
    public void init(LispTree tree) {
        super.init(tree);
        formula = Formulas.formula(tree.child(1));
    }

    public Formula getFormula() {
        return formula;
    }

    @Override
    public DerivationStream call(final Example ex, final Callable c) {
        return new SingleDerivationStream() {
            @Override
            public Derivation createDerivation() {
                List<Derivation> args = c.getChildren();
                Formula f = Formulas.formula(formula.toLispTree());
                for (Derivation arg : args) {
                    if (!(f instanceof LambdaFormula))
                        throw new RuntimeException("Expected LambdaFormula, but got " + f + "; initial: " + formula);
                    f = Formulas.lambdaApply((LambdaFormula) f, arg.getFormula());
                }
                Derivation res = new Derivation.Builder().withCallable(c).formula(f).createDerivation();
                return res;
            }
        };
    }

    public static class Options {
        @Option(gloss = "verbosity")
        public int verbose = 0;
    }
}
