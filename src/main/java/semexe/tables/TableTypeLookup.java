package semexe.tables;


import semexe.basic.LispTree;
import semexe.basic.LogInfo;
import semexe.basic.Option;
import semexe.*;

/**
 * Look up types for entities and properties in TableKnowledgeGraph.
 * (Delegate all decisions to TableTypeSystem.)
 *
 * @author ppasupat
 */
public class TableTypeLookup implements TypeLookup {
    public static Options opts = new Options();

    // Test cases
    public static void main(String[] args) {
        TypeLookup typeLookup = new TableTypeLookup();
        String formulaString =
                "(lambda x ((reverse >) ((reverse fb:cell.cell.number) (var x))))";
        //"(lambda x (< (< ((reverse <) ((reverse fb:row.row.next) (var x))))))";
        Formula formula = Formulas.formula(LispTree.proto.tree(formulaString));
        LogInfo.logs("%s", formulaString);
        LogInfo.logs("%s", TypeInference.inferType(formula, typeLookup));
    }

    @Override
    public SemType getEntityType(String entity) {
        if (opts.verbose >= 1)
            LogInfo.logs("TableTypeLookup.getEntityType %s", entity);
        SemType type = TableTypeSystem.getEntityTypeFromId(entity);
        if (type == null && opts.verbose >= 1)
            LogInfo.logs("TableTypeLookup.getEntityType FAIL %s", entity);
        return type;
    }

    @Override
    public SemType getPropertyType(String property) {
        if (opts.verbose >= 1)
            LogInfo.logs("TableTypeLookup.getPropertyType %s", property);
        SemType type = TableTypeSystem.getPropertyTypeFromId(property);
        if (type == null && opts.verbose >= 1)
            LogInfo.logs("TableTypeLookup.getPropertyType FAIL %s", property);
        return type;
    }

    public static class Options {
        @Option(gloss = "Verbosity")
        public int verbose = 0;
    }

}
