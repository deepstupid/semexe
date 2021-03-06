package semexe.test;

import semexe.Formula;
import semexe.Formulas;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Test Formulas.
 *
 * @author Percy Liang
 */
public class FormulaTest {
    private static Formula F(String s) {
        return Formula.fromString(s);
    }

    @Test
    public void simpleFormula() {
        AssertJUnit.assertEquals(F("(f a)"),
                Formulas.betaReduction(F("((lambda x (f (var x))) a)")));

        // Bound, shouldn't replace x
        assertEquals(F("((lambda x (f (var x))) (var y))"),
                Formulas.substituteVar(F("((lambda x (f (var x))) (var y))"), "x", F("a")));

        // Free, should replace y
        assertEquals(F("((lambda x (f (var x))) a)"),
                Formulas.substituteVar(F("((lambda x (f (var x))) (var y))"), "y", F("a")));
    }
}
