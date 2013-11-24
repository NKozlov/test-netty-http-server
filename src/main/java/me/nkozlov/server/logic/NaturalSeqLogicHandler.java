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
 * Формирует ряд натуральных положительных чисел. Thread-safe операции. Инициализируется в IoC-контейнере.
 *
 * @author Kozlov Nikita
 * @see LogicHandler
 */
public class NaturalSeqLogicHandler implements LogicHandler<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(NaturalSeqLogicHandler.class);

    FileFactory fileFactory;

    private AtomicInteger atomNextValue;

    public NaturalSeqLogicHandler(FileFactory fileFactory) {
        this.fileFactory = fileFactory;
    }

    /**
     * Thread-safe инкремент значения.
     */
    @Override
    public Integer executeLogic() {
        return atomNextValue.incrementAndGet();
    }

    /**
     * Инциализирует начальное значение из файла. Происходит каждый раз при запуске сервера.
     */
    public void doInit() {
        BufferedReader bufferedReader = null;
        String content = "";
        try {
            bufferedReader = fileFactory.getBufferedReader();
            if (bufferedReader != null) {
                content = bufferedReader.readLine();

                // если файл новый и до этого не был создан
                if (content == null) {
                    content = "0";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("[{}]: read value from file = {}", Thread.currentThread().getName(), content);
        this.atomNextValue = new AtomicInteger(Integer.valueOf(content));
    }
}
