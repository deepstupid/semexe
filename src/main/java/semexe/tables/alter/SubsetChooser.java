package semexe.tables.alter;

import java.util.Collection;

public interface SubsetChooser {

    Subset chooseSubset(String id, DenotationData denotationData);

    Subset chooseSubset(String id, DenotationData denotationData, Collection<Integer> forbiddenTables);

}
