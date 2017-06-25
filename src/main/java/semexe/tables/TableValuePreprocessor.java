package semexe.tables;


import semexe.basic.LogInfo;
import semexe.basic.Option;
import semexe.*;

import java.util.ArrayList;
import java.util.List;

public class TableValuePreprocessor extends TargetValuePreprocessor {
    public static Options opts = new Options();

    @Override
    public Value preprocess(Value value) {
        if (value instanceof ListValue) {
            List<Value> values = new ArrayList<>();
            for (Value entry : ((ListValue) value).values) {
                values.add(preprocess(entry));
            }
            return new ListValue(values);
        } else {
            return preprocessSingle(value);
        }
    }

    public static Value preprocessSingle(Value origTarget) {
        if (origTarget instanceof DescriptionValue) {
            String origString = ((DescriptionValue) origTarget).value;
            Value canonical = canonicalize(origString);
            if (opts.verbose >= 1)
                LogInfo.logs("Canonicalize %s --> %s", origString, canonical);
            return canonical;
        } else {
            return origTarget;
        }
    }

    /*
     * Most common origString patterns:
     * - number (4, 20, "4,000", 1996, ".15")
     * - number range ("1997/98", "2000-2005")
     * - number + unit ("4 years", "82.6 m")
     * - ordinal ("1st")
     * - date ("January 4, 1994", "7 August 2004", "9-1-1990")
     * - time -- point or amount ("4:47", "
     * - short strings (yes, no, more, less, before, after)
     * - string ("Poland", "World Championship")
     */
    protected static Value canonicalize(String origString) {
        Value answer;
        LanguageInfo languageInfo = LanguageAnalyzer.getSingleton().analyze(origString);
        // Try converting to a number.
        answer = StringNormalizationUtils.parseNumberStrict(origString);
        if (answer != null) return answer;
        //answer = StringNormalizationUtils.parseNumberWithLanguageAnalyzer(languageInfo);
        //if (answer != null) return answer;
        // Try converting to a date.
        answer = StringNormalizationUtils.parseDate(origString);
        if (answer != null) return answer;
        answer = StringNormalizationUtils.parseDateWithLanguageAnalyzer(languageInfo);
        if (answer != null) return answer;
        // Maybe it's number + unit
        answer = StringNormalizationUtils.parseNumberWithUnitStrict(origString);
        if (answer != null) return answer;
        // Just treat as a description string
        return new DescriptionValue(origString);
    }

    public static class Options {
        @Option(gloss = "Verbosity")
        public int verbose = 0;
    }

}
