/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.utilz.appcontext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Класс, который обеспечивает доступ к applicationContext ({@link ApplicationContext}) из любой части кода.
 * Инициализируется в IoC-контейнере. Сохранение ссылки на applicationContext происходит в {@link me.nkozlov.Bootstrap}
 *
 * @author Kozlov Nikita
 */
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        applicationContext = ctx;
    }

    /**
     * Возвращает ссылку на applicationContext. Статический метод.
     *
     * @return {@link ApplicationContext}
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
