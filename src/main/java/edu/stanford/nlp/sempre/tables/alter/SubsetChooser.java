package edu.stanford.nlp.sempre.tables.alter;

import java.util.Collection;

public interface SubsetChooser {

    Subset chooseSubset(String id, DenotationData denotationData);

    Subset chooseSubset(String id, DenotationData denotationData, Collection<Integer> forbiddenTables);

}
