package semexe.tables.grow;


import semexe.*;
import semexe.tables.ScopedFormula;
import semexe.basic.LispTree;
import semexe.basic.Option;

/**
 * Mapping(s, r) ==> f(s, r)
 *
 * @author ppasupat
 */
public class EndGrowFn extends SemanticFn {
    public static Options opts = new Options();
    Formula formula;

    public void init(LispTree tree) {
        super.init(tree);
        formula = Formulas.formula(tree.child(1));
        if (!(formula instanceof LambdaFormula) || !(((LambdaFormula) formula).body instanceof LambdaFormula))
            throw new RuntimeException("Function for EndGrowFn must take 2 arguments (a set s and a relation r)");
    }

    public Formula getFormula() {
        return formula;
    }

    @Override
    public DerivationStream call(Example ex, Callable c) {
        return new SingleDerivationStream() {
            @Override
            public Derivation createDerivation() {
                if (c.getChildren().size() != 1)
                    throw new RuntimeException("Wrong number of argument: expected 1; got " + c.getChildren().size());
                if (!(c.child(0).formula instanceof ScopedFormula))
                    throw new RuntimeException("Wrong argument type: expected ScopedFormula; got " + c.child(0).formula);
                ScopedFormula scoped = (ScopedFormula) c.child(0).formula;
                Formula result = formula;
                result = Formulas.lambdaApply((LambdaFormula) result, scoped.head);
                result = Formulas.lambdaApply((LambdaFormula) result, scoped.relation);
                return new Derivation.Builder().withCallable(c)
                        .formula(result).type(TypeInference.inferType(result)).createDerivation();
            }
        };
    }

    public static class Options {
        @Option(gloss = "verbosity")
        public int verbose = 0;
    }
}
