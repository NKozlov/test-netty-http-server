/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.logic.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Класс, обеспечивающий thread-safe операции с файлами. Инициализируется в IoC-контейнере.
 *
 * @author Kozlov Nikita
 */
public class FileExecutor {

    private static final Logger logger = LoggerFactory.getLogger(FileFactory.class);

    @Autowired
    private FileFactory fileFactory;

    /**
     * Запись в файл. Thread-safe операция.
     */
    public synchronized void writeToFile(String content) throws IOException {
        logger.debug("[{}]: writeToFile({})", Thread.currentThread().getName(), content);
        try (BufferedWriter bufferedWriter = fileFactory.getBufferedWriter()) {
            bufferedWriter.write(content, 0, content.length());
            bufferedWriter.flush();
        }
    }
}
