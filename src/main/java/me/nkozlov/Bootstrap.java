/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov;

import me.nkozlov.console.listener.ConsoleEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Kozlov Nikita
 */
public class Bootstrap {

    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    public static void main(String[] args) {
        ApplicationContext applicationContext;
        try {
            applicationContext =
                    new ClassPathXmlApplicationContext("classpath*:applicationContext.xml");
        } catch (BeanDefinitionStoreException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        Thread threadConsoleListener = new Thread((ConsoleEventListener) applicationContext.getBean("consoleEventListener"));

        threadConsoleListener.start();

        //        for debugging
        try {
            threadConsoleListener.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //        end

    }
}
