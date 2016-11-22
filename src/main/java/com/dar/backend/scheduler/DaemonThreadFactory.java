package com.dar.backend.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

class DaemonThreadFactory implements ThreadFactory {
    private final ThreadFactory factory;

    DaemonThreadFactory() {
        this(Executors.defaultThreadFactory());
    }

    private DaemonThreadFactory(ThreadFactory factory) {
        if (factory == null)
            throw new NullPointerException("factory cannot be null");
        this.factory = factory;
    }

    public Thread newThread(Runnable r) {
        final Thread t = factory.newThread(r);
        t.setDaemon(true);
        return t;
    }

}
