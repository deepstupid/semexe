package semexe.test;

import semexe.Grammar;
import semexe.basic.LogInfo;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Attempt to load all grammars to test for validity.
 *
 * @author Yushi Wang
 */

public class GrammarValidityTest {
    private String[] dataPaths = new String[]{"data/", "freebase/", "tables/", "regex/", "overnight/"};

    @Test(groups = {"grammar"})
    public void readGrammars() {
        try {
            List<String> successes = new ArrayList<>(), failures = new ArrayList<>();
            for (String dataPath : dataPaths) {
                Files.walk(Paths.get(dataPath)).forEach(filePath -> {
                    try {
                        if (filePath.toString().toLowerCase().endsWith(".grammar")) {
                            Grammar test = new Grammar();
                            LogInfo.logs("Reading grammar file: %s", filePath.toString());
                            test.read(filePath.toString());
                            LogInfo.logs("Finished reading", filePath.toString());
                            successes.add(filePath.toString());
                        }
                    } catch (Exception ex) {
                        failures.add(filePath.toString());
                    }
                });
            }
            LogInfo.begin_track("Following grammar tests passed:");
            for (String path : successes)
                LogInfo.logs("%s", path);
            LogInfo.end_track();
            LogInfo.begin_track("Following grammar tests failed:");
            for (String path : failures)
                LogInfo.logs("%s", path);
            LogInfo.end_track();
            assertEquals(0, failures.size());
        } catch (Exception ex) {
            LogInfo.logs(ex.toString());
        }
    }
}
