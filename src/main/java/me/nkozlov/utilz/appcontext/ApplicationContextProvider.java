/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.utilz.appcontext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Kozlov Nikita
 */
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        applicationContext = ctx;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
