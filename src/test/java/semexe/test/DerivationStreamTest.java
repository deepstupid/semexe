package semexe.test;

import semexe.Derivation;
import semexe.SingleDerivationStream;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

/**
 * @author Percy Liang
 */
public class DerivationStreamTest {
    @Test
    public void single() {
        SingleDerivationStream s = new SingleDerivationStream() {
            public Derivation createDerivation() {
                return new Derivation.Builder().cat("NP").createDerivation();
            }
        };
        assertEquals(true, s.hasNext());
        assertEquals(true, s.hasNext());
        assertEquals(true, s.hasNext());
        assertEquals("NP", s.next().cat);
        assertEquals(false, s.hasNext());
        assertEquals(false, s.hasNext());

        s = new SingleDerivationStream() {
            public Derivation createDerivation() {
                return null;
            }
        };
        assertEquals(false, s.hasNext());
    }
}
