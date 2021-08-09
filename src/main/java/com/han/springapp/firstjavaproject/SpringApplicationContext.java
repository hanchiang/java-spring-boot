package com.han.springapp.firstjavaproject;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * This class allows us to access beans from anywhere in the application
 */
public class SpringApplicationContext implements ApplicationContextAware {
    private static ApplicationContext CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        CONTEXT = context;
    }

    /**
     * When beans are created by Spring, the name of the bean will be in camelCase, i.e. first letter is lower case
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName) {
        return CONTEXT.getBean(beanName);
    }
}
