package com.factory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BeanFactory {
    private static Properties properties;

    private static Map<String,Object> beanMap = new HashMap<>();

    // 静态代码块初始化加载配置文件
    static {
        properties = new Properties();
        try {
            // 使用类加载起加载resources目录下的配置文件
            properties.load(BeanFactory.class.getClassLoader().getResourceAsStream("factory.properties"));
        } catch (IOException e) {
            throw new ExceptionInInitializerError("BeanFactory initialize error, cause: " + e.getMessage());
        }
    }

    // 根据传进来的名字构造返回实例
    public static Object getBean(String beanName) {
        if(!beanMap.containsKey(beanName)) {
            synchronized (BeanFactory.class) {
                if(!beanMap.containsKey(beanName)) {
                    try {
                        Class<?> beanClass = Class.forName(properties.getProperty(beanName));
                        Object bean = beanClass.newInstance();
                        beanMap.put(beanName,bean);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException("BeanFactory have not [ " + beanName + "]bean",e);
                    } catch (IllegalAccessException | InstantiationException e) {
                        throw new RuntimeException("[" + beanName + "] instantiation error!", e);
                    }
                }
            }
        }
        return beanMap.get(beanName);

    }
}
