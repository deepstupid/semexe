package semexe.basic;

import semexe.exec.Execution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static semexe.basic.LogInfo.*;

/**
 * Given a set of objects and a Processor that performs something on each of the
 * objects, launch a set of threads that does the computation in parallel.
 */
public class Parallelizer<T> {
    // Number of threads in the thread pool to use to process all the objects.
    private int numThreads;

    public Parallelizer(int numThreads) {
        this.numThreads = numThreads;
    }

    // Test
    public static void main(String[] args) {
        Parallelizer<String> p = new Parallelizer<>(10);
        List<String> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) items.add("item " + i);
        begin_track("main");
        LogInfo.begin_threads();
        p.process(items, new Processor<>() {
            public void process(String s, int i, int n) {
                begin_track("%d/%d: %s", i, n, s);
                logs("begin");
                Utils.sleep((i / 2) * 1000);
                logs("done");
                end_track();
            }
        });
        LogInfo.end_threads();
        end_track();
    }

    public void process(final List<T> points, final Processor<T> processor) {
        // Loop over examples in parallel
        final ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        final Ref<Throwable> exception = new Ref(null);
        for (int i = 0; i < points.size(); i++) {
            final int I = i;
            final T x = points.get(i);
            executor.execute(new Runnable() {
                public void run() {
                    if (Execution.shouldBail()) return;
                    try {
                        if (exception.value == null) {
                            processor.process(x, I, points.size());
                        }
                    } catch (Throwable t) {
                        exception.value = t; // Save exception
                    }
                }
            });
        }
        executor.shutdown();
        try {
            while (!executor.awaitTermination(1, TimeUnit.SECONDS)) ;
        } catch (InterruptedException e) {
            throw Exceptions.bad("Interrupted");
        }
        if (exception.value != null) {
            exception.value.printStackTrace();
            throw new RuntimeException(exception.value);
        }
    }

    // Override this interface.
    public interface Processor<T> {
        // This is called with object x
        // i: the index of x in the list
        // n: total number of objects in the list
        void process(T x, int i, int n);
    }
}
