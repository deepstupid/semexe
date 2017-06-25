package semexe.overnight.test;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;
import semexe.overnight.SimpleWorld;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Test simple world from overnight framework.
 * Creates a small database using SimpleWorld,
 * and does sanity checks on the induced knowledge graph
 *
 * @author Yushi Wang
 */
public class SimpleWorldTest {
    public static void main(String[] args) {
        new SimpleWorldTest().externalWorldTest();
    }

    @Test
    public void externalWorldTest() {
        SimpleWorld.opts.domain = "external";
        SimpleWorld.opts.dbPath = "overnight/unittest.db";
        SimpleWorld.opts.verbose = 1;
        SimpleWorld.recreateWorld();

        AssertJUnit.assertEquals(SimpleWorld.sizeofDB(), 12);
    }
}
