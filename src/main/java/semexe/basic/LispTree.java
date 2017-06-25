package semexe.basic;

public class LispTree extends AbstractLispTree<LispTree> {
    public static LispTree proto = new LispTree();

    protected LispTree newTree() {
        return new LispTree();
    }
}
