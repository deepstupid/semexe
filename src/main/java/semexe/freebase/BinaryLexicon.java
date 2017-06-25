package semexe.freebase;

import com.google.common.base.Strings;
import edu.stanford.nlp.io.IOUtils;
import semexe.FeatureVector;
import semexe.Formula;
import semexe.Json;
import semexe.Params;
import semexe.freebase.lexicons.EntrySource;
import semexe.freebase.lexicons.normalizers.EntryNormalizer;
import semexe.freebase.lexicons.normalizers.PrepDropNormalizer;
import semexe.basic.*;
import semexe.exec.Execution;
import semexe.freebase.lexicons.LexicalEntry;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Lexicon for binary predicates, "born" --> fb:people.person.place_of_birth
 *
 * @author jonathanberant
 */
public final class BinaryLexicon {

    public static final String INTERSECTION = "Intersection_size_typed";
    public static Options opts = new Options();
    private static BinaryLexicon binaryLexicon;
    Map<String, List<LexicalEntry.BinaryLexicalEntry>> lexemeToEntryList = new HashMap<>();
    private EntryNormalizer lexiconLoadingNormalizer;
    private FbFormulasInfo fbFormulasInfo;

    private BinaryLexicon() throws IOException {
        if (Strings.isNullOrEmpty(opts.binaryLexiconFilesPath))
            throw new RuntimeException("Missing unary lexicon file");
        fbFormulasInfo = FbFormulasInfo.getSingleton();
        // if we omit prepositions then the lexicon normalizer does that, otherwise, it is a normalizer that does nothing
        lexiconLoadingNormalizer = new PrepDropNormalizer(); // the alignment lexicon already contains stemmed stuff so just need to drop prepositions
        read(opts.binaryLexiconFilesPath);
    }

