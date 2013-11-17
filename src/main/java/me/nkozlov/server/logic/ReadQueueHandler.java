/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.logic;

import me.nkozlov.server.logic.session.Session;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Kozlov Nikita
 */
public final class ReadQueueHandler {

    private final BlockingQueue<Session> sessionQueue;
    private final ExecutorService threadPool;
    private int threadPoolSize;

    public ReadQueueHandler(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
        this.threadPool = Executors.newFixedThreadPool(threadPoolSize);
        this.sessionQueue = new LinkedBlockingQueue<>();
    }
}
