/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnologiabasica.jettyapicommons.util;

/**
 *
 * @author afonso
 */
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class JThreadFactoryBuilder {

    private String namePrefix = null;
    private boolean daemon = false;
    private int priority = Thread.NORM_PRIORITY;
    private ThreadFactory backingThreadFactory = null;
    private UncaughtExceptionHandler uncaughtExceptionHandler = null;

    public JThreadFactoryBuilder setNamePrefix(String namePrefix) {
        if (namePrefix == null) {
            throw new NullPointerException();
        }
        this.namePrefix = namePrefix;
        return this;
    }

    public JThreadFactoryBuilder setDaemon(boolean daemon) {
        this.daemon = daemon;
        return this;
    }

    public JThreadFactoryBuilder setPriority(int priority) {
        if (priority < Thread.MIN_PRIORITY) {
            throw new IllegalArgumentException(String.format(
                    "Thread priority (%s) must be >= %s", priority,
                    Thread.MIN_PRIORITY));
        }

        if (priority > Thread.MAX_PRIORITY) {
            throw new IllegalArgumentException(String.format(
                    "Thread priority (%s) must be <= %s", priority,
                    Thread.MAX_PRIORITY));
        }

        this.priority = priority;
        return this;
    }

    public JThreadFactoryBuilder setUncaughtExceptionHandler(
            UncaughtExceptionHandler uncaughtExceptionHandler) {
        if (null == uncaughtExceptionHandler) {
            throw new NullPointerException(
                    "UncaughtExceptionHandler cannot be null");
        }
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
        return this;
    }

    public JThreadFactoryBuilder setThreadFactory(
            ThreadFactory backingThreadFactory) {
        if (null == uncaughtExceptionHandler) {
            throw new NullPointerException(
                    "BackingThreadFactory cannot be null");
        }
        this.backingThreadFactory = backingThreadFactory;
        return this;
    }

    public ThreadFactory build() {
        return build(this);
    }

    private static ThreadFactory build(JThreadFactoryBuilder builder) {
        final String namePrefix = builder.namePrefix;
        final Boolean daemon = builder.daemon;
        final Integer priority = builder.priority;
        final UncaughtExceptionHandler uncaughtExceptionHandler = builder.uncaughtExceptionHandler;
        final ThreadFactory backingThreadFactory = (builder.backingThreadFactory != null) ? builder.backingThreadFactory
                : Executors.defaultThreadFactory();

        final AtomicLong count = new AtomicLong(0);

        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = backingThreadFactory.newThread(runnable);
                if (namePrefix != null) {
                    thread.setName(namePrefix + "-" + count.getAndIncrement());
                }
                if (daemon != null) {
                    thread.setDaemon(daemon);
                }
                if (priority != null) {
                    thread.setPriority(priority);
                }
                if (uncaughtExceptionHandler != null) {
                    thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
                }
                return thread;
            }
        };
    }
}
