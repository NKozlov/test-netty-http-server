/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.logic.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * При инициализации создает нужный файл, если он не создан. Затем возвращает {@link BufferedReader} и {@link BufferedWriter
 * } при необходимости. Инициализируется в IoC-контейнере.
 *
 * @author Kozlov Nikita
 */
public class FileFactory {

    private static final Logger logger = LoggerFactory.getLogger(FileFactory.class);

    private Charset charset;
    private Path filePath;

    /**
     * Конструктор, на входе ждет из IoC-контейнера (при инициализации) Properties, где написан путь к файлу
     * с которым по умолчанию будет работать объект этого класса.
     */
    public FileFactory(Properties fileConfigProps) {
        this.doInit(fileConfigProps);
    }

    /**
     * Конструктор, который позволяет указать путь до файла.
     */
    public FileFactory(String strPath, String fileName) {
        this.doInit(strPath, fileName);
    }

    /**
     * Возвращает {@link BufferedWriter} для работы с текущим файлом.
     * По умолчанию текущий файл задается в file-settings.properties.
     */
    public BufferedWriter getBufferedWriter() throws IOException {
        return Files.newBufferedWriter(this.filePath, this.charset);
    }

    /**
     * Возвращает {@link BufferedReader} для работы с текущим файлом.
     * По умолчанию текущий файл задается в file-settings.properties.
     */
    public BufferedReader getBufferedReader() throws IOException {
        return Files.newBufferedReader(this.filePath, this.charset);
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    //    init default
    private void doInit(Properties fileConfigProps) {

        Path filePath = FileSystems.getDefault()
                .getPath(fileConfigProps.getProperty("file.sequence.natural.path"), fileConfigProps.getProperty("file.sequence.natural.name"));

        this.doInit(filePath);
    }

    private void doInit(String strPath, String fileName) {
        Path filePath = FileSystems.getDefault()
                .getPath(strPath, fileName);
        doInit(filePath);
    }

    private void doInit(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                logger.error("[{}]: {} \n {}", Thread.currentThread().getName(), e.getMessage(), e.getStackTrace());
                throw new RuntimeException(e);
            }
        }

        this.filePath = path;
        this.charset = Charset.forName("UTF-8");
    }
}
