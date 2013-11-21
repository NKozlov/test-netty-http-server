/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.logic.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * todo Document type TestFile
 */
public class TestFile {

    private static final Logger logger = LoggerFactory.getLogger(TestFile.class);

    public static void main(String[] args) {
        ApplicationContext applicationContext;
        try {
            applicationContext =
                    new ClassPathXmlApplicationContext("classpath*:applicationContext.xml");
        } catch (BeanDefinitionStoreException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        FileReadQueueHandler fileReadQueueHandler = new FileReadQueueHandler(1);
        fileReadQueueHandler.getThreadPool().shutdown();
        try {
            if (!fileReadQueueHandler.getThreadPool().awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS)) {
                fileReadQueueHandler.getThreadPool().shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fileReadQueueHandler.getThreadPool().shutdownNow();
        int debug = 1;
    }
}
