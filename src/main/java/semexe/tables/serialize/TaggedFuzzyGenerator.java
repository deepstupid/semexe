package semexe.tables.serialize;


import semexe.*;
import semexe.basic.IOUtils;
import semexe.basic.LispTree;
import semexe.basic.LogInfo;
import semexe.basic.Pair;
import semexe.exec.Execution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Generate TSV files containing information about fuzzy matched objects.
 *
 * @author ppasupat
 */
public class TaggedFuzzyGenerator extends TSVGenerator implements Runnable {

    private static final String[] FIELDS = new String[]{
            "id", "type", "start", "end", "phrase", "fragment"
    };
    private Grammar grammar = new Grammar();

    public static void main(String[] args) {
        Execution.run(args, "TaggedFuzzyGeneratorMain", new TaggedFuzzyGenerator(),
                Master.getOptionsParser());
    }

    @Override
    public void run() {
        // Read grammar
        grammar.read(Grammar.opts.inPaths);
        // Read dataset
        LogInfo.begin_track("Dataset.read");
        for (Pair<String, String> pathPair : Dataset.opts.inPaths) {
            String group = pathPair.getFirst();
            String path = pathPair.getSecond();
            // Open output file
            String filename = Execution.getFile("fuzzy-" + group + ".tsv");
            out = IOUtils.openOutHard(filename);
            dump(FIELDS);
            // Read LispTrees
            LogInfo.begin_track("Reading %s", path);
            int maxExamples = Dataset.getMaxExamplesForGroup(group);
            Iterator<LispTree> trees = LispTree.proto.parseFromFile(path);
            // Go through the examples
            int n = 0;
            while (n < maxExamples) {
                // Format: (example (id ...) (utterance ...) (targetFormula ...) (targetValue ...))
                LispTree tree = trees.next();
                if (tree == null) break;
                if (tree.children.size() < 2 || !"example".equals(tree.child(0).value)) {
                    if ("metadata".equals(tree.child(0).value)) continue;
                    throw new RuntimeException("Invalid example: " + tree);
                }
                Example ex = Example.fromLispTree(tree, path + ':' + n);
                ex.preprocess();
                LogInfo.begin_track("Example %s (%d): %s => %s", ex.id, n, ex.getTokens(), ex.targetValue);
                n++;
                dumpExample(ex, tree);
                LogInfo.end_track();
            }
            out.close();
            LogInfo.logs("Finished dumping to %s", filename);
            LogInfo.end_track();
        }
        LogInfo.end_track();
    }

    @Override
    protected void dump(String... stuff) {
        assert stuff.length == FIELDS.length;
        super.dump(stuff);
    }

    private void dumpExample(Example ex, LispTree tree) {
        int n = ex.numTokens();
        for (int i = 0; i < n; i++) {
            StringBuilder sb = new StringBuilder(ex.token(i));
            for (int j = i; j < n; j++) {
                String term = sb.toString();
                Derivation deriv =
                        new Derivation.Builder()
                                .cat(Rule.phraseCat).start(i).end(j)
                                .rule(Rule.nullRule)
                                .children(Derivation.emptyList)
                                .withStringFormulaFrom(term)
                                .canonicalUtterance(term)
                                .createDerivation();
                List<Derivation> children = new ArrayList<>();
                children.add(deriv);
                // Get the derived derivations
                for (Rule rule : grammar.getRules()) {
                    SemanticFn.CallInfo c = new SemanticFn.CallInfo(rule.lhs, i, j + 1, rule, children);
                    Iterator<Derivation> itr = rule.sem.call(ex, c);
                    while (itr.hasNext()) {
                        deriv = itr.next();
                        LogInfo.logs("Found %s %s -> %s", rule.lhs, term, deriv.formula);
                        dump(ex.id, rule.lhs.substring(1), String.valueOf(i), String.valueOf(j + 1), term, deriv.formula.toString());
                    }
                }
                if (j + 1 < n)
                    sb.append(' ').append(ex.token(j + 1));
            }
        }
    }

}
