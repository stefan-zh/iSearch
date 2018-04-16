package com.isearch.app;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;

// This Concurrent HTTP Requester resembles a thread pool but is not reusable.
// This is a very minimalistic version of a thread pool that is custom built to
// serve no more than 20 HTTP Requests concurrently.
class ConcurrentHttpRequester<T> {

    // upper limit on the number of threads
    private static final int POOL_SIZE_LIMIT = 20;

    private final Set<Thread> threadPool;
    private final List<T> results;

    ConcurrentHttpRequester(BlockingQueue<? extends FutureTask<T>> tasks) {
        BlockingQueue<? extends FutureTask<T>> workQueue = tasks != null ? tasks : new LinkedBlockingQueue<>();
        // pool size: 1 <= poolSize <= POOL_SIZE_LIMIT
        int poolSize = Math.min(workQueue.size(), POOL_SIZE_LIMIT);

        // initialize the thread pool
        threadPool = new HashSet<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            WorkerThread thread = new WorkerThread(workQueue);
            this.threadPool.add(thread);
        }

        results = new LinkedList<>();
    }

    /**
     * Execute all the tasks in the thread pool and return their results.
     *
     * @return the list of results from the tasks
     */
    List<T> execute() {
        // start the threads
        threadPool.forEach(Thread::start);

        // await thread completion
        awaitCompletion();

        // return results
        return this.results;
    }

    // will stop when all threads are complete
    private void awaitCompletion() {
        while (true) {
            // check if any threads are still alive
            boolean anyAlive = threadPool.stream().anyMatch(Thread::isAlive);
            if (!anyAlive) {
                break;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                // do nothing
            }
        }
    }

    // helper Worker Thread class
    private class WorkerThread extends Thread {

        // pointer to the queue with tasks
        private final BlockingQueue<? extends FutureTask<T>> workQueue;

        WorkerThread(BlockingQueue<? extends FutureTask<T>> workQueue) {
            this.workQueue = workQueue;
        }

        @Override
        public void run() {
            while (!workQueue.isEmpty()) {
                // poll removes a task from the queue and returns it
                FutureTask<T> task = workQueue.poll();
                try {
                    if (task != null) {
                        task.run();
                        T result = task.get();
                        results.add(result);
                    }
                } catch (RuntimeException | InterruptedException | ExecutionException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}
