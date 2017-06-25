package semexe.tables.features;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import semexe.*;
import semexe.FuzzyMatchFn.FuzzyMatchFnMode;
import semexe.tables.TableKnowledgeGraph;
import semexe.basic.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Represents a phrase in the utterance.
 * <p>
 * Also contains additional information such as POS and NER tags.
 *
 * @author ppasupat
 */
public class PhraseInfo {
    public static Options opts = new Options();
    // Caching
    private static final LoadingCache<Example, List<PhraseInfo>> cache = CacheBuilder.newBuilder()
            .maximumSize(20)
            .build(
                    new CacheLoader<Example, List<PhraseInfo>>() {
                        @Override
                        public List<PhraseInfo> load(Example ex) throws Exception {
                            List<PhraseInfo> phraseInfos = new ArrayList<>();
                            List<String> tokens = ex.languageInfo.tokens;
                            for (int s = 1; s <= opts.maxPhraseLength; s++) {
                                for (int i = 0; i <= tokens.size() - s; i++) {
                                    phraseInfos.add(new PhraseInfo(ex, i, i + s));
                                }
                            }
                            return phraseInfos;
                        }
                    });
    public final int start, end, endOffset;
    public final String text;
    public final String lemmaText;
    public final List<String> tokens;
    public final List<String> lemmaTokens;
    public final List<String> posTags;
    public final List<String> nerTags;
    public final String canonicalPosSeq;
    public final List<String> fuzzyMatchedPredicates;

    public PhraseInfo(Example ex, int start, int end) {
        this.start = start;
        this.end = end;
        LanguageInfo languageInfo = ex.languageInfo;
        this.endOffset = end - languageInfo.numTokens();
        tokens = languageInfo.tokens.subList(start, end);
        lemmaTokens = languageInfo.tokens.subList(start, end);
        posTags = languageInfo.posTags.subList(start, end);
        nerTags = languageInfo.nerTags.subList(start, end);
        text = languageInfo.phrase(start, end).toLowerCase();
        lemmaText = languageInfo.lemmaPhrase(start, end).toLowerCase();
        canonicalPosSeq = languageInfo.canonicalPosSeq(start, end);
        fuzzyMatchedPredicates = opts.computeFuzzyMatchPredicates ? getFuzzyMatchedPredicates(ex.context) : null;
    }

    public static List<PhraseInfo> getPhraseInfos(Example ex) {
        try {
            return cache.get(ex);
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    private List<String> getFuzzyMatchedPredicates(ContextValue context) {
        if (context == null || context.graph == null || !(context.graph instanceof TableKnowledgeGraph))
            return null;
        TableKnowledgeGraph graph = (TableKnowledgeGraph) context.graph;
        List<String> matchedPredicates = new ArrayList<>();
        // Assume everything is ValueFormula with NameValue inside
        List<Formula> formulas = new ArrayList<>();
        formulas.addAll(graph.getFuzzyMatchedFormulas(text, FuzzyMatchFnMode.ENTITY));
        formulas.addAll(graph.getFuzzyMatchedFormulas(text, FuzzyMatchFnMode.BINARY));
        for (Formula formula : formulas) {
            if (formula instanceof ValueFormula) {
                Value value = ((ValueFormula<?>) formula).value;
                if (value instanceof NameValue) {
                    matchedPredicates.add(((NameValue) value).id);
                }
            }
        }
        return matchedPredicates;
    }

    @Override
    public String toString() {
        return '"' + text + '"';
    }

    public static class Options {
        @Option(gloss = "Maximum number of tokens in a phrase")
        public int maxPhraseLength = 3;
        @Option(gloss = "Fuzzy match predicates")
        public boolean computeFuzzyMatchPredicates = false;
    }

}
