package edu.stanford.nlp.sempre.overnight.test;

import org.testng.annotations.Test;

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
        edu.stanford.nlp.sempre.overnight.SimpleWorld.opts.domain = "external";
        edu.stanford.nlp.sempre.overnight.SimpleWorld.opts.dbPath = "overnight/unittest.db";
        edu.stanford.nlp.sempre.overnight.SimpleWorld.opts.verbose = 1;
        edu.stanford.nlp.sempre.overnight.SimpleWorld.recreateWorld();

        assertEquals(edu.stanford.nlp.sempre.overnight.SimpleWorld.sizeofDB(), 12);
    }
}
