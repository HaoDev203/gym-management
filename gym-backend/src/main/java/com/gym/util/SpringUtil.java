package com.gym.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring 工具类。
 *
 * <p>用途：在静态方法中获取 Spring Bean。</p>
 *
 * @author liuxinsi
 * @date 2026-06-08
 */
@Component
public final class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    /**
     * 获取 Spring Bean。
     *
     * @param clazz Bean 类型
     * @return Spring Bean 实例
     */
    public static <T> T getBean(Class<T> clazz) {
        if (applicationContext == null) {
            throw new IllegalStateException("ApplicationContext 未初始化");
        }
        return applicationContext.getBean(clazz);
    }
}
