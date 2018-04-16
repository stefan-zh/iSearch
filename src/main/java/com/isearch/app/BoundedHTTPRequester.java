package com.isearch.app;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

class BoundedHTTPRequester {

    private static final int DEFAULT_POOL_SIZE = 20;

    BoundedHTTPRequester() {
        this(DEFAULT_POOL_SIZE, new LinkedBlockingQueue<>());
    }

    private BoundedHTTPRequester(
        int fixedPoolSize,
        BlockingQueue<?> tasks
    ) {

    }

    <T> Future<T> submit(Callable<T> task) {
        return null;
    }

}
