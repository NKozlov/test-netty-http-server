/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.logic;

import me.nkozlov.server.logic.session.HttpSessionReadQueueHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Kozlov Nikita
 */
abstract public class AbstractReadQueue<E> implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(HttpSessionReadQueueHandler.class);

    protected final BlockingQueue<E> sessionQueue;
    protected final ExecutorService threadPool;
    protected int threadPoolSize;

    protected AbstractReadQueue(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
        this.threadPool = Executors.newFixedThreadPool(threadPoolSize);
        this.sessionQueue = new LinkedBlockingQueue<E>();

        initThreadPool();
    }

    public void addTaskToQueue(E task) {
        if (task != null) {
            logger.debug("addSessionToProcess({})", task);
            this.sessionQueue.add(task);
        }
    }

    public BlockingQueue<E> getSessionQueue() {
        return this.sessionQueue;
    }

    protected void initThreadPool() {
        for (int i = 0; i < this.threadPoolSize; i++) {
            this.threadPool.execute(this);
        }
    }
}
