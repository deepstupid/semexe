package semexe.tables;


import semexe.basic.LogInfo;
import semexe.basic.Option;
import semexe.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Return 1 if |pred| and |target| represent the same list and 0 otherwise.
 * <p>
 * This is similar to FreebaseValueEvaluator, but
 * - does not give partial credits
 * - also check the type of the values (number, date, ...)
 *
 * @author ppasupat
 */
public class TableValueEvaluator implements ValueEvaluator {
    public static Options opts = new Options();

    public double getCompatibility(Value target, Value pred) {
        List<Value> targetList = ((ListValue) target).values;
        if (!(pred instanceof ListValue)) return 0;
        List<Value> predList = ((ListValue) pred).values;
        // Make unique
        predList = new ArrayList<>(new HashSet<>(predList));

        if (targetList.size() != predList.size()) return 0;

        for (Value targetValue : targetList) {
            boolean found = false;
            for (Value predValue : predList) {
                if (getItemCompatibility(targetValue, predValue)) {
                    found = true;
                    break;
                }
            }
            if (!found) return 0;
        }
        return 1;
    }

    // Compare one element of the list.
    protected static boolean getItemCompatibility(Value target, Value pred) {
        if (pred instanceof ErrorValue) return false;  // Never award points for error
        if (pred == null) {
            LogInfo.warning("Predicted value is null!");
            return false;
        }

        if (target instanceof DescriptionValue) {
            String targetText = ((DescriptionValue) target).value;
            if (pred instanceof NameValue || pred instanceof DescriptionValue) {
                // Just has to match the description
                String predText = (pred instanceof NameValue) ? ((NameValue) pred).description : ((DescriptionValue) pred).value;
                if (predText == null) predText = "";
                if (opts.allowNormalizedStringMatch) {
                    targetText = StringNormalizationUtils.aggressiveNormalize(targetText);
                    predText = StringNormalizationUtils.aggressiveNormalize(predText);
                }
                return targetText.equals(predText);
            } else if (pred instanceof NumberValue) {
                if (opts.allowMismatchedTypes) {
                    NumberValue targetNumber = StringNormalizationUtils.parseNumberLenient(targetText);
                    return targetNumber != null && targetNumber.equals(pred);
                }
            }
        } else if (target instanceof NumberValue) {
            NumberValue targetNumber = (NumberValue) target;
            if (pred instanceof NumberValue) {
                // Compare number
                return compareNumberValues(targetNumber, (NumberValue) pred);
            } else if (pred instanceof DateValue) {
                // Assume year
                DateValue date = (DateValue) pred;
                return date.year == targetNumber.value && date.month == -1 && date.day == -1;
            } else if (pred instanceof NameValue || pred instanceof DescriptionValue) {
                // Try converting NameValue String into NumberValue
                if (opts.allowMismatchedTypes) {
                    NumberValue predNumber = StringNormalizationUtils.toNumberValue(pred);
                    return predNumber != null && compareNumberValues(targetNumber, predNumber);
                }
            }
        } else if (target instanceof DateValue) {
            DateValue targetDate = (DateValue) target;
            if (pred instanceof DateValue) {
                // Compare date and date
                return compareDateValues(targetDate, (DateValue) pred);
            }
        }

        return target.equals(pred);
    }

    // ============================================================
    // Item Compatibility
    // ============================================================

    protected static boolean compareNumberValues(NumberValue target, NumberValue pred) {
        if (opts.ignoreNumberValueUnits) {
            return Math.abs(target.value - pred.value) < 1e-6;
        } else {
            return target.equals(pred);
        }
    }

    protected static boolean compareDateValues(DateValue target, DateValue pred) {
        if (opts.strictDateEvaluation) {
            return target.equals(pred);
        } else {
            // If a field in target is not blank (-1), pred must match target on that field
            if (target.year != -1 && target.year != pred.year) return false;
            if (target.month != -1 && target.month != pred.month) return false;
            return target.day == -1 || target.day == pred.day;
        }
    }

    public static class Options {
        @Option(gloss = "Allow type conversion on predicted values before comparison")
        public boolean allowMismatchedTypes = false;
        @Option(gloss = "Allow matching on normalized strings (e.g. remove parentheses)")
        public boolean allowNormalizedStringMatch = true;
        @Option(gloss = "When comparing number values, only consider the value and not the unit")
        public boolean ignoreNumberValueUnits = true;
        @Option(gloss = "Strict date evaluation (year, month, and date all have to match)")
        public boolean strictDateEvaluation = false;
    }

}
