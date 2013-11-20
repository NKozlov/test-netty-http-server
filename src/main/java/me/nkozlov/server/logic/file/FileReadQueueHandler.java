/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.logic.file;

import me.nkozlov.server.logic.AbstractReadQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;

/**
 * @author Kozlov Nikita
 */
public final class FileReadQueueHandler extends AbstractReadQueue<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(FileReadQueueHandler.class);

    public FileReadQueueHandler(int threadPoolSize) {
        super(threadPoolSize);
    }

    public ExecutorService getThreadPool(){
        return threadPool;
    }

    @Override
    public void run() {
        logger.debug("{} START.", Thread.currentThread().getName());
        Path file = FileSystems.getDefault().getPath("src/main/resources", "example.txt");
        logger.debug("path to file = {}", file);
        try {
            Files.createFile(file);
            logger.info("File {} was create.", file);
        } catch (IOException e) {
            logger.info("File named {} already exists", file);
        }

        for (int i = 0; i < 4; i++) {
            logger.trace("Loop i = {}", i);
            Charset charset = Charset.forName("UTF-8");
            String s = "example " + i;

            try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
                writer.write(s, 0, s.length());
            } catch (IOException x) {
                System.err.format("IOException: %s%n", x);
            }
        }
    }
}
