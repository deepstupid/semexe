package edu.stanford.nlp.sempre.freebase.test;

import edu.stanford.nlp.sempre.freebase.Stemmer;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class StemmerTest {
    @Test
    public void simpleStem() {
        Stemmer stemmer = new Stemmer();
        assertEquals("box", Stemmer.stem("boxes"));
        assertEquals("creat", Stemmer.stem("created"));
        assertEquals("citi", Stemmer.stem("cities"));
    }
}
