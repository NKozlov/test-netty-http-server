/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov;

import me.nkozlov.console.listener.ConsoleEventListener;
import me.nkozlov.utilz.appcontext.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main-класс, который инициализирует основные ресурсы, в том числе и Console Listener (в отдельном потоке с именем "ConsoleListener Thread"). <br/>
 * Так же инициализируется applicationContext, который сохраняется в классе {@link ApplicationContextProvider}.
 * Класс является точкой входа в приложение. Содержит метод {@link Bootstrap#main(String[])}.
 *
 * @author Kozlov Nikita
 * @see me.nkozlov.console.listener.ConsoleEventListener
 * @see me.nkozlov.console.ConsoleEventHandler
 * @see ApplicationContextProvider
 */
public class Bootstrap {

    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    /**
     * Основной метод, с которого начинается выполнение проложения.
     *
     * @see Bootstrap
     */
    public static void main(String[] args) {
        ApplicationContext applicationContext;
        try {
            applicationContext =
                    new ClassPathXmlApplicationContext("classpath*:applicationContext.xml");
        } catch (BeanDefinitionStoreException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        // создаем поток для Console Listener
        ConsoleEventListener consoleEventListener = applicationContext.getBean("consoleEventListener", ConsoleEventListener.class);
        Thread threadConsoleListener = new Thread(consoleEventListener);
        threadConsoleListener.setName("ConsoleListener Thread");

        //  сохраняем applicationContext в  статическом поле класса  ApplicationContextProvider
        applicationContext.getBean("applicationContextProvider", ApplicationContextProvider.class).setApplicationContext(applicationContext);
        /*applicationContext.getBean("serverResources", ServerResources.class).setReadQueueHandler(applicationContext.getBean("readQueueHandler",
                HttpSessionReadQueueHandler.class));*/

        threadConsoleListener.start();
    }
}