    public static BinaryLexicon getInstance() {
        if (binaryLexicon == null)
            try {
                binaryLexicon = new BinaryLexicon();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        return binaryLexicon;
    }

    private void read(String lexiconFile) throws IOException {

        LogInfo.begin_track_printAll("Loading binary lexicon file " + lexiconFile);
        for (String line : IOUtils.readLines(lexiconFile)) {
            LexicalEntry.LexiconValue lv = Json.readValueHard(line, LexicalEntry.LexiconValue.class);
            String lexemeKey = lv.lexeme;
            String normalizedLexemeKey = lexiconLoadingNormalizer.normalize(lexemeKey);
            // add lexeme and normalized lexeme
            addEntryToMap(lexemeKey, lv);
            if (!lexemeKey.equals(normalizedLexemeKey)) {
                addEntryToMap(normalizedLexemeKey, lv);
            }
        }
        sortLexiconEntries();
        LogInfo.log("Number of entries: " + lexemeToEntryList.size());
        LogInfo.end_track();
    }

    public void addEntryToMap(String lexemeKey, LexicalEntry.LexiconValue lv) {
        List<LexicalEntry.BinaryLexicalEntry> bEntries = buildEntry(lv, lexemeKey);
        for (LexicalEntry.BinaryLexicalEntry bEntry : bEntries)
            MapUtils.addToList(lexemeToEntryList, lexemeKey, bEntry);
    }

    private void sortLexiconEntries() {
        for (List<LexicalEntry.BinaryLexicalEntry> entries : lexemeToEntryList.values()) {
            Collections.sort(entries, new BinaryLexEntryByCounterComparator());
        }
    }

    public List<LexicalEntry.BinaryLexicalEntry> buildEntry(LexicalEntry.LexiconValue lexValue, String lexemeKey) {

        EntrySource source = EntrySource.parseSourceDesc(lexValue.source);
        FbFormulasInfo.BinaryFormulaInfo info = fbFormulasInfo.getBinaryInfo(lexValue.formula);

        if (!validBinaryFormula(lexValue.formula))
            return Collections.emptyList();

        if (info == null) {
            if (opts.verbose >= 3)
                LogInfo.log("BinaryLexicon: skipping entry since there is no info for formula: " + lexValue.formula.toString());
            return Collections.emptyList();
        }
        // get alignment features
        Map<String, Double> alignmentScores = new TreeMap<>(lexValue.features);

        if (fbFormulasInfo.isCvtFormula(info) && source == EntrySource.STRING_MATCH) {

            List<LexicalEntry.BinaryLexicalEntry> entries = new ArrayList<>();
            for (FbFormulasInfo.BinaryFormulaInfo cvtInfo : fbFormulasInfo.getCvtExpansions(info)) {
                entries.add(
                        new LexicalEntry.BinaryLexicalEntry(
                                lexemeKey, lexemeKey, new HashSet<>(cvtInfo.descriptions), cvtInfo.formula, source,
                                cvtInfo.popularity, cvtInfo.expectedType1, cvtInfo.expectedType2, cvtInfo.unitId, cvtInfo.unitDesc, alignmentScores, lexValue.lexeme));
            }
            return entries;
        } else {
            LexicalEntry.BinaryLexicalEntry entry = new LexicalEntry.BinaryLexicalEntry(
                    lexemeKey, lexemeKey, new HashSet<>(info.descriptions), lexValue.formula, source,
                    info.popularity, info.expectedType1, info.expectedType2, info.unitId, info.unitDesc, alignmentScores, lexValue.lexeme);
            return Collections.singletonList(entry);
        }
    }

    public List<LexicalEntry.BinaryLexicalEntry> lookupEntries(String textDesc) throws IOException {
        List<LexicalEntry.BinaryLexicalEntry> entries = lexemeToEntryList.get(textDesc.toLowerCase());
        if (entries != null) {
            List<LexicalEntry.BinaryLexicalEntry> res = new ArrayList<>();
            for (int i = 0; i < Math.min(entries.size(), opts.maxEntries); ++i) {
                res.add(entries.get(i));
            }
            return res;
        }
        return Collections.emptyList();
    }

    /**
     * If the property has a reverse, keep it if it reversed
     */
    public boolean validBinaryFormula(Formula formula) {
        if (fbFormulasInfo.hasOpposite(formula)) {
            boolean valid = FbFormulasInfo.isReversed(formula);
            if (opts.verbose >= 3) {
                if (!valid)
                    LogInfo.logs("BinaryLexicon: invalid formula: %s", formula);
                else
                    LogInfo.logs("BinaryLexicon: valid formula: %s", formula);
            }
            return valid;
        }
        return true;
    }

    public void updateLexicon(Pair<String, Formula> lexemeFormulaPair, int support) {
        StopWatchSet.begin("BinaryLexicon.updateLexicon");
        if (opts.verbose > 0)
            LogInfo.logs("Pair=%s, score=%s", lexemeFormulaPair, support);
        boolean exists = false;
        String lexeme = lexemeFormulaPair.getFirst();
        Formula formula = lexemeFormulaPair.getSecond();

        List<LexicalEntry.BinaryLexicalEntry> bEntries = MapUtils.get(lexemeToEntryList, lexeme, Collections.emptyList());
        for (LexicalEntry.BinaryLexicalEntry bEntry : bEntries) {
            if (bEntry.formula.equals(formula)) {
                bEntry.alignmentScores.put("Feedback", (double) support);
                if (opts.verbose > 0)
                    LogInfo.logs("Entry exists: %s", bEntry);
                exists = true;
                break;
            }
        }
        if (!exists) {
            FbFormulasInfo.BinaryFormulaInfo bInfo = fbFormulasInfo.getBinaryInfo(formula);
            if (bInfo == null) {
                LogInfo.warnings("BinaryLexicon.updateLexicon: no binary info for %s", formula);
                return;
            }
            LexicalEntry.BinaryLexicalEntry newEntry =
                    new LexicalEntry.BinaryLexicalEntry(
                            lexeme, lexeme, new HashSet<>(bInfo.descriptions), bInfo.formula, EntrySource.FEEDBACK,
                            bInfo.popularity, bInfo.expectedType1, bInfo.expectedType2, bInfo.unitId, bInfo.unitDesc, new HashMap<>(), lexeme);
            MapUtils.addToList(lexemeToEntryList, lexeme, newEntry);
            newEntry.alignmentScores.put("Feedback", (double) support);
            LogInfo.logs("Adding new binary entry=%s", newEntry);

        }
        StopWatchSet.end();
    }

    public void sortLexiconByFeedback(Params params) {
        StopWatchSet.begin("BinaryLexicon.sortLexiconByFeedback");
        LogInfo.log("Number of entries: " + lexemeToEntryList.size());
        BinaryLexEntrybyFeaturesComparator comparator =
                new BinaryLexEntrybyFeaturesComparator(params);
        for (Map.Entry<String, List<LexicalEntry.BinaryLexicalEntry>> stringListEntry : lexemeToEntryList.entrySet()) {
            Collections.sort(stringListEntry.getValue(), comparator);
            if (opts.verbose > 1) {
                LogInfo.logs("Sorted list for lexeme=%s", (String) stringListEntry.getKey());
                for (LexicalEntry.BinaryLexicalEntry bEntry : stringListEntry.getValue()) {
                    FeatureVector fv = new FeatureVector();
                    LexiconFn.getBinaryEntryFeatures(bEntry, fv);
                    LogInfo.logs("Entry=%s, dotprod=%s", bEntry, fv.dotProduct(comparator.params));
                }
            }
        }
        try {
            // Output the lexicon to the execution directory.
            String path = Execution.getFile("lexicon");
            if (path != null) {
                PrintWriter writer = semexe.basic.IOUtils.openOut(path);
                for (Map.Entry<String, List<LexicalEntry.BinaryLexicalEntry>> stringListEntry : lexemeToEntryList.entrySet()) {
                    writer.println(stringListEntry.getKey() + '\t' + stringListEntry.getValue());
                }
                writer.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        StopWatchSet.end();
    }

    public static class Options {
        @Option(gloss = "Number of results return by the lexicon")
        public int maxEntries = 1000;
        @Option(gloss = "Path to binary lexicon files")
        public String binaryLexiconFilesPath = "lib/fb_data/7/binaryInfoStringAndAlignment.txt";
        @Option(gloss = "Verbosity")
        public int verbose = 0;
        @Option(gloss = "Alignment score to sort by")
        public String keyToSortBy = INTERSECTION;
    }

    public static class BinaryLexEntrybyFeaturesComparator implements Comparator<LexicalEntry.BinaryLexicalEntry> {

        public final Params params;

        public BinaryLexEntrybyFeaturesComparator(Params params) {
            this.params = params;
        }

        @Override
        public int compare(LexicalEntry.BinaryLexicalEntry entry1, LexicalEntry.BinaryLexicalEntry entry2) {

            FeatureVector features1 = new FeatureVector();
            FeatureVector features2 = new FeatureVector();
            LexiconFn.getBinaryEntryFeatures(entry1, features1);
            LexiconFn.getBinaryEntryFeatures(entry2, features2);
            double score1 = features1.dotProduct(params);
            double score2 = features2.dotProduct(params);
            if (score1 > score2) return -1;
            if (score1 < score2) return +1;
            // back off to usual thing
            double entry1Score = MapUtils.getDouble(entry1.alignmentScores, opts.keyToSortBy, 0.0);
            double entry2Score = MapUtils.getDouble(entry2.alignmentScores, opts.keyToSortBy, 0.0);

            if (entry1Score > entry2Score)
                return -1;
            if (entry1Score < entry2Score)
                return +1;
            if (entry1.popularity > entry2.popularity)
                return -1;
            if (entry1.popularity < entry2.popularity)
                return +1;
            return 0;
        }
    }

    public static class BinaryLexEntryByCounterComparator implements Comparator<LexicalEntry.BinaryLexicalEntry> {

        @Override
        public int compare(LexicalEntry.BinaryLexicalEntry entry1, LexicalEntry.BinaryLexicalEntry entry2) {
            double entry1Score = MapUtils.getDouble(entry1.alignmentScores, opts.keyToSortBy, 0.0);
            double entry2Score = MapUtils.getDouble(entry2.alignmentScores, opts.keyToSortBy, 0.0);

            if (entry1Score > entry2Score)
                return -1;
            if (entry1Score < entry2Score)
                return +1;
            if (entry1.popularity > entry2.popularity)
                return -1;
            if (entry1.popularity < entry2.popularity)
                return +1;
            // to do - this is to break ties - make more efficient
            int stringComparison = entry1.formula.toString().compareTo(entry2.formula.toString());
            if (stringComparison < 0)
                return -1;
            if (stringComparison > 0)
                return +1;
            return 0;
        }
    }
}
