/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov;

import me.nkozlov.server.ServerResources;
import me.nkozlov.server.logic.packet.HttpPacketReadQueueHandler;
import me.nkozlov.utilz.appcontext.ApplicationContextProvider;
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

        //        create thread for console
        Thread threadConsoleListener = applicationContext.getBean("threadConsoleListener", Thread.class);
        threadConsoleListener.setName("ConsoleListener Thread");

        /*ThreadFactory threadFactory = Executors.privilegedThreadFactory();
                threadFactory.newThread();*/
        //        add applicationContext to  applicationContextProvider

//        инициализация
        applicationContext.getBean("applicationContextProvider", ApplicationContextProvider.class).setApplicationContext(applicationContext);
        applicationContext.getBean("serverResources", ServerResources.class).setReadQueueHandler(applicationContext.getBean("readQueueHandler",
                HttpPacketReadQueueHandler.class));

        threadConsoleListener.start();
    }
}
