package semexe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import semexe.basic.LispTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the discourse context (time, place, history of exchanges).
 * This is part of an Example and used by ContextFn.
 *
 * @author Percy Liang
 */
public class ContextValue extends Value {
    public final String user;
    public final DateValue date;
    public final List<Exchange> exchanges;  // List of recent exchanges with the user
    public final KnowledgeGraph graph;  // Mini-knowledge graph that captures the context
    public ContextValue(String user, DateValue date, List<Exchange> exchanges, KnowledgeGraph graph) {
        this.user = user;
        this.date = date;
        this.exchanges = exchanges;
        this.graph = graph;
    }

    public ContextValue(String user, DateValue date, List<Exchange> exchanges) {
        this(user, date, exchanges, null);
    }

    public ContextValue(KnowledgeGraph graph) {
        this(null, null, new ArrayList(), graph);
    }

    // Example:
    //   (context (user pliang)
    //            (date 2014 4 20)
    //            (exchange "when was chopin born" (!fb:people.person.date_of_birth fb:en.frederic_chopin) (date 1810 2 22))
    //            (graph NaiveKnowledgeGraph ((string Obama) (string "born in") (string Hawaii)) ...))
    public ContextValue(LispTree tree) {
        String user = null;
        DateValue date = null;
        KnowledgeGraph graph = null;
        exchanges = new ArrayList<>();
        int n = tree.children.size();
        for (int i = 1; i < n; i++) {
            String key = tree.child(i).child(0).value;
            switch (key) {
                case "user":
                    user = tree.child(i).child(1).value;
                    break;
                case "date":
                    date = new DateValue(tree.child(i));
                    break;
                case "graph":
                    graph = KnowledgeGraph.fromLispTree(tree.child(i));
                    break;
                case "exchange":
                    exchanges.add(new Exchange(tree.child(i)));
                    break;
                default:
                    throw new RuntimeException("Invalid: " + tree.child(i));
            }
        }
        this.user = user;
        this.date = date;
        this.graph = graph;
    }

    @JsonCreator
    public static ContextValue fromString(String str) {
        return new ContextValue(LispTree.proto.tree(str));
    }

    public ContextValue withDate(DateValue newDate) {
        return new ContextValue(user, newDate, exchanges, graph);
    }

    public ContextValue withNewExchange(List<Exchange> newExchanges) {
        return new ContextValue(user, date, newExchanges, graph);
    }

    public ContextValue withGraph(KnowledgeGraph newGraph) {
        return new ContextValue(user, date, exchanges, newGraph);
    }

    public LispTree tree() {
        LispTree tree = LispTree.proto.newList();
        tree.addChild("context");
        if (user != null)
            tree.addChild(LispTree.proto.newList("user", user));
        if (date != null)
            tree.addChild(date.tree());
        // When logging examples, logging the entire graph takes too much screen space.
        // I don't think that we ever deserialize a graph from a serialized context,
        // so this should be fine.
        if (graph != null)
            tree.addChild(graph.toShortLispTree());
        for (Exchange e : exchanges)
            tree.addChild(LispTree.proto.newList("exchange", e.toLispTree()));
        return tree;
    }

    @Override
    public int hashCode() {
        int hash = 0x7ed55d16;
        hash = hash * 0xd3a2646c + user.hashCode();
        hash = hash * 0xd3a2646c + date.hashCode();
        hash = hash * 0xd3a2646c + exchanges.hashCode();
        hash = hash * 0xd3a2646c + graph.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContextValue that = (ContextValue) o;
        if (!this.user.equals(that.user)) return false;
        if (!this.date.equals(that.date)) return false;
        if (!this.exchanges.equals(that.exchanges)) return false;
        else return this.graph.equals(that.graph);
    }

    @JsonValue
    public String toString() {
        return tree().toString();
    }

    // A single exchange between the user and the system
    // Note: we are not storing the entire derivation right now.
    public static class Exchange {
        public final String utterance;
        public final Formula formula;
        public final Value value;

        public Exchange(String utterance, Formula formula, Value value) {
            this.utterance = utterance;
            this.formula = formula;
            this.value = value;
        }

        public Exchange(LispTree tree) {
            utterance = tree.child(1).value;
            formula = Formulas.formula(tree.child(2));
            value = Values.fromLispTree(tree.child(3));
        }

        public LispTree toLispTree() {
            LispTree tree = LispTree.proto.newList();
            tree.addChild("exchange");
            tree.addChild(utterance);
            tree.addChild(formula.toLispTree());
            tree.addChild(value.tree());
            return tree;
        }

        @Override
        public String toString() {
            return toLispTree().toString();
        }
    }
}
