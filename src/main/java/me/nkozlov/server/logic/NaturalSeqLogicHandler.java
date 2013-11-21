/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.logic;

import me.nkozlov.server.logic.file.FileFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Kozlov Nikita
 */
public class NaturalSeqLogicHandler implements LogicHandler<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(NaturalSeqLogicHandler.class);

    FileFactory fileFactory;

    private AtomicInteger atomNextValue;

    public NaturalSeqLogicHandler(FileFactory fileFactory) {
        this.fileFactory = fileFactory;
        this.doInit();
    }

    /**
     * Thread-safe инкремент значения.
     */
    @Override
    public Integer executeLogic() {
        return atomNextValue.incrementAndGet();
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void doInit() {
        BufferedReader bufferedReader = null;
        String content = "";
        try {
            bufferedReader = fileFactory.getBufferedReader();
            if (bufferedReader != null) {
                content = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("[{}]: read value from file = {}", Thread.currentThread().getName(), content);
        this.atomNextValue = new AtomicInteger(Integer.valueOf(content));
    }
}