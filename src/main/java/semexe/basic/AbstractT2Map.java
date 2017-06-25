package semexe.basic;

/**
 * Just a dummy template right now.
 * TODO: move functionality in here.
 */
public abstract class AbstractT2Map<S extends Comparable<S>, T extends Comparable<T>> implements MemUsage.Instrumented {
    protected boolean locked;
    protected AbstractTMap.Functionality<T> keyFunc;

    public abstract void switchToSortedList();

    public abstract void lock();

    public abstract int size();

    public long getBytes() {
        return MemUsage.objectSize(MemUsage.booleanSize + MemUsage.pointerSize);
    }
}
