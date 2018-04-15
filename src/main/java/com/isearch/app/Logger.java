package com.isearch.app;

import java.io.OutputStream;
import java.io.PrintStream;

// System.out is not thread-safe for simple logging.
// PrintStream wrapped around System.out is thread-safe but will build up buffer
// over time that needs to be flushed: https://stackoverflow.com/a/7554564
class Logger {

    private final PrintStream logger;

    Logger(OutputStream os) {
        logger = new PrintStream(os);
    }

    // thread-safe println(String)
    void println(String s) {
        logger.println(s);
        logger.flush();
    }

    // thread-safe println(long)
    void println(long l) {
        logger.println(l);
        logger.flush();
    }

}
